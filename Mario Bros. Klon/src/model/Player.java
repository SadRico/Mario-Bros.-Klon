package model;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import model.enums.PowerUpType;

public class Player {
    public int x, y, width = 32, height = 32;
    public int velocityX, velocityY;
    public boolean onGround, moveLeft, moveRight, running;
    public int lives = 3, score = 0;
    public PowerUpType powerUp = PowerUpType.NONE;

    private static final int GRAVITY = 1;
    private static final int JUMP_STRENGTH = -20;  // erhöht Sprunghöhe
    private static final int WALK_SPEED = 4;
    private static final int RUN_SPEED = 7;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        int speed = running ? RUN_SPEED : WALK_SPEED;
        velocityX = moveLeft ? -speed : moveRight ? speed : 0;
        if (!onGround) velocityY += GRAVITY;
        x += velocityX;
        y += velocityY;
        if (y > 500) { y = 500; velocityY = 0; onGround = true; } else onGround = false;
        if (x < 0) x = 0;
    }

    public void jump() {
        if (onGround) { velocityY = JUMP_STRENGTH; onGround = false; }
    }

    public void takeDamage() { lives--; }

    public void shootFireball() { /* später implementieren */ }

    public Rectangle getBounds() { return new Rectangle(x, y, width, height); }

    public void draw(Graphics2D g) {
        switch (powerUp) {
            case NONE: g.setColor(java.awt.Color.RED); break;
            case SUPER: g.setColor(java.awt.Color.ORANGE); break;
            case FIRE: g.setColor(java.awt.Color.YELLOW); break;
        }
        g.fillRect(x, y, width, height);
        g.setColor(java.awt.Color.BLACK);
        g.fillOval(x + 8, y + 8, 4, 4);
        g.fillOval(x + 20, y + 8, 4, 4);
    }
}