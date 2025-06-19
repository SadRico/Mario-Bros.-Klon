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

    public GamePanel() {
        setPreferredSize(new java.awt.Dimension(600, 560));
        setDoubleBuffered(true);
        setFocusable(true);
        overlay = new UIOverlay();
    }
    private int cameraX = 0;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (level != null && player != null) {
            Graphics2D g2d = (Graphics2D) g;

            // Kamera-Position berechnen
            cameraX = player.x - getWidth() / 2 + player.width / 2;
            if (cameraX < 0) cameraX = 0;
            if (cameraX > level.getWidth() - getWidth()) {
                cameraX = level.getWidth() - getWidth();
            }

            // Alle Fireballs zeichnen
            for (Fireball f : player.fireballs) {
                f.draw(g2d);
            }

            // Welt mit Kamera verschieben
            g2d.translate(-cameraX, 0);
            level.draw(g2d);
            player.draw(g2d);

            // Kamera zurücksetzen, um UI fix zu zeichnen
            g2d.translate(cameraX, 0);
            overlay.draw(g2d, player);
        }
    }

    public void setup(Level level, Player player) {
        this.level = level;
        this.player = player;
        requestFocusInWindow();
    }}