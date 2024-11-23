package org.example;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

class Ball {
    public static final int DIAMETER = 70; // Размер шарика
    private int x, y;
    private int speed;
    private int imageIndex;
    private BufferedImage image;

    public Ball(int x, int y, int imageIndex) {
        this.x = x;
        this.y = y;
        this.imageIndex = imageIndex;
        this.image = loadBallImage(imageIndex); // Загружаем текстуру
        this.speed = 2; // Устанавливаем скорость падения для плавного движения
    }

    private BufferedImage loadBallImage(int index) {
        String imagePath = null;
        switch (index) {
            case 0:
                imagePath = "/ball_texture1.png";
                break;
            case 1:
                imagePath = "/ball_texture2.png";
                break;
            case 2:
                imagePath = "/ball_texture3.png";
                break;
            default:
                return null;
        }

        try {
            return ImageIO.read(getClass().getResourceAsStream(imagePath)); // Загружаем изображение
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, DIAMETER, DIAMETER, null); // Рисуем текстуру шарика
    }

    public void updatePosition() {
        y += speed; // Шарик падает вниз
    }

    public boolean contains(int x, int y) {
        // Увеличиваем границы попадания
        int hitBoxPadding = 30; // Увеличение зоны попадания
        return (x >= this.x - hitBoxPadding && x <= this.x + DIAMETER + hitBoxPadding) &&
                (y >= this.y - hitBoxPadding && y <= this.y + DIAMETER + hitBoxPadding); // Проверяем на попадание
    }

    public int getY() {
        return y; 
    }
}