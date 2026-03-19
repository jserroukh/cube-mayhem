package views;

import javax.swing.*;
import controller.GameController;
import model.GameState;
import model.JSONParseur;
import model.Board;
import model.GameEngine;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenuPanel extends JPanel {
    private GameState gameState;
    private static LevelsPanel2D levels;
    private JFrame frame;
    private JButton startButton, start2DButton, levelEditor2DButton, optionsButton, quitButton;

    public MainMenuPanel(GameState gameState) {
        this.gameState = gameState;
        initializeFrame();
        setupPanel();
        addComponents();
        frame.add(this);
        frame.setVisible(true);
    }

    private void initializeFrame() {
        frame = new JFrame("Cube Mayhem - Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
    }

    private void setupPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); 
        setBackground(Color.BLACK);
    }

    private void addComponents() {
        add(Box.createVerticalStrut(40)); 
        add(createTitlePanel());
        add(Box.createVerticalStrut(30));
        createButtons();
        addButtonsToPanel();
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.BLACK);

        JLabel title = createLabel("Cube Mayhem", Color.WHITE, new Font("SansSerif", Font.BOLD, 32));
        JLabel subtitle = createLabel("(The mayhem is in your brain)", Color.GRAY, new Font("SansSerif", Font.ITALIC, 12));

        titlePanel.add(title);
        titlePanel.add(subtitle);
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        return titlePanel;
    }

    private JLabel createLabel(String text, Color color, Font font) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(font);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void createButtons() {
        startButton = createTextButton("start", e -> initListOfLevels(false));
        start2DButton = createTextButton("start 2D", e -> start2DGame());
        levelEditor2DButton = createTextButton("Level Editor 2D", e -> startLevelEditor2D()); // <-- NOUVEAU BOUTON
        optionsButton = createTextButton("options", e -> {}); 
        quitButton = createTextButton("quit", e -> System.exit(0));
    }

    private void addButtonsToPanel() {
        add(startButton);
        add(Box.createVerticalStrut(10));
        add(start2DButton);
        add(Box.createVerticalStrut(10));
        add(levelEditor2DButton); // <-- AJOUT du bouton éditeur ici
        add(Box.createVerticalStrut(10));
        add(optionsButton);
        add(Box.createVerticalStrut(10));
        add(quitButton);
    }

    private JButton createTextButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.PLAIN, 18));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.addActionListener(action);
        addHoverEffect(button);
        return button;
    }

    private void addHoverEffect(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(Color.LIGHT_GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.WHITE);
            }
        });
    }

    private void initListOfLevels(boolean is2d) {
        levels = new LevelsPanel2D(is2d);
    }

    private void start2DGame() {
        //GameEngine gameEngine = new GameEngine(gameState);
        //GameView3D gameView = new GameView3D(gameState.getCube(), gameState.getBoard(), gameEngine);
        //new GameController(gameState, gameView, gameEngine);
        initListOfLevels(true);
    }

    private void startLevelEditor2D() {
        Board boardEditor = JSONParseur.parseBoard("assets\\json\\levelEditor.json");
        if (boardEditor != null) {
            System.out.println("Board chargé !");
            System.out.println("Cube position: " + boardEditor.getCube().getX() + ", " + boardEditor.getCube().getY());
        }
        GameState gameStateEditor = new GameState(boardEditor);
        GameEngine gameEngineEditor = new GameEngine(gameStateEditor);
        GameView gameViewEditor = new GameView(gameStateEditor,2);
        LevelEditorPanel levelEditorPanel = gameViewEditor.getLevelEditorPanel();
        new GameController(gameStateEditor, gameViewEditor, gameEngineEditor, levelEditorPanel);
    }
}
