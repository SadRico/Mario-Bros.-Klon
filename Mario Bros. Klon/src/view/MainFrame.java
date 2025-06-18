package view;

import javax.swing.JFrame;
import controller.GameController;
import controller.InputHandler;

public class MainFrame extends JFrame {
    private GamePanel panel;
    private GameController controller;

    public MainFrame() {
        setTitle("Super Mario Klon");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        panel = new GamePanel();
        add(panel);
        pack();
        setLocationRelativeTo(null);
        controller = new GameController(panel);
        // KeyListener mit Player aus Controller registrieren
        panel.addKeyListener(new InputHandler(controller.getPlayer(), panel));
        new Thread(controller).start();
    }
}