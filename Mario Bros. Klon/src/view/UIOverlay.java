package view;

import java.awt.Graphics2D;
import model.Player;

public class UIOverlay {
    public void draw(Graphics2D g, Player player, long elapsedSeconds) {
        g.setColor(java.awt.Color.WHITE);
        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        g.drawString("Leben: " + player.lives, 10, 30);
        g.drawString("Punkte: " + player.score, 10, 50);

        long minutes = elapsedSeconds / 60;
        long seconds = elapsedSeconds % 60;
        String timeStr = String.format("Zeit: %2d", seconds);
        g.drawString(timeStr, 10, 70);
    }
}