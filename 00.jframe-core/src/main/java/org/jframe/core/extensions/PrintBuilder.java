package org.jframe.core.extensions;


import org.jframe.core.logging.LogHelper;

/**
 * Created by leo on 2018-12-11.
 */
public class PrintBuilder {

    private final StringBuilder sb = new StringBuilder();

    public PrintBuilder out(String message) {
        System.out.println(message);
        this.sb.append(message + LogHelper.getLineBreak());
        return this;
    }

    public PrintBuilder err(String message) {
        System.err.println(message);
        this.sb.append(message + LogHelper.getLineBreak());
        return this;
    }

    public String getMessages(){
        return this.sb.toString();
    }

    public void clear(){
        this.sb.delete(0, this.sb.length());
    }

}
