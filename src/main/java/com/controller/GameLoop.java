package controller;

import javax.swing.SwingUtilities;

import model.Direction;
import model.GameEngine;
import model.TileType;
import views.GamePanel;

public class GameLoop implements Runnable {
    Thread gamethread;
    GamePanel gamePanel;
    GameEngine gameEngine;
    private boolean running;

    public GameLoop(GamePanel gamePanel, GameEngine gameEngine) {
        this.gamePanel = gamePanel;
        this.gameEngine = gameEngine;
        running = false; // l'etat du jeux
    }

    public void start() {
        if (running) {
            return;
        } // ne pas relancer une boucle si deja lancer
        running = true;
        gamethread = new Thread(this);
        gamethread.start();
    }

    @Override
    public void run() {
        while (running) {
            boolean lost = gameEngine.isLost();
            boolean won = gameEngine.isWin();

            System.out.println("lost=" + lost + " won=" + won);
            if (won) {
                SwingUtilities.invokeLater(() -> gamePanel.showWinPanel());// mieux pour ui
                stop();
                break;
            } else if (lost) {
                SwingUtilities.invokeLater(() -> gamePanel.showLosePanel());
                stop();
                break;
            }

            gameEngine.update();
            gamePanel.repaint();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        running = false;
        if (Thread.currentThread() != gamethread) {
            try {
                gamethread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
