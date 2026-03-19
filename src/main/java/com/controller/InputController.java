package controller;

import java.awt.event.MouseEvent;

import model.*;
import views.*;

public class InputController {
    // Models
    private GameState gameState;
    private GameEngine gameEngine;

    // Views
    private GameView3D gameView3D;
    private GameView gameView;
    private GamePanel gamePanel;
    private LevelEditorPanel levelEditorPanel;

    // Controllers
    private MouseHandler mouseHandler;
    private MouseHandler mouseHandler2;

    public InputController(GameState gameState, GameView gameView, GameEngine gameEngine, GamePanel gamePanel) {
        this.gameState = gameState;
        this.gameEngine = gameEngine;
        this.gameView = gameView;
        this.gamePanel = gamePanel;

        // événements de souris
        mouseHandler = new MouseHandler(this);
        gameView.addMouseListener(mouseHandler);
        gamePanel.addMouseListener(mouseHandler);
        gamePanel.getMapPanel().addMouseListener(mouseHandler);
    }

    public InputController(GameState gameState, GameView gameView, GameEngine gameEngine, LevelEditorPanel gamePanel) {
        this.gameState = gameState;
        this.gameEngine = gameEngine;
        this.gameView = gameView;
        this.levelEditorPanel = gamePanel;

        // événements de souris
        mouseHandler2 = new MouseHandler(this);
        gameView.addMouseListener(mouseHandler);
        levelEditorPanel.addMouseListener(mouseHandler);
        levelEditorPanel.getEditorMapPanel().addMouseListener(mouseHandler2);
    }

    public InputController(GameState gameState, GameView3D gameView) {
        this.gameState = gameState;
        this.gameView3D = gameView;

        // événements de souris
        mouseHandler = new MouseHandler(this);
    }

    // gere le click de la souris
    public void handleMouseClick(MouseEvent e) {
        Object source = e.getSource();
        Board board = gameState.getBoard();
        if (board == null)
            return;

        int tileSize;
        int col, row;

        // Si clic sur GamePanel
        if (gamePanel != null && gamePanel.getMapPanel() != null && source == gamePanel.getMapPanel()) {
            tileSize = gamePanel.getTileSize();
            col = e.getX() / tileSize;
            row = e.getY() / tileSize;

            if (col >= board.getSize() || row >= board.getSize() || col < 0 || row < 0) {
                System.err.println("coordonnées invalides");
                return;
            }

            Cellule cellule = board.getTile(col, row);
            if (cellule == null)
                return;

            TileType currentTile = cellule.getTile();
            if (gamePanel.getMapPanel().isRemovableTile(currentTile)) {
                gamePanel.getMapPanel().removeTileFromBoard(board, col, row);
            } else if (currentTile == TileType.EMPTY) {
                gamePanel.getMapPanel().placeTileOnBoard(board, col, row);
            }

            // Si clic sur LevelEditorPanel
        } else if (levelEditorPanel != null && levelEditorPanel.getEditorMapPanel() != null &&
                source == levelEditorPanel.getEditorMapPanel()) {

            tileSize = levelEditorPanel.getTileSize();
            col = e.getX() / tileSize;
            row = e.getY() / tileSize;

            Board editorBoard = levelEditorPanel.getEditorMapPanel().getGameState().getBoard();
            if (editorBoard == null || col >= editorBoard.getSize() || row >= editorBoard.getSize()
                    || col < 0 || row < 0)
                return;

            if (e.getButton() == MouseEvent.BUTTON1) {
                levelEditorPanel.getEditorMapPanel().gererClicGauche(col, row);
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                levelEditorPanel.getEditorMapPanel().gererClicDroit(col, row);
            }

        } else {
            System.err.println("Click ignoré : source inconnue");
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    public void setGameEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public GameView3D getGameView3D() {
        return gameView3D;
    }

    public void setGameView3D(GameView3D gameView3D) {
        this.gameView3D = gameView3D;
    }

    public GameView getGameView() {
        return gameView;
    }

    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public MouseHandler getMouseHandler() {
        return mouseHandler;
    }

    public void setMouseHandler(MouseHandler mouseHandler) {
        this.mouseHandler = mouseHandler;
    }

}
