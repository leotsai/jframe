package org.jframe.web.viewModels;


/**
 * Created by leo on 2016-12-16.
 */
public class LayoutViewModel<T> {
    private String html;
    private String title;
    private String keywords;
    private String description;
    private String error;
    private T value;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public LayoutViewModel() {

    }


    public LayoutViewModel(String title) {
        this.title = title;
    }

    public LayoutViewModel(String title, T value) {
        this.title = title;
        this.value = value;
    }


    public String getError() {
        return error;
    }

    public void setError(String value) {
        this.error = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        title = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
