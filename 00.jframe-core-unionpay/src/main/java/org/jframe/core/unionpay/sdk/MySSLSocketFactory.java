package org.jframe.core.unionpay.sdk;



import org.apache.commons.httpclient.HttpClientError;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;

public class MySSLSocketFactory implements ProtocolSocketFactory {
	private SSLContext sslcontext = null;

	public MySSLSocketFactory() {
	}

	private static SSLContext createEasySSLContext() {
		try {
			SSLContext context = SSLContext.getInstance("SSL");
			context.init(null, new TrustManager[] { new MyX509TrustManager() },
					null);
			return context;
		} catch (Exception e) {
			throw new HttpClientError(e.toString());
		}
	}

	private SSLContext getSSLContext() {
		if (this.sslcontext == null) {
            this.sslcontext = createEasySSLContext();
        }
		return this.sslcontext;
	}

	@Override
	public Socket createSocket(String host, int port, InetAddress clientHost,
							   int clientPort) throws IOException, UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(host, port,
				clientHost, clientPort);
	}

	@Override
	public Socket createSocket(String host, int port, InetAddress localAddress,
							   int localPort, HttpConnectionParams params) throws IOException,
			UnknownHostException {
		return createSocket(host, port, localAddress, localPort);
	}

	@Override
	public Socket createSocket(String host, int port) throws IOException,
			UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(host, port);
	}

	@Override
	public boolean equals(Object obj) {
		return ((obj != null) && obj.getClass()
				.equals(MySSLSocketFactory.class));
	}

	@Override
	public int hashCode() {
		return MySSLSocketFactory.class.hashCode();
	}

	public static class MyX509TrustManager implements X509TrustManager {
		public MyX509TrustManager() {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}
	}
}
