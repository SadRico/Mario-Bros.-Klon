package model;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import model.enums.EnemyType;
import java.awt.Color;
import java.util.List;


public class Enemy {
    public int x, y, width = 32, height = 32;
    private int velocityX;
    public EnemyType type;
    private boolean dead;
    private int direction = -1;

    public Enemy(int x, int y, EnemyType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.velocityX = direction * 2;
    }


    public void update(List<Block> blocks) {
        if (!dead) {
            x += velocityX;

            // Kollision mit Block prüfen
            for (Block block : blocks) {
                if (this.getBounds().intersects(block.getBounds())) {
                    // Rückgängig machen
                    x -= velocityX;

                    // Richtung umkehren
                    direction *= -1;
                    velocityX = direction * 2;
                    break;
                }
            }
            if (x < 0 || x > 3200 - width) {
                direction *= -1;
                velocityX = direction * 2;
            }
        }
    }

    public void kill() { dead = true; }
    public boolean isDead() { return dead; }
    public Rectangle getBounds() { return new Rectangle(x, y, width, height); }

    public void draw(Graphics2D g) {
        switch (type) {
            case GOOMBA: g.setColor(new Color(139,69,19)); break;
            case KOOPA: g.setColor(Color.GREEN); break;
        }
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.fillOval(x+6, y+8, 6, 6);
        g.fillOval(x+20, y+8, 6, 6);
        g.drawArc(x+10, y+16, 12, 8, 0, -180);
    }
}