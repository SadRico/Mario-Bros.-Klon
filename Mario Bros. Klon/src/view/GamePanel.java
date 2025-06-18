package view;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import model.Level;
import model.Player;

public class GamePanel extends JPanel {
    private Level level;
    private Player player;
    private UIOverlay overlay;

    public GamePanel() {
        setPreferredSize(new java.awt.Dimension(800, 600));
        setDoubleBuffered(true);
        setFocusable(true);
        overlay = new UIOverlay();
    }

    public void setup(Level level, Player player) {
        this.level = level;
        this.player = player;
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (level != null && player != null) {
            level.draw(g2d);
            player.draw(g2d);
            overlay.draw(g2d, player);
        }
    }
}