package org.jframe.infrastructure.imaging;

/**
 * Created by Leo on 2017/1/12.
 */
public enum ImageSize {
    Full(0, 0),
    S80X80(80, 80),
    S200X200(200, 200),
    W200(200, 0),
    H100(0, 100);

    private Size size;

    private ImageSize(int width, int height) {
        this.size = new Size(width, height);
    }

    public Size getSize() {
        return size;
    }
}
