
import java.util.HashMap;

import javax.swing.JFrame;

import controller.GameController;
import controller.GameLoop;
import model.Board;
import model.Cube;
import model.GameEngine;
import model.GameState;
import model.JSONParseur;
import views.*;


public class Main {

    public static void main(String[] args) {
        Board board = JSONParseur.parseBoard("assets\\json\\level.json");
        if (board != null) {
            System.out.println("Board chargé !");
            System.out.println("Cube position: " + board.getCube().getX() + ", " + board.getCube().getY());
        }
        GameState gameState = new GameState(board); // etat du jeu
        MainMenuPanel menuPanel = new MainMenuPanel(gameState);
    }

}
