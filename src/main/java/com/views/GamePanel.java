package views;

import controller.GameController;
import java.awt.*;
import java.util.Map;
import javax.swing.*;

import model.GameEngine;
import model.GameState;
import model.Position;
import model.TileType;

public class GamePanel extends JPanel {

    private GameState gameState;
    private GameController gameController;
    private TileType selectedTile;
    private JPanel tileSelectorPanel;
    private JPanel stopPanel;
    private JScrollPane scrollPane;
    private JButton doneButton;
    private JButton stopButton;
    private MapPanel mapPanel;
    private JButton selectedButton = null;
    private int tileSize = 32;
    private boolean is2d;

    private static final Color[] Colors = {
            Color.CYAN, Color.GREEN, Color.LIGHT_GRAY, Color.ORANGE,
            Color.BLUE, Color.RED, Color.BLACK
    };

    public GamePanel(GameState gameState, boolean is2d) {
        this.is2d = is2d;
        this.gameState = gameState;
        setLayout(new BorderLayout());

        mapPanel = new MapPanel(this, gameState);
        add(mapPanel, BorderLayout.CENTER);

        tileSelectorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        tileSelectorPanel.setBackground(Color.BLACK);

        stopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        stopPanel.setBackground(Color.BLACK);

        initButtons(is2d);

        scrollPane = new JScrollPane(tileSelectorPanel);
        scrollPane.setPreferredSize(new Dimension(800, 100));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.SOUTH);
        setDoubleBuffered(true);
    }

    private void initButtons(boolean is2d) {
        // Bouton Done
        doneButton = new JButton("Done");
        styleButton(doneButton, new Color(30, 30, 30));
        doneButton.addActionListener(e -> {
            if (is2d) {
                gameController.startGame();
                scrollPane.setViewportView(stopPanel);
                stopPanel.setVisible(true);
            } else {
                GameEngine gameEngine = new GameEngine(gameState);
                GameView3D gameView = new GameView3D(gameState.getCube(), gameState.getBoard(), gameEngine);
                new GameController(gameState, gameView, gameEngine);
            }
        });
        tileSelectorPanel.add(doneButton);

        // Bouton Stop
        stopButton = new JButton("Stop");
        styleButton(stopButton, new Color(120, 20, 20));
        stopButton.addActionListener(e -> {
            gameController.stopGame();
            resetToGameView();
        });
        stopPanel.add(stopButton);

        updateTileSelectorPanel(); // Initialisation des boutons de tuiles
    }

    private void updateTileSelectorPanel() {
        Map<TileType, Integer> tileStock = gameState.getBoard().getTileStock();

        for (Map.Entry<TileType, Integer> entry : tileStock.entrySet()) {
            TileType type = entry.getKey();
            int count = entry.getValue();

            if (type != TileType.EMPTY && type != TileType.START &&
                    type != TileType.FINISH && type != TileType.FORBIDDEN) {

                JButton tileButton = new JButton(type.getTileType() + " (" + count + ")");
                tileButton.setFocusable(false);
                tileButton.setBackground(colorChoice(type));
                tileButton.setForeground(Color.WHITE);
                setIcone(tileButton, type);

                tileButton.addActionListener(e -> {
                    selectedTile = type;
                    if (selectedButton != null) {
                        selectedButton.setBorder(UIManager.getBorder("Button.border"));
                    }
                    selectedButton = tileButton;
                    selectedButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                });

                tileSelectorPanel.add(tileButton);
            }
        }

        tileSelectorPanel.revalidate();
        tileSelectorPanel.repaint();
    }

    public void updateTileButtons() {
        tileSelectorPanel.removeAll();
        tileSelectorPanel.add(doneButton);
        updateTileSelectorPanel();
    }

    public void showWinPanel() {
        removeAll();
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel message = new JLabel("Gagné !");
        message.setHorizontalAlignment(SwingConstants.CENTER);
        message.setFont(new Font("Arial", Font.BOLD, 32));
        message.setForeground(Color.WHITE);
        add(message, BorderLayout.CENTER);

        JButton backToMenu = new JButton("Niveaux");
        backToMenu.addActionListener(e -> {
            gameController.backToLevelMenu();
            SwingUtilities.getWindowAncestor(this).dispose();
        });
        add(backToMenu, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    public void showLosePanel() {
        removeAll();
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel message = new JLabel("Perdu !");
        message.setHorizontalAlignment(SwingConstants.CENTER);
        message.setFont(new Font("Arial", Font.BOLD, 32));
        message.setForeground(Color.RED);
        add(message, BorderLayout.CENTER);

        revalidate();
        repaint();

        Timer timer = new Timer(2000, e -> resetToGameView());
        timer.setRepeats(false);
        timer.start();
    }

    public void resetToGameView() {
        gameState.getBoard().getCube().setPosition(new Position(
                (int) gameState.getBoard().getTileStart().getX(),
                (int) gameState.getBoard().getTileStart().getY()));

        removeAll();
        setLayout(new BorderLayout());
        add(mapPanel, BorderLayout.CENTER);

        // Remettre le panneau du bas à l'état initial
        tileSelectorPanel.removeAll();
        tileSelectorPanel.add(doneButton);
        updateTileSelectorPanel();

        scrollPane.setViewportView(tileSelectorPanel);
        add(scrollPane, BorderLayout.SOUTH);

        revalidate();
        repaint();
        mapPanel.repaint();
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFocusable(false);
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setPreferredSize(new Dimension(100, 40));
    }

    private void setIcone(JButton button, TileType type) {
        try {
            String path = "assets\\imgs\\imgs2D\\" + type.getTileType() + ".png";
            ImageIcon icon = new ImageIcon(path);
            button.setIcon(icon);
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'icône pour " + type.getTileType());
        }
    }

    public void setTileSize(int newSize) {
        tileSize = newSize;
        mapPanel.setTileSize(newSize);
        mapPanel.revalidate();
        mapPanel.repaint();
    }

    public static Color colorChoice(TileType type) {
        return Colors[type.ordinal() % Colors.length];
    }

    public int getTileSize() {
        return tileSize;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public GameController getGameController() {
        return this.gameController;
    }

    public TileType getSelectedTile() {
        return selectedTile;
    }

    public MapPanel getMapPanel() {
        return this.mapPanel;
    }
}
