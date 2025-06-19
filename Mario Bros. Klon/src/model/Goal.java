package model;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Rectangle;

public class Goal {
    private int x, y, width, height;

    int flagHeight = 360; // Höhe der Fahnenstange
    int groundY = 532;

    public Goal(int x) {
        this.x = x;
        this.width = 20;
        this.height = flagHeight;
        this.y = groundY - flagHeight; // Fahnenstange steht auf dem Boden
    }


    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D g) {
        // Fahnenstange zeichnen
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);

        // Fahne oben an der Stange
        g.setColor(Color.RED);
        g.fillRect(x + width, y, 30, 20);
    }
}
