package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import model.Player;
import view.GamePanel;

public class InputHandler extends KeyAdapter {
    private Player player;
    private GamePanel view;

    public InputHandler(Player player, GamePanel view) {
        this.player = player;
        this.view = view;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A: player.moveLeft = true; break;
            case KeyEvent.VK_D: player.moveRight = true; break;
            case KeyEvent.VK_W: player.jump(); break;
            case KeyEvent.VK_SHIFT: player.running = true; break;
            case KeyEvent.VK_SPACE:
                if (player.powerUp == model.enums.PowerUpType.FIRE) {
                    player.shootFireball();
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A: player.moveLeft = false; break;
            case KeyEvent.VK_D: player.moveRight = false; break;
            case KeyEvent.VK_SHIFT: player.running = false; break;
        }
    }
}