package views;

import controller.GameController;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import model.GameState;
import model.JSONGenerator;
import model.TileType;

public class LevelEditorPanel extends JPanel {

    private GameState gameState;
    private GameController gameController;
    private TileType selectedTile;

    private JPanel buttonPanel;
    private JScrollPane scrollPane;

    private LevelEditorMapPanel levelEditorMapPanel;
    private JButton selectedButton = null;
    int tileSize = 32;

    public LevelEditorPanel(GameState gameState) {
        this.gameState = gameState;
        setLayout(new BorderLayout());

        levelEditorMapPanel = new LevelEditorMapPanel(this, gameState);
        add(levelEditorMapPanel, BorderLayout.CENTER);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(Color.DARK_GRAY);

        createTileButtons();

        scrollPane = new JScrollPane(buttonPanel);
        scrollPane.setPreferredSize(new Dimension(800, 100));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.SOUTH);

        setDoubleBuffered(true);
    }

    private void createTileButtons() {
        buttonPanel.removeAll(); // Nettoyer les anciens boutons
        JButton doneButton = new JButton("Done");
        doneButton.setFocusable(false);
        doneButton.setForeground(Color.WHITE);
        doneButton.setBackground(Color.BLACK);
        doneButton.addActionListener(e -> {
            levelEditorMapPanel.setShowCube(true); // Affiche le cube
            gameController.startGame();            // Démarre le jeu
        });
        buttonPanel.add(doneButton);

        JButton createButton = new JButton("Create");
        createButton.setFocusable(false);
        createButton.setForeground(Color.WHITE);
        createButton.setBackground(new Color(0, 128, 0));
        createButton.addActionListener(e -> createLevel());
        buttonPanel.add(createButton);

        Map<TileType, Integer> tileStock = gameState.getBoard().getTileStock();

        for (TileType type : TileType.values()) {
            int count = tileStock.getOrDefault(type, 0);

            JButton tileButton = new JButton(type.getTileType() + " (" + count + ")");
            tileButton.setFocusable(false);
            tileButton.setBackground(colorChoice(type));
            tileButton.setForeground(Color.WHITE);
            setIcone(tileButton, type);

            tileButton.addActionListener(e -> {
                this.selectedTile = type;
                if (selectedButton != null) {
                    selectedButton.setBorder(UIManager.getBorder("Button.border"));
                }
                selectedButton = tileButton;
                selectedButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            });

            buttonPanel.add(tileButton);
        }

        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private void createLevel() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Voulez-vous créez le niveau ?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            cleanBoard();
            JSONGenerator.generateLevel("assets\\json", gameState.getBoard());
        }
    }

    private void cleanBoard(){
        gameState.getBoard().cleanBoard();
    }

    public void updateTileButtons() {
        createTileButtons(); 
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
        this.tileSize = newSize;
        levelEditorMapPanel.setTileSize(newSize);
        levelEditorMapPanel.revalidate();
        levelEditorMapPanel.repaint();
    }

    public static Color colorChoice(TileType type) {
        switch (type) {
            case EMPTY: return Color.LIGHT_GRAY;
            case START: return Color.GREEN;
            case FINISH: return Color.CYAN;
            case ARROW: return Color.ORANGE;
            case SLIDE: return Color.BLUE;
            case FORBIDDEN: return Color.BLACK;
            case BUTTON: return Color.RED;
            default: return Color.DARK_GRAY;
        }
    }

    public int getTileSize() {
        return tileSize;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public TileType getSelectedTile() {
        return selectedTile;
    }

    public LevelEditorMapPanel getEditorMapPanel(){
        return levelEditorMapPanel;
    }

}


