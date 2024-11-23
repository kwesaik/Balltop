package org.example;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

public class BallCatchingGame extends JPanel implements ActionListener {
    private static final int MAX_BALLS = 5; // Максимальное количество шариков
    private static final int TIMER_DELAY = 80; // Скорость обновления игры
    private final ArrayList<Ball> balls = new ArrayList<>();
    private final Timer timer;
    private int score = 0;
    private BufferedImage backgroundImage;
    private boolean gameOver = false;
    private JButton nukeButton; // Кнопка "Ядерка" уничтожает все шары(покупка за очки)

    public BallCatchingGame() {
        setBackground(Color.RED);
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize()); // Установка размера в полный экран
        timer = new Timer(TIMER_DELAY, this); // Настройка таймера

        loadBackgroundImage();
        playBackgroundMusic();
        setupMouseListener();
        setupNukeButton();
        timer.start();
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/background_image.png")); // загружаем фон
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playBackgroundMusic() {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/background_music.wav")));
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Циклическое воспроизведение музыки
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                checkBallHit(e.getX(), e.getY()); // Проверка ударов о шар
            }
        });
    }

    private void setupNukeButton() {
        nukeButton = new JButton("Ядерка");
        nukeButton.setBackground(Color.RED);
        nukeButton.setForeground(Color.WHITE);
        nukeButton.setFocusPainted(false);
        nukeButton.setBorderPainted(false);
        nukeButton.setOpaque(true);
        nukeButton.setVisible(false);

        nukeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (score >= 30) {
                    score -= 20; // Уменьшаем счет на 20
                    balls.clear(); // Удаляем все шарики
                }
                updateNukeButton(); // Обновляем состояние кнопки
            }
        });

        add(nukeButton); // Добавляем кнопку на панель
    }

    private void updateNukeButton() {
        nukeButton.setVisible(score >= 30);
    }

    private void checkBallHit(int x, int y) {
        for (int i = balls.size() - 1; i >= 0; i--) { // Перебор шаров
            Ball ball = balls.get(i);
            if (ball.contains(x, y)) { // Проверяем, попали ли в шарик
                balls.remove(i); // Удаляем шарик
                score++;
                updateNukeButton(); // Обновляем состояние кнопки при увеличении счета
                break; // Покидаем цикл, чтобы не удалять несколько шаров за один клик
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null); // Рисуем фон
        g.setColor(Color.GREEN);
        g.drawString("Счет: " + score, 10, 20); // Отображаем счет

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 60));
            g.drawString("Игра окончена!", getWidth() / 2 - 200, getHeight() / 2); // Центрирование текста
        } else {
            for (Ball ball : balls) {
                ball.draw(g); // Рисуем шарики
            }
        }

        // Рисуем кнопку "Ядерка"
        if (nukeButton.isVisible()) {
            nukeButton.setBounds(getWidth() / 2 - 75, getHeight() - 100, 150, 50);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            addNewBall();
            updateBallPositions();
            repaint();
        }
    }

    private void updateBallPositions() {
        for (Ball ball : balls) {
            ball.updatePosition(); // Обновляем позицию каждого шарика
            if (ball.getY() >= getHeight()) {
                gameOver = true; // Завершаем игру
                timer.stop(); // Останавливаем таймер
            }
        }
    }

    private void addNewBall() {
        if (balls.size() < MAX_BALLS) { // Проверяем, если число шариков меньше максимального
            Random random = new Random();
            int x = random.nextInt(getWidth() - Ball.DIAMETER);
            int imageIndex = random.nextInt(3);
            balls.add(new Ball(x, 0, imageIndex)); // Создаем шарик в верхней части экрана
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ловля шариков");
        BallCatchingGame game = new BallCatchingGame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(game);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }
}
