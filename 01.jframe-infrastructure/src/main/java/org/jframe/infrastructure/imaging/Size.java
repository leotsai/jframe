package org.jframe.infrastructure.imaging;

import java.awt.*;

/**
 * Created by Leo on 2017/1/12.
 */
public class Size {
    private int width;
    private int height;

    public Size(){

    }

    public Size(int width, int height){
        this.width = width;
        this.height = height;
    }

    public int getWidth(){
        return width;
    }

    public void setWidth(int value){
        this.width = value;
    }

    public int getHeight(){
        return height;
    }

    public void setHeight(int value){
        this.height = value;
    }

    public Size scaleTo(Size target){
        int targetWidth = target.getWidth();
        int targetHeight = target.getHeight();


        if (targetWidth == 0 && targetHeight == 0)
        {
            return this;
        }
        if (targetWidth == 0)
        {
            float rate = (float) this.height/targetHeight;
            if (rate <= 1)
            {
                return new Size(this.width, this.height);
            }
            return new Size((int) (this.width/rate), targetHeight);
        }

        if (targetHeight == 0)
        {
            float rate = (float) this.width/targetWidth;
            if (rate <= 1)
            {
                return new Size(this.width, this.height);
            }
            return new Size(targetWidth, (int) (this.height/rate));
        }

        Size size = new Size();
        float r = Math.max(((float) this.width/targetWidth), ((float) this.height)/targetHeight);
        if (r <= 1)
        {
            return new Size(this.width, this.height);
        }
        size.setWidth((int) Math.round(this.width/r));
        size.setHeight((int) Math.round(this.height/r));
        if (size.getWidth() == 0)
        {
            size.setWidth(1);
        }
        if (size.getHeight() == 0)
        {
            size.setHeight(1);
        }
        return size;
    }

    public Size scaleTo(int maxLenth) {
        int minLength = Math.min(this.height, this.width);
        if (minLength <= maxLenth) {
            return this;
        }
        float rate = ((float) maxLenth) / minLength;
        return new Size((int)(this.width * rate), (int)(this.height * rate));
    }


    public boolean isSame(Size size){
        return this.width == size.getWidth() && this.height == size.getHeight();
    }

    public Point getCenter(Size size){
        return new Point((this.width - size.getWidth())/2, (this.height - size.getHeight())/2);
    }

}
