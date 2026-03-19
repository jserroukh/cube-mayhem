package controller;
import model.Board;
import model.Cube;
import model.Direction;
import model.GameEngine;
import model.GameState;
import model.TileType;
import views.GamePanel;
import views.GameView;
import views.MainMenuPanel;
import views.GameView3D;
import views.LevelEditorPanel;
import views.LevelsPanel2D;

import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;


public class GameController {

    // Models
    private GameState gameState;
    private GameEngine gameEngine;

    // Views
    private GameView3D gameView3D;
    private GameView gameView;
    private GamePanel gamePanel;
    private LevelEditorPanel levelEditorPanel;
    // Controllers
    private GameLoop gameLoop;
    private InputController inputController;
    private Cube posCubeinit;//position initial du cube dans un level donne
    
    public GameController(GameState gameState, GameView gameView, GameEngine gameEngine , GamePanel gamePanel) {
        this.gameState = gameState;
        posCubeinit = gameState.getCube();
        this.gameView = gameView;
        this.gameEngine = gameEngine;
        this.gamePanel = gamePanel;
        
        // Controller auquel on délègue la gestion des évènements claviers/souris
        inputController = new InputController(gameState, gameView , gameEngine, gamePanel);
        gamePanel.setGameController(this);
        this.gameLoop = new GameLoop(gamePanel,gameEngine);
    }
    public GameController(GameState gameState, GameView3D gameView, GameEngine gameEngine) {
        this.gameState = gameState;
        this.gameView3D = gameView;
        this.gameEngine = gameEngine;
        
        // Controller auquel on délègue la gestion des évènements claviers/souris
        inputController = new InputController(gameState, gameView3D);
    }
    public GameController(GameState gameState, GameView gameView, GameEngine gameEngine , LevelEditorPanel levelEditorPanel) {
        this.gameState = gameState;
        posCubeinit = gameState.getCube();
        this.gameView = gameView;
        this.gameEngine = gameEngine;
        this.levelEditorPanel = levelEditorPanel;
        
        // Controller auquel on délègue la gestion des évènements claviers/souris
        inputController = new InputController(gameState, gameView , gameEngine, levelEditorPanel);
        levelEditorPanel.setGameController(this);
        this.gameLoop = new GameLoop(gamePanel,gameEngine);
    }
    // public void gameWined(){
    //     if(gameEngine.isWinned()){
            
    //     }
    // }
    // public void gameLosed(){
    //     if(gameEngine.isLosed()){
           
    //     }
    // }

    public void startGame(){
        gameLoop.start();
    }

    public void stopGame(){
        gameLoop.stop();
    }

    public void backToLevelMenu() {//utilise pour la win
        // nv panneau select niveaux
        SwingUtilities.invokeLater(() -> new LevelsPanel2D(gameView.isIs2d()));
    }
}   

  
