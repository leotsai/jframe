package org.jframe.core.http;

/**
 * Created by leo on 2017-09-24.
 */
public enum ContentType {
    TEXT_PLAIN("text/plain; charset=UTF-8"),
    TEXT_HTML("text/html; charset=UTF-8"),
    TEXT_XML("text/xml; charset=UTF-8"),
    MULTIPART_FORM_DATA("multipart/form-data; charset=UTF-8"),
    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded; charset=UTF-8"),
    APPLICATION_JSON("application/json; charset=UTF-8"),
    APPLICATION_OCTET_STREAM("application/octet-stream; charset=UTF-8"),
    APPLICATION_XHTML_XML("application/xhtml+xml; charset=UTF-8"),
    APPLICATION_XML("application/xml; charset=UTF-8");

    private String value;

    private ContentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}