package model;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import model.enums.PowerUpType;

public class Player {
    public int x, y, width = 32, height = 32;
    public int velocityX, velocityY;
    public boolean onGround, moveLeft, moveRight, running;
    private boolean jumpPressed = false;
    public int lives = 3, score = 0;
    public List<Fireball> fireballs = new ArrayList<>();
    public boolean facingRight = true;
    public PowerUpType powerUp = PowerUpType.NONE;

    private static final int GRAVITY = 1;
    private static final int JUMP_STRENGTH = -20;  // erhöht Sprunghöhe
    private static final int WALK_SPEED = 4;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        updateFireballs();  // Fireballs aktualisieren

        int speed = WALK_SPEED;
        velocityX = moveLeft ? -speed : moveRight ? speed : 0;
        velocityY += GRAVITY;

        if (x < 0) x = 0;

        // Richtung merken
        if (moveLeft) facingRight = false;
        if (moveRight) facingRight = true;
    }

    public void updateFireballs() {
        Iterator<Fireball> it = fireballs.iterator();
        while (it.hasNext()) {
            Fireball f = it.next();
            f.update();
            if (!f.active) {
                it.remove();
            }
        }
    }

    public void jump() {
        if (onGround) {
            velocityY = JUMP_STRENGTH;
            onGround = false;
        }
    }


    // In model.Player
    public void stopJump() {
        jumpPressed = false;
        if (velocityY < -6) velocityY = -6; // Für variablen Sprung
    }


    public void takeDamage() {
        if (powerUp != PowerUpType.NONE) {
            // Power-Up verlieren und wieder klein werden
            powerUp = PowerUpType.NONE;
            height = 32;
            y += 32; // Höhe anpassen, damit er nicht im Block steckt
        } else {
            // Leben verlieren und zurück zum Start
            lives--;
            if (lives > 0) {
                resetPosition(); // Zurücksetzen wenn noch Leben
            }
        }
    }

    public void resetPosition() {
        x = 100;
        y = 400;
        velocityX = 0;
        velocityY = 0;
        moveLeft = false;
        moveRight = false;
        running = false;
        onGround = false;
    }

    public void shootFireball() {
        if (powerUp == PowerUpType.FIRE) {
            fireballs.add(new Fireball(x + width / 2, y + height / 2, facingRight));
        }
    }

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