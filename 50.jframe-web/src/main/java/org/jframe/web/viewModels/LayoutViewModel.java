package org.jframe.web.viewModels;

/**
 * Created by leo on 2016-12-16.
 */
public class LayoutViewModel<T>{

    private String error;
    private String title;
    private T model;

    public String getError(){
        return error;
    }

    public void setError(String value){
        this.error = value;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String value){
        title = value;
    }

    public T getModel(){
        return model;
    }

    public void setModel(T value){
        this.model = value;
    }

}
