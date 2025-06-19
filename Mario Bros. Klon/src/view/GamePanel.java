package view;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import model.Level;
import model.Player;
import model.Fireball;

public class GamePanel extends JPanel {
    private Level level;
    private Player player;
    private UIOverlay overlay;
    private long elapsedTime = 0;

    public void setElapsedTime(long seconds) {
        this.elapsedTime = seconds;
        repaint();
    }

    public GamePanel() {
        setPreferredSize(new java.awt.Dimension(850, 560));
        setDoubleBuffered(true);
        setFocusable(true);
        overlay = new UIOverlay();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (level != null && player != null) {
            Graphics2D g2d = (Graphics2D) g;

            // Kamera-Position berechnen
            int cameraX = player.x - getWidth() / 2 + player.width / 2;
            cameraX = Math.max(0, Math.min(cameraX, level.getWidth() - getWidth()));

            // Fireballs zeichnen
            for (Fireball f : player.fireballs) {
                f.draw(g2d);
            }

            g2d.translate(-cameraX, 0);
            level.draw(g2d);
            player.draw(g2d);
            g2d.translate(cameraX, 0);

            // UI mit verstrichener Zeit zeichnen
            if (overlay != null) {
                overlay.draw(g2d, player, elapsedTime);
            }
        }
    }

    public void setup(Level level, Player player) {
        this.level = level;
        this.player = player;
        requestFocusInWindow();
    }
}