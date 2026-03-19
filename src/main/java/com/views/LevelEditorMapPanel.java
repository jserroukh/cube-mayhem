package views;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import model.*;

public class LevelEditorMapPanel extends JPanel {

    private GameState gameState;
    private LevelEditorPanel levelEditorPanel;
    private int tileSize = 32;
    private Direction selectedDirection = Direction.SOUTH;
    private boolean showCube = false;

    public LevelEditorMapPanel(LevelEditorPanel gamePanel, GameState gameState) {
        this.levelEditorPanel = gamePanel;
        this.gameState = gameState;
        setLayout(null);
        setPreferredSize(new Dimension(gameState.getGameWidth(), gameState.getGameHeight() - 100));
        setBackground(gameState.getGameBgColor());
        ajouterZoomMolette();
    }

    public void gererClicGauche(int col, int row) {
        Board board = gameState.getBoard();
        Cellule cellule = board.getTile(col, row);
        if (cellule == null) return;

        TileType currentTile = cellule.getTile();
        if (currentTile == TileType.EMPTY || currentTile == TileType.FORBIDDEN) {
            placeTileOnBoard(board, col, row);
        }
    }

    public void gererClicDroit(int col, int row) {
        Board board = gameState.getBoard();
        Cellule cellule = board.getTile(col, row);
        if (cellule == null) return;

        TileType currentTile = cellule.getTile();
        if (isRemovableTile(currentTile)) {
            removeTileFromBoard(board, col, row);
        }
    }

    public void ajouterZoomMolette() {
        addMouseWheelListener(e -> {
            int notches = e.getWheelRotation();
            int newTileSize = tileSize - (notches * 4);
            newTileSize = Math.max(16, Math.min(128, newTileSize));
            setTileSize(newTileSize);
            int newWidth = gameState.getBoard().getSize() * tileSize;
            int newHeight = gameState.getBoard().getSize() * tileSize;
            setPreferredSize(new Dimension(newWidth, newHeight));
            revalidate();
            repaint();
        });
    }

    private boolean isRemovableTile(TileType tile) {
        return tile != TileType.FORBIDDEN;
    }

    private void removeTileFromBoard(Board board, int col, int row) {
        board.removeTileEditor(col, row);
        repaint();
        levelEditorPanel.updateTileButtons();
    }

    private void placeTileOnBoard(Board board, int col, int row) {
        TileType selected = levelEditorPanel.getSelectedTile();
        if (selected != null) {
            if (isDirectionalTile(selected)) {
                showDirectionMenu(col, row, selected);
            } else {
                board.placeTileEditor(selected, Direction.SOUTH, col, row);
                repaint();
                levelEditorPanel.updateTileButtons();
            }
        }
    }

    private void showDirectionMenu(int col, int row, TileType selected) {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem north = new JMenuItem("↑ Nord");
        JMenuItem south = new JMenuItem("↓ Sud");
        JMenuItem west = new JMenuItem("← Ouest");
        JMenuItem east = new JMenuItem("→ Est");

        north.addActionListener(e -> placeTileWithDirection(col, row, selected, Direction.NORTH));
        south.addActionListener(e -> placeTileWithDirection(col, row, selected, Direction.SOUTH));
        west.addActionListener(e -> placeTileWithDirection(col, row, selected, Direction.WEST));
        east.addActionListener(e -> placeTileWithDirection(col, row, selected, Direction.EAST));

        int popupX = col * tileSize;
        int popupY = row * tileSize;
        menu.add(north);
        menu.add(south);
        menu.add(west);
        menu.add(east);
        menu.show(this, popupX, popupY);
    }

    private void placeTileWithDirection(int col, int row, TileType selected, Direction direction) {
        if (!gameState.getBoard().placeTileEditor(selected, direction, col, row)) {
            showErrorPopup("Pas assez de tuiles : " + selected.getTileType());
        }
        repaint();
        levelEditorPanel.updateTileButtons();
    }

    private void showErrorPopup(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    private boolean isDirectionalTile(TileType type) {
        return type == TileType.ARROW || type == TileType.SLIDE || type == TileType.ROTATOR;
    }

    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }

    public void setShowCube(boolean show) {
        this.showCube = show;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        dessinerGrille(g);
        dessinerCube(g);
    }

    private void dessinerGrille(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Board board = gameState.getBoard();
        if (board == null) return;

        Cellule[][] grid = board.getGrid();
        if (grid == null) return;

        int size = board.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Cellule cellule = grid[j][i];
                if (cellule != null) {
                    TileType type = cellule.getTile();
                    int x = j * tileSize;
                    int y = i * tileSize;

                    try {
                        if (type == TileType.ARROW || type == TileType.ROTATOR || type == TileType.SLIDE) {
                            String path = "assets\\imgs\\imgs2D\\" + type.getTileType() + cellule.getTileDirection() + ".png";
                            ImageIcon icon = new ImageIcon(path);
                            g2d.drawImage(icon.getImage(), x, y, tileSize, tileSize, this);
                        } else if (type == TileType.PORTAL || type == TileType.PORTAL_2 || type == TileType.PORTAL_3 || 
                                   type == TileType.START || type == TileType.BRIDGE_CLOSE || type == TileType.BRIDGE_OPEN || 
                                   type == TileType.FINISH || type == TileType.BUTTON) {
                            String path = "assets\\imgs\\imgs2D\\" + type.getTileType() + ".png";
                            ImageIcon icon = new ImageIcon(path);
                            g2d.drawImage(icon.getImage(), x, y, tileSize, tileSize, this);
                        } else {
                            Color color = GamePanel.colorChoice(type);
                            g2d.setColor(color);
                            g2d.fillRect(x, y, tileSize, tileSize);
                        }
                    } catch (Exception e) {
                        System.out.println("Erreur de chargement de l'image pour " + type.getTileType());
                    }

                    dessinerBordureCase(g2d, x, y);
                }
            }
        }
    }

    private void dessinerCube(Graphics g) {
        if (!showCube) return;

        Graphics2D g2d = (Graphics2D) g;
        Cube cube = gameState.getCube();
        int cubeX = (int) cube.getX() * tileSize;
        int cubeY = (int) cube.getY() * tileSize;

        g2d.setColor(Color.WHITE);
        g2d.fillRect(cubeX, cubeY, tileSize, tileSize);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(cubeX, cubeY, tileSize, tileSize);
    }

    private void dessinerBordureCase(Graphics2D g2d, int x, int y) {
        g2d.setColor(Color.GREEN);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(x, y, tileSize, tileSize);
    }
    
    public GameState getGameState() {
        return gameState;
    }
}

