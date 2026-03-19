
package views;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import controller.GameController;
import model.*;

public class MapPanel extends JPanel {

    private GameState gameState;
    private GamePanel gamePanel;
    private GameController gameController;
    private int tileSize = 32;
    private Direction selectedDirection = Direction.SOUTH;

    public MapPanel(GamePanel gamePanel, GameState gameState) {
        this.gamePanel = gamePanel;
        this.gameState = gameState;
        this.gameController = gamePanel.getGameController();
        setLayout(null);
        setPreferredSize(new Dimension(gameState.getGameWidth(), gameState.getGameHeight() - 100));
        setBackground(gameState.getGameBgColor());

        ajouterZoomMolette();
    }

    private void ajouterZoomMolette() {
        addMouseWheelListener(e -> {
            int notches = e.getWheelRotation();
            int newTileSize = tileSize - (notches * 4); // Chaque cran = 4px
    
            // Limiter la taille entre 16px et 128px
            newTileSize = Math.max(16, Math.min(128, newTileSize));
            
            setTileSize(newTileSize);
    
            // Ajuster la taille du panneau si besoin
            int newWidth = gameState.getBoard().getSize() * tileSize;
            int newHeight = gameState.getBoard().getSize() * tileSize;
            setPreferredSize(new Dimension(newWidth, newHeight));
    
            revalidate();
            repaint();
        });
    }

    public boolean isRemovableTile(TileType tile) {
        return tile != TileType.EMPTY && tile != TileType.FORBIDDEN &&
               tile != TileType.BRIDGE_OPEN && tile != TileType.BRIDGE_CLOSE &&
               tile != TileType.FINISH && tile != TileType.START &&
               tile != TileType.BUTTON;
    }

    public void removeTileFromBoard(Board board, int col, int row) {
        board.removeTile(col, row);
        repaint();
        gamePanel.updateTileButtons();
    }

    public void placeTileOnBoard(Board board, int col, int row) {
        TileType selected = gamePanel.getSelectedTile();
        if (selected != null) {
            if (isDirectionalTile(selected)) {
                showDirectionMenu(col, row, selected);
            } else {
                if (!board.placeTile(selected, selectedDirection, col, row)) {
                    showErrorPopup("Pas assez de tuiles : " + selected.getTileType());
                }
                repaint();
                gamePanel.updateTileButtons();
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
    
        menu.add(north);
        menu.add(south);
        menu.add(west);
        menu.add(east);
    
        int popupX = col * tileSize;
        int popupY = row * tileSize;
        menu.show(this, popupX, popupY);
    }
    public void placeTileWithDirection(int col, int row, TileType selected, Direction direction) {
        if (!gameState.getBoard().placeTile(selected, direction, col, row)) {
            showErrorPopup("Pas assez de tuiles : " + selected.getTileType());
        }
        repaint();
        gamePanel.updateTileButtons();
    }
    
    private void showErrorPopup(String message) {
        JOptionPane.showMessageDialog(
            this,             // Parent
            message,          // Message
            "Erreur",         // Titre
            JOptionPane.ERROR_MESSAGE // Type d'alerte
        );
    }
    
    private boolean isDirectionalTile(TileType type) {
        return type == TileType.ARROW || type == TileType.SLIDE || type == TileType.ROTATOR;
    }
    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
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
                            Image img = icon.getImage();
                            g2d.drawImage(img, x, y, tileSize, tileSize, this);
                        } else if (type == TileType.PORTAL || type == TileType.PORTAL_2 || type == TileType.PORTAL_3 || 
                                   type == TileType.START || type == TileType.BRIDGE_CLOSE || type == TileType.BRIDGE_OPEN || 
                                   type == TileType.FINISH || type == TileType.BUTTON) {
                            String path = "assets\\imgs\\imgs2D\\" + type.getTileType() + ".png";
                            ImageIcon icon = new ImageIcon(path);
                            Image img = icon.getImage();
                            g2d.drawImage(img, x, y, tileSize, tileSize, this);
                        } else {
                            // Sinon, juste remplir avec la couleur
                            Color color = GamePanel.colorChoice(type);
                            g2d.setColor(color);
                            g2d.fillRect(x, y, tileSize, tileSize);
                        }
                    } catch (Exception e) {
                        System.out.println("Erreur de chargement de l'image pour " + type.getTileType());
                    }
    
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(x, y, tileSize, tileSize);
                }
            }
        }
    }
    
   

    private void dessinerCube(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Cube cube = gameState.getCube();
        int cubeX = (int) cube.getX() * tileSize;
        int cubeY = (int) cube.getY() * tileSize;

        g2d.setColor(Color.WHITE);
        g2d.fillRect(cubeX, cubeY, tileSize, tileSize);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(cubeX, cubeY, tileSize, tileSize);
    }
} 

