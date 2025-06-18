package controller;

import java.util.Iterator;

import model.Level;
import model.Player;
import view.GamePanel;

public class GameController implements Runnable {
    private GamePanel view;
    private Level level;
    private Player player;
    private boolean running = true;
    private model.Database database;  // Datenbank hinzufügen

    public GameController(GamePanel view) {
        this.view = view;
        this.database = new model.Database();  // Datenbank initialisieren
        initGame();
    }

    private void initGame() {
        level = new Level();
        player = new Player(100, 400);
        view.setup(level, player);
    }

    @Override
    public void run() {
        final int fps = 60;
        final long frameTime = 1000 / fps;

        while (running) {
            long start = System.currentTimeMillis();
            update();
            view.repaint();
            long delta = System.currentTimeMillis() - start;
            try {
                Thread.sleep(Math.max(0, frameTime - delta));
            } catch (InterruptedException ignored) {}
        }
    }

    private void update() {
        player.update();
        level.update();
        handleEnemyCollisions();  // neue Kollisionserkennung
        checkLevelComplete();     // neues Levelende
        if (player.lives <= 0) {
            running = false;
            System.out.println("Game Over");
        }
    }

    private void handleEnemyCollisions() {
        Iterator<model.Enemy> it = level.getEnemies().iterator();
        while (it.hasNext()) {
            model.Enemy enemy = it.next();
            if (!enemy.isDead() && player.getBounds().intersects(enemy.getBounds())) {
                if (player.velocityY > 0) {
                    enemy.kill();
                    player.velocityY = -10;  // Rückstoß
                    player.score += 100;
                } else {
                    player.takeDamage();
                }
            }
        }
    }

    private void checkLevelComplete() {
        if (player.x >= level.getWidth() - 100) {
            running = false;
            long totalTime = System.currentTimeMillis();
            try {
                database.saveScore("Player", totalTime, player.score);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Level Complete! Score gespeichert.");
        }
    }

    public void stop() {
        running = false;
    }

    // Getter für InputHandler
    public Player getPlayer() {
        return player;
    }
}