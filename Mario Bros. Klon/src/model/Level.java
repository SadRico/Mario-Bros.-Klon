package model;

import java.awt.Graphics2D;
import java.awt.Color;
import java.util.*;
import model.enums.BlockType;

public class Level {
    private List<Block> blocks;
    private List<Enemy> enemies;
    private List<Item> items;
    private Goal goal;
    private int width;

    public Level() {
        blocks = new ArrayList<>();
        enemies = new ArrayList<>();
        items = new ArrayList<>();
        width = 3200;
        generateLevel();
    }

    private void generateLevel() {
        int levelHeight = 560;
        int goalX = width - 100;

        // Boden durchgehend
        for (int i = 0; i < width; i += 32) {
            blocks.add(new Block(i, 532, BlockType.GROUND));
        }

        // Früher Bereich: einfache Blöcke
        for (int i = 300; i < 500; i += 32) {
            blocks.add(new Block(i, 400, BlockType.BRICK));
        }

        blocks.add(new Block(350, 300, BlockType.QUESTION));
        blocks.add(new Block(450, 300, BlockType.QUESTION));

        // Kleine Röhre
        for (int i = 0; i < 3; i++) {
            blocks.add(new Block(800, 532 - i * 32, BlockType.PIPE));
            blocks.add(new Block(832, 532 - i * 32, BlockType.PIPE));
        }

        // Mittelbereich mit Plattformen
        for (int i = 1000; i < 1200; i += 32) {
            blocks.add(new Block(i, 350, BlockType.BRICK));
        }

        // Plattform-Stufen
        for (int step = 0; step < 8; step++) {
            for (int j = 0; j <= step; j++) {
                blocks.add(new Block(width - 600 + step * 32, 532 - j * 32, BlockType.BRICK));
            }
        }

        //  flache Plattform
        for (int i = 2000; i < 2400; i += 32) {
            blocks.add(new Block(i, 400, BlockType.BRICK));
        }

        // Gegner verteilen
        enemies.add(new Enemy(400, 500, model.enums.EnemyType.GOOMBA));
        enemies.add(new Enemy(600, 500, model.enums.EnemyType.GOOMBA));
        enemies.add(new Enemy(1100, 318, model.enums.EnemyType.GOOMBA));
        enemies.add(new Enemy(1500, 500, model.enums.EnemyType.KOOPA));
        enemies.add(new Enemy(1900, 500, model.enums.EnemyType.KOOPA));
        enemies.add(new Enemy(2300, 368, model.enums.EnemyType.GOOMBA));

        // Items
        items.add(new Item(200, 500, model.enums.ItemType.COIN));
        items.add(new Item(250, 500, model.enums.ItemType.COIN));
        items.add(new Item(1050, 318, model.enums.ItemType.COIN));
        items.add(new Item(1450, 418, model.enums.ItemType.MUSHROOM));
        items.add(new Item(2100, 368, model.enums.ItemType.FIRE_FLOWER));
        items.add(new Item(2600, 500, model.enums.ItemType.COIN));

        // Ziel am Ende
        goal = new Goal(goalX);
    }

    public void update() {
        enemies.forEach(Enemy::update);
    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(135, 206, 235));
        g.fillRect(0, 0, width, 600);
        g.setColor(Color.WHITE);
        for (int i = 0; i < width; i += 200) {
            g.fillOval(i, 100, 60, 30);
            g.fillOval(i + 20, 90, 40, 25);
            g.fillOval(i + 40, 100, 50, 30);
        }
        blocks.forEach(b -> b.draw(g));
        enemies.stream().filter(e -> !e.isDead()).forEach(e -> e.draw(g));
        items.forEach(i -> i.draw(g));

        // Ziel rendern
        if (goal != null) {
            goal.draw(g);
        }
    }
    public List<Block> getBlocks() {
        return blocks;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Item> getItems() {
        return items;
    }

    public int getWidth() {
        return width;
    }

    public Goal getGoal() {
        return goal;
    }

}