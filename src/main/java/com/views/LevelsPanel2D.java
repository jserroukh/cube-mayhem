package views;

import model.*;
import controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LevelsPanel2D extends JPanel {
    private JFrame frame;
    private static final String LEVELS_DIRECTORY = "assets\\json";
    private boolean is2d;
    public LevelsPanel2D(boolean is2d) {
        this.is2d=is2d;
        setupFrame();
        setupPanel();
        addLevelButtons();
        frame.add(this);
        frame.setVisible(true);
    }

    private void setupFrame() {
        frame = new JFrame("Cube Mayhem - Levels");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
    }

    private void setupPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.BLACK);
    }

    private void addLevelButtons() {
        File directory = new File(LEVELS_DIRECTORY);
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Dossier de niveaux introuvable : " + LEVELS_DIRECTORY);
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            System.out.println("Erreur lors de la lecture du dossier : " + LEVELS_DIRECTORY);
            return;
        }

        Pattern pattern = Pattern.compile("level(\\d+)\\.json");
        for (File file : files) {
            Matcher matcher = pattern.matcher(file.getName());
            if (matcher.matches()) {
                String levelName = matcher.group(0).replace(".json", ""); // "levelX"
                JButton levelButton = createLevelButton(levelName, file.getPath());
                add(Box.createVerticalStrut(10)); // Espace entre les boutons
                add(levelButton);
            }
        }
    }

    private JButton createLevelButton(String levelName, String filePath) {
        JButton button = new JButton(levelName);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        styleButton(button);

        button.addActionListener(e -> startLevel(filePath));

        return button;
    }

    private void styleButton(JButton button) {
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.PLAIN, 18));

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

    private void startLevel(String filePath) {
        Board board = JSONParseur.parseBoard(filePath);
        if (board != null) {
            GameState gameState = new GameState(board);
            GameEngine gameEngine = new GameEngine(gameState);
            GameView gameView = new GameView(gameState,is2d);
            GamePanel gamePanel = gameView.getGamePanel();
            new GameController(gameState, gameView, gameEngine, gamePanel);

            frame.dispose(); // Ferme la fenêtre de sélection des niveaux
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement du niveau : " + filePath);
        }
    }
}
