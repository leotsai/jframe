package org.jframe.infrastructure.imaging;

import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;


/**
 * Created by Leo on 2017/1/12.
 */
public class ImageHeper {
    public static void resize(File sourceImage, File destImage, Size maxSize) throws Exception {
        OutputStream output = null;
        try {
            output = new FileOutputStream(destImage);
            resize(sourceImage, output, maxSize);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } finally {
            IOUtils.closeQuietly(output);
        }
    }

    public static void cropCenter(File sourceImage, File destImage, int width) throws Exception {
        OutputStream output = null;
        try {
            output = new BufferedOutputStream(new FileOutputStream(destImage));
            cropCenter(sourceImage, output, width);
        } catch (IOException e) {
            throw new Exception(e);
        }
        finally {
            IOUtils.closeQuietly(output);
        }
    }

    private static void resize(File input, OutputStream output, Size maxSize) throws Exception {
        BufferedImage img = ImageIO.read(input);
        boolean hasAlpha = img.getColorModel().hasAlpha();
        Size size = new Size(img.getWidth(null), img.getHeight(null)).scaleTo(maxSize);
        BufferedImage image = new BufferedImage(size.getWidth(), size.getHeight(), hasAlpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(img.getScaledInstance(size.getWidth(), size.getHeight(), Image.SCALE_SMOOTH), 0, 0, null);
        ImageIO.write(image, hasAlpha ? "png" : "jpg", output);
    }

    private static void cropCenter(File input, OutputStream output, int maxWidth) throws Exception {
        BufferedImage sourceImage = ImageIO.read(input);
        boolean hasAlpha = sourceImage.getColorModel().hasAlpha();
        Size originalSize = new Size(sourceImage.getWidth(), sourceImage.getHeight());
        Size scaledSize = originalSize.scaleTo(maxWidth);
        Image tempImage = sourceImage;
        if(scaledSize.isSame(originalSize) == false){
            tempImage = sourceImage.getScaledInstance(scaledSize.getWidth(), scaledSize.getHeight(), Image.SCALE_SMOOTH);
        }
        int minLength = Math.min(scaledSize.getWidth(), scaledSize.getHeight());
        Point center = scaledSize.getCenter(new Size(minLength, minLength));
        BufferedImage tag = new BufferedImage(minLength, minLength, hasAlpha? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
        tag.getGraphics().drawImage(tempImage, 0, 0, minLength, minLength,center.x,center.y,minLength + center.x, minLength + center.y,null);
        ImageIO.write(tag, hasAlpha ? "png" : "jpg", output);
    }


}
