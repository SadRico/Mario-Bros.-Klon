package controller;

import java.util.Iterator;
import javax.swing.JOptionPane;

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
            try { Thread.sleep(Math.max(0, frameTime - delta)); }
            catch (InterruptedException ignored) {}
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
            long totalTime = System.currentTimeMillis(); // optional: Zeit stoppen
            try { database.saveScore(name, totalTime, player.score); }
            catch (Exception e) { e.printStackTrace(); }
            JOptionPane.showMessageDialog(view, "Ziel erreicht! Score gespeichert.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleBlockCollisions() {
        Iterator<Block> bit = level.getBlocks().iterator();
        while (bit.hasNext()) {
            Block block = bit.next();
            if (player.getBounds().intersects(block.getBounds())) {
                // oben landen
                if (player.velocityY > 0 && player.y + player.height - player.velocityY <= block.y) {
                    player.y = block.y - player.height;
                    player.velocityY = 0;
                    player.onGround = true;
                }
                // von unten gegen Block
                else if (player.velocityY < 0 && player.y <= block.y + block.height) {
                    player.y = block.y + block.height;
                    player.velocityY = 0;
                    if (block.type == BlockType.QUESTION) {
                        block.hit();
                       // level.spawnItem(block.x, block.y - block.height);
                    } else if (block.type == BlockType.BRICK) {
                        bit.remove();
                    }
                }
                // seitlich
                else {
                    if (player.x < block.x) player.x = block.x - player.width;
                    else player.x = block.x + block.width;
                }
            }
        }
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
    }

    private void checkLevelComplete() {
        if (player.x >= level.getWidth() - 100) {
            running = false;
            // Name-Eingabe und Highscore speichern
            String name = JOptionPane.showInputDialog(view, "Gib deinen Namen ein:", "Highscore speichern", JOptionPane.PLAIN_MESSAGE);
            if (name == null || name.isEmpty()) name = "Player";
            long totalTime = System.currentTimeMillis();
            try { database.saveScore(name, totalTime, player.score); }
            catch (Exception e) { e.printStackTrace(); }
            JOptionPane.showMessageDialog(view, "Level Complete! Score gespeichert.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void stop() { running = false; }
    public Player getPlayer() { return player; }
}