package model;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import model.enums.ItemType;
import java.awt.Color;

public class Item {
    public int x, y, width = 24, height = 24;
    public ItemType type;

    public Item(int x, int y, ItemType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public Rectangle getBounds() { return new Rectangle(x, y, width, height); }

    public void draw(Graphics2D g) {
        switch (type) {
            case COIN:
                g.setColor(Color.YELLOW);
                g.fillOval(x, y, width, height);
                g.setColor(Color.ORANGE);
                g.drawOval(x, y, width, height);
                break;
            case MUSHROOM:
                g.setColor(Color.RED);
                g.fillOval(x, y+8, width, height-8);
                g.setColor(Color.WHITE);
                g.fillOval(x+4, y+4, 6, 6);
                g.fillOval(x+14, y+4, 6, 6);
                break;
            case FIRE_FLOWER:
                g.setColor(Color.ORANGE);
                g.fillOval(x+8, y, 8, 8);
                g.setColor(Color.GREEN);
                g.fillRect(x+10, y+8, 4, 16);
                break;
        }
    }
}