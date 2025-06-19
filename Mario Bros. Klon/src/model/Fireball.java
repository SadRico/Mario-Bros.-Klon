package model;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Fireball {
    public int x, y, width = 12, height = 12;
    public int velocityX;
    public boolean active = true;

    public Fireball(int startX, int startY, boolean facingRight) {
        this.x = startX;
        this.y = startY;
        this.velocityX = facingRight ? 10 : -10;
    }

    public void update() {
        x += velocityX;
        // Bildschirmgrenze prüfen
        if (x < 0 || x > 3200) active = false;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.ORANGE);
        g.fillOval(x, y, width, height);
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(x, y, width, height);
    }
}
