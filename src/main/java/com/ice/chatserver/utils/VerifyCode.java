package com.ice.chatserver.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @Description: 验证码的一个生成类
 * @Author: ice2020x
 * @Date: 2021/11/11
 */
public class VerifyCode {

    public static String drawRandomText(int width, int height, BufferedImage verifyImg) {

        Graphics2D graphics = (Graphics2D) verifyImg.getGraphics();

        graphics.setColor(Color.WHITE);

        graphics.fillRect(0, 0, width, height);

        graphics.setFont(new Font("微软雅黑", Font.BOLD, 40));

        //数字和字母的组合

        String baseNumLetter = "123456789abcdefghijklmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";

        StringBuffer sBuffer = new StringBuffer();

        int x = 10;  //旋转原点的 x 坐标
        String ch = "";
        Random random = new Random();
        for (int i = 0; i < 4; i++) {

            graphics.setColor(getRandomColor());

            int degree = random.nextInt() % 30;

            int dot = random.nextInt(baseNumLetter.length());

            ch = baseNumLetter.charAt(dot) + "";

            sBuffer.append(ch);

            //正向旋转

            graphics.rotate(degree * Math.PI / 180, x, 45);

            graphics.drawString(ch, x, 45);

            //反向旋转

            graphics.rotate(-degree * Math.PI / 180, x, 45);

            x += 48;
        }

        //画干扰线

        for (int i = 0; i < 6; i++) {

            // 设置随机颜色
            graphics.setColor(getRandomColor());
            // 随机画线

            graphics.drawLine(random.nextInt(width), random.nextInt(height),
                    random.nextInt(width), random.nextInt(height));

        }

        //添加噪点
        for (int i = 0; i < 30; i++) {

            int x1 = random.nextInt(width);

            int y1 = random.nextInt(height);

            graphics.setColor(getRandomColor());

            graphics.fillRect(x1, y1, 2, 2);

        }
        return sBuffer.toString();

    }
    private static Color getRandomColor() {
        Random ran = new Random();
        return new Color(ran.nextInt(256),
                ran.nextInt(256), ran.nextInt(256));

    }

}
