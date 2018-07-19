package org.jframe.core.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jframe.core.exception.KnownException;
import org.jframe.core.extensions.Action;
import org.jframe.core.extensions.JList;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leo on 2017-09-23.
 */
public class WebClient implements AutoCloseable {

    private URI uri;
    private int timeoutMilliSeconds = 120000;
    private String charset;
    private HttpHost proxy;
    private ContentType contentType;
    private String userAgent;
    private final Map<String, String> headers;
    private CookieStore cookieStore = null;

    private CloseableHttpClient httpClient = null;
    private CloseableHttpResponse httpResponse = null;

    public WebClient() {
        this.charset = "UTF-8";
        this.contentType = ContentType.TEXT_HTML;
        this.headers = new HashMap<>();
    }

    public WebClient setHeader(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    public String downloadString(String fullUrl) throws Exception {
        this.contentType = ContentType.TEXT_HTML;
        return this.getEntityAsString(this.execute(new HttpGet(fullUrl)));
    }

    public InputStream downloadStream(String fullUrl) throws Exception {
        this.contentType = ContentType.APPLICATION_OCTET_STREAM;
        return this.execute(new HttpGet(fullUrl)).getContent();
    }

    public InputStream uploadStream(String fullUrl, String data) throws Exception {
        this.contentType = ContentType.APPLICATION_OCTET_STREAM;
        HttpPost post = new HttpPost(fullUrl);
        post.setEntity(new ByteArrayEntity(data.getBytes("UTF-8")));
        return this.execute(post).getContent();
    }

    public void downloadFile(String fullUrl, File file) throws Exception {
        this.contentType = ContentType.APPLICATION_OCTET_STREAM;
        HttpEntity entity = this.execute(new HttpGet(fullUrl));
        FileOutputStream output = new FileOutputStream(file);
        byte[] buffer = new byte[(int) entity.getContentLength()];
        entity.getContent().read(buffer);
        output.write(buffer);
        output.close();
        entity.getContent().close();
    }

    public String post(String fullUrl, String data) throws Exception {
        return this.post(fullUrl, data.getBytes(this.charset));
    }

    public String post(String fullUrl, byte[] bytes) throws Exception {
        HttpPost post = new HttpPost(fullUrl);
        post.setEntity(new ByteArrayEntity(bytes));
        this.contentType = ContentType.APPLICATION_FORM_URLENCODED;
        return this.getEntityAsString(this.execute(post));
    }

    public String postFile(String fullUrl, File file) throws Exception {
        return this.postFiles(fullUrl, new File[]{file});
    }

    public String postFiles(String fullUrl, File[] files) throws Exception {
        return this.postFiles(fullUrl, files, null);
    }

    public String postFiles(String fullUrl, File[] files, List<FormItem> extraData) throws Exception {
        HttpPost post = new HttpPost(fullUrl);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        if (files == null || files.length == 0) {
            throw new KnownException("files parameter is null or empty");
        }
        for (File file : files) {
            builder.addPart("file", new FileBody(file));
        }
        if (extraData != null) {
            for (FormItem item : extraData) {
                builder.addBinaryBody(item.getName(), item.getValue());
            }
        }
        post.setEntity(builder.build());
        this.contentType = null;
        return this.getEntityAsString(this.execute(post));
    }

    private HttpEntity execute(HttpRequestBase requestBase) throws Exception {
        this.close();
        this.uri = requestBase.getURI();
        this.httpClient = this.buildClient();
        if (this.contentType != null) {
            requestBase.setHeader("Content-Type", this.contentType.getValue());
        }
        if (this.cookieStore != null) {
            String cookies = String.join(";", JList.from(this.cookieStore.getCookies()).select(x -> x.getName() + "=" + x.getValue()));
            requestBase.setHeader("Cookie", cookies);
        }
        for (String key : this.headers.keySet()) {
            requestBase.setHeader(key, this.headers.get(key));
        }
        requestBase.setConfig(this.getConfig());
        this.httpResponse = this.httpClient.execute(requestBase);
        return this.httpResponse.getEntity();
    }

    private String getEntityAsString(HttpEntity entity) throws Exception {
        return entity == null ? null : EntityUtils.toString(entity, this.charset);
    }

    private RequestConfig getConfig() {
        RequestConfig.Builder builder = RequestConfig.custom();
        builder.setConnectTimeout(this.timeoutMilliSeconds);
        return builder.build();
    }

    private CloseableHttpClient buildClient() {
        HttpClientBuilder builder = HttpClients.custom();
        builder.setUserAgent(this.userAgent);
        builder.setProxy(this.proxy);
        builder.setDefaultCookieStore(this.cookieStore);
        return builder.build();
    }

    private void releaseCloseable(Closeable closeable, Action setNull) throws Exception {
        if (closeable != null) {
            closeable.close();
            setNull.apply();
        }
    }

    @Override
    public void close() throws Exception {
        this.releaseCloseable(this.httpClient, () -> this.httpClient = null);
        this.releaseCloseable(this.httpResponse, () -> this.httpResponse = null);
    }

    // getters & setters ---------------------------------------------------------------------------------


    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public HttpHost getProxy() {
        return proxy;
    }

    public void setProxy(HttpHost proxy) {
        this.proxy = proxy;
    }

    public int getTimeoutMilliSeconds() {
        return timeoutMilliSeconds;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }

    public void setCookieStore(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }

    public URI getUri() {
        return uri;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }
}
