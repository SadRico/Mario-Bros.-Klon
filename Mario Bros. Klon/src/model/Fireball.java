package model;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;

public class Fireball {
    public int x, y, width = 16, height = 16;
    public int velocityX, velocityY;
    public boolean active;

    public Fireball(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.velocityX = direction * 8;
        this.velocityY = -2;
        this.active = true;
    }

    public void update() {
        if (active) {
            x += velocityX;
            y += velocityY;
            velocityY += 1;
            if (x < 0 || x > 3200 || y > 600) active = false;
        }
    }

    public Rectangle getBounds() { return new Rectangle(x, y, width, height); }
    public void draw(Graphics2D g) {
        if (active) {
            g.setColor(Color.ORANGE);
            g.fillOval(x, y, width, height);
            g.setColor(Color.RED);
            g.fillOval(x+4, y+4, width-8, height-8);
        }
    }
}