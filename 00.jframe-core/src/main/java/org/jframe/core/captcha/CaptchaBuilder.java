package org.jframe.core.captcha;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Created by leo on 2017-09-17.
 */
public class CaptchaBuilder {
    private static final int width = 160;
    private static final int height = 40;
    private static final int lineCount = 150;

    public static BufferedImage build(String code) {
        int codeWidth = width / (code.length() + 2);
        int fontSize = height - 2;
        int codeY = height - 4;

        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();
        Random random = new Random();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Fixedsys", Font.PLAIN, fontSize));

        drawLines(random, g);

        for (int i = 0; i < code.length(); i++) {
            g.setColor(getLetterColor());
            g.drawString(String.valueOf(code.charAt(i)), (i + 1) * codeWidth, codeY);
        }
        return buffImg;
    }

    private static Color getRandomColor(Random random) {
        return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    private static Color getLetterColor() {
        return new Color(50, 50, 50);
    }

    private static Color getLineColor() {
        return new Color(233, 233, 233);
    }

    private static void drawLines(Random random, Graphics2D g) {
        for (int i = 0; i < lineCount; i++) {
            int xStart = random.nextInt(width);//x坐标开始
            int yStart = random.nextInt(height);//y坐标开始
            int xEnd = xStart + random.nextInt(width / 8);//x坐标结束
            int yeEnd = yStart + random.nextInt(height / 8);//y坐标结束
            g.setColor(getLineColor());
            g.drawLine(xStart, yStart, xEnd, yeEnd);
        }
    }

}
