package model;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import model.enums.BlockType;
import java.awt.Color;
import java.awt.Font;

public class Block {
    public int x, y, width = 32, height = 32;
    public BlockType type;
    private boolean hit;

    public Block(int x, int y, BlockType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void hit() { if (type == BlockType.QUESTION && !hit) hit = true; }

    public Rectangle getBounds() { return new Rectangle(x, y, width, height); }

    public void draw(Graphics2D g) {
        switch (type) {
            case GROUND: g.setColor(new Color(139,69,19)); break;
            case BRICK: g.setColor(new Color(205,133,63)); break;
            case QUESTION: g.setColor(hit ? new Color(139,69,19) : Color.YELLOW); break;
            case PIPE: g.setColor(Color.GREEN); break;
        }
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        if (type == BlockType.QUESTION && !hit) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("?", x+10, y+22);
        }
    }
}