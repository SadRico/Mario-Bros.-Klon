package controller;

import java.util.Iterator;
import javax.swing.JOptionPane;

import model.Fireball;
import model.Level;
import model.Player;
import model.Enemy;
import model.Item;
import model.Block;
import model.Database;
import model.enums.PowerUpType;
import model.enums.BlockType;
import view.GamePanel;

public class GameController implements Runnable {
    private GamePanel view;
    private Level level;
    private Player player;
    private long startTime;
    private boolean running = true;
    private Database database;

    public GameController(GamePanel view) {
        this.view = view;
        this.database = new Database();
        initGame();
    }

    private void initGame() {
        level = new Level();
        player = new Player(100, 400);
        view.setup(level, player);
        startTime = System.currentTimeMillis();
    }


    @Override
    public void run() {
        final int fps = 60;
        final long frameTime = 1000 / fps;

        while (running) {
            long start = System.currentTimeMillis();

            update();

            long elapsedMillis = System.currentTimeMillis() - startTime;
            long elapsedSeconds = elapsedMillis / 1000;
            view.setElapsedTime(elapsedSeconds);

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

        handleBlockCollisions();
        handleItemCollisions();
        handleEnemyCollisions();
        handleGoalCollision();

        if (player.lives <= 0) {
            running = false;
            JOptionPane.showMessageDialog(view, "Game Over", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private void handleGoalCollision() {
        if (level.getGoal() != null && player.getBounds().intersects(level.getGoal().getBounds())) {
            running = false;
            String name = JOptionPane.showInputDialog(view, "Gib deinen Namen ein:", "Highscore speichern", JOptionPane.PLAIN_MESSAGE);
            if (name == null || name.isEmpty()) name = "Player";

            long elapsedTimeMillis = System.currentTimeMillis() - startTime;
            long elapsedSeconds = elapsedTimeMillis / 1000;

            try {
                database.saveScore(name, elapsedSeconds, player.score);
            } catch (Exception e) {
                e.printStackTrace();
            }

            JOptionPane.showMessageDialog(view, "Ziel erreicht! Score gespeichert.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleBlockCollisions() {
        boolean onAnyBlock = false;

        // Horizontale Bewegung und Kollision
        player.x += player.velocityX;
        for (Block block : level.getBlocks()) {
            if (player.getBounds().intersects(block.getBounds())) {
                if (player.velocityX > 0) {
                    player.x = block.x - player.width;
                } else if (player.velocityX < 0) {
                    player.x = block.x + block.width;
                }
                player.velocityX = 0;
            }
        }

        // Vertikale Bewegung und Kollision
        player.y += player.velocityY;
        for (Iterator<Block> it = level.getBlocks().iterator(); it.hasNext();) {
            Block block = it.next();
            if (player.getBounds().intersects(block.getBounds())) {
                if (player.velocityY > 0 && player.y + player.height - player.velocityY <= block.y) {
                    // Landet auf Block
                    player.y = block.y - player.height;
                    player.velocityY = 0;
                    onAnyBlock = true;
                } else if (player.velocityY < 0 && player.y <= block.y + block.height) {
                    // Stößt von unten gegen Block
                    player.y = block.y + block.height;
                    player.velocityY = 0;
                    if (block.type == BlockType.QUESTION) {
                        block.hit();
                    } else if (block.type == BlockType.BRICK) {
                        it.remove();
                    }
                }
            }
        }

        player.onGround = onAnyBlock;
    }

    private void handleItemCollisions() {
        Iterator<Item> iit = level.getItems().iterator();
        while (iit.hasNext()) {
            Item item = iit.next();
            if (player.getBounds().intersects(item.getBounds())) {
                switch (item.type) {
                    case COIN:
                        player.score += 200;
                        break;
                    case MUSHROOM:
                        if (player.powerUp == PowerUpType.NONE) {
                            player.powerUp = PowerUpType.SUPER;
                            player.height = 64;
                        }
                        player.score += 1000;
                        break;
                    case FIRE_FLOWER:
                        player.powerUp = PowerUpType.FIRE;
                        player.height = 64;
                        player.score += 1000;
                        break;
                }
                iit.remove();
            }
        }
    }

    private void handleEnemyCollisions() {
        Iterator<Enemy> eit = level.getEnemies().iterator();

        while (eit.hasNext()) {
            Enemy enemy = eit.next();

            // Prüft zuerst Fireball-Kollision
            for (Fireball f : player.fireballs) {
                if (!enemy.isDead() && f.getBounds().intersects(enemy.getBounds())) {
                    enemy.kill();
                    f.active = false;
                    player.score += 100;
                }
            }

            // Spieler-Kollision mit Gegner
            if (!enemy.isDead() && player.getBounds().intersects(enemy.getBounds())) {
                if (player.velocityY > 0) {
                    enemy.kill();
                    player.velocityY = -10;
                    player.score += 100;
                } else {
                    player.takeDamage();
                }
            }
        }

        // Entfernt Fireballs aus der Liste
        player.fireballs.removeIf(f -> !f.active);
    }
    public Player getPlayer() { return player; }
}

