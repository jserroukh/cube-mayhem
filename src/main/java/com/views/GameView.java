package views;

import javax.swing.*;

import model.GameState;

public class GameView extends JFrame {
    private GamePanel gamePanel;
    private LevelEditorPanel levelEditorPanel;
    private boolean is2d;

    public GameView(GameState gameState,boolean is2d) {
        this.is2d=is2d;
        gamePanel = new GamePanel(gameState,is2d);
        setTitle("Cube Mayhem");
        setResizable(false); // ne peut pas être redimensionné
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(gamePanel);
        pack(); // obligatoire pour que ca marche avec le preffered size de GamePanel
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public GameView(GameState gameState,int i) {
        levelEditorPanel = new LevelEditorPanel(gameState);
        setTitle("Cube Mayhem");
        setResizable(false); // ne peut pas être redimensionné
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(levelEditorPanel);
        pack(); // obligatoire pour que ca marche avec le preffered size de GamePanel
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public GamePanel getGamePanel() { return gamePanel; }
    public LevelEditorPanel getLevelEditorPanel(){ return levelEditorPanel;}
    public boolean isIs2d() {
        return is2d;
    }
}