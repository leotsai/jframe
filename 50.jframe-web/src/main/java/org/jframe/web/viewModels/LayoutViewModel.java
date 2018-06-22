package org.jframe.web.viewModels;


/**
 * Created by leo on 2016-12-16.
 */
public class LayoutViewModel<T> {

    private String error;
    private String title;
    private T value;

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
