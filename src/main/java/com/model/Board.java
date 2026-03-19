package model;

import java.util.HashMap;
import java.util.Map;
import java.awt.Point;

public class Board {
    private int size;
    private Cellule[][] grid;
    private Cube cube;
    private Map<TileType, Integer> tileStock; // Pour stocker les quantite de chque tuile lie un nom et une quantite
    private Position[] arrayPosition = new Position[7];

    public Board(int n, Cube c, Map<TileType, Integer> initialTiles) { // stockage des tile par niveaux
        this.size = n;
        this.grid = new Cellule[n][n];
        this.cube = c;
        this.tileStock = new HashMap<>(initialTiles);
        for (int i = 0; i < n; i++) {// pour les null pointer excepiton quil y avait dans la console
            for (int j = 0; j < n; j++) {
                grid[i][j] = new Cellule(TileType.FORBIDDEN);
            }
        }
        Position p = new Position(0, 0);
        for (int i = 0; i < arrayPosition.length; i++) {
            arrayPosition[i] = p;
        }

    }

    public void interact(Cellule c) {
        if (c.getTile().equals(TileType.FINISH)) {
            System.out.println("win");
        }
        if (c.getTile().equals(TileType.FORBIDDEN) || c.getTile().equals(TileType.BRIDGE_CLOSE)) {
            System.out.println("lose");
        } else {
            c.interact(cube, arrayPosition);
            if (arrayPosition[6].getX() == -1) {// Vérifie si le bouton a été activé lors de l'appel d'interact et ouvre
                                                // le pont dans ce cas
                for (int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid.length; j++) {
                        if (grid[i][j].getTile().equals(TileType.BRIDGE_CLOSE)) {
                            grid[i][j].setTile(TileType.BRIDGE_OPEN);
                        }
                    }
                }
            }
        }
    }

    public boolean placeTile(TileType tiletype, Direction d, int x, int y) {// x et y les coordonnees pour l'insant, d
                                                                            // la direction choisi
        if (!tileStock.containsKey(tiletype) || tileStock.get(tiletype) <= 0) {
            return false; // Pas assez de tuiles
        }

        Position p = new Position(x, y);
        tiletype.putPortalPosition(p, arrayPosition);

        grid[x][y] = new Cellule(tiletype, d);// ajout de la tuile a board reste plus qua diminuer le stock
        tileStock.put(tiletype, tileStock.get(tiletype) - 1);// diminution du stock
        return true;
    }

    public boolean placeTileEditor(TileType tiletype, Direction d, int x, int y) {// x et y les coordonnees pour
                                                                                  // l'insant, d
        // la direction choisi
        /*
         * if (!tileStock.containsKey(tiletype) || tileStock.get(tiletype) <= 0) {
         * return false; // Pas assez de tuiles
         * }
         */

        Position p = new Position(x, y);
        tiletype.putPortalPosition(p, arrayPosition);

        grid[x][y] = new Cellule(tiletype, d);// ajout de la tuile a board reste plus qua diminuer le stock
        // tileStock.put(tiletype, tileStock.get(tiletype) + 1);
        tileStock.put(tiletype, tileStock.getOrDefault(tiletype, 0) + 1);
        return true;
    }

    public void removeTile(int x, int y) {

        if (x < 0 || y < 0 || x >= size || y >= size) {
            return;
        }
        TileType tileType = grid[x][y].getTile();
        Position p = new Position(x, y);
        tileType.removePortalPosition(p, arrayPosition);

        tileStock.put(tileType, tileStock.get(tileType) + 1);
        tileType = TileType.EMPTY; // Utilisation de la valeur existante de l'énumération
        grid[x][y] = new Cellule(tileType);
    }

    public void removeTileEditor(int x, int y) {

        if (x < 0 || y < 0 || x >= size || y >= size) {
            return;
        }
        TileType tileType = grid[x][y].getTile();
        Position p = new Position(x, y);
        tileType.removePortalPosition(p, arrayPosition);

        // tileStock.put(tileType, tileStock.get(tileType) - 1);
        int newCount = tileStock.getOrDefault(tileType, 0) - 1;
        if (newCount <= 0) {
            tileStock.remove(tileType); // Retirer du stock si 0 ou moins
        } else {
            tileStock.put(tileType, newCount);
        }
        if(tileType==TileType.EMPTY){
            tileType = TileType.FORBIDDEN;
        }else{
            tileType = TileType.EMPTY;
        } 
        grid[x][y] = new Cellule(tileType);
    }

    public boolean isForbiden() {
        if (grid[(int) cube.getX()][(int) cube.getY()].getTile().equals(TileType.FORBIDDEN) ||
                grid[(int) cube.getX()][(int) cube.getY()].getTile().equals(TileType.BRIDGE_CLOSE)) {
            return true;
        }
        return false;
    }

    public boolean isFinish() {
        return grid[(int) cube.getX()][(int) cube.getY()].getTile().equals(TileType.FINISH);
    }

    // tuile selectionne pour etre place que lon va retirer
    public boolean pullTileOnStock(TileType tile) {
        if (tileStock.containsKey(tile) && tileStock.get(tile) > 0) {
            tileStock.put(tile, tileStock.get(tile) - 1);
            if (tileStock.get(tile) == 0) {
                tileStock.remove(tile);
            }
            return true;
        }
        return false;
    }

    // Rajout de la tuile dans le stock
    public void putTileOnStock(TileType tile) {
        tileStock.put(tile, tileStock.getOrDefault(tile, 0) + 1);
    }

    public Cellule getTile(int row, int col) {
        return grid[row][col];
    }

    public int getSize() {
        return size;
    }

    public Cellule[][] getGrid() {
        return grid;
    }

    public Cube getCube() {
        return cube;
    }

    public Map<TileType, Integer> getTileStock() {
        return this.tileStock;
    }

    public void setGrid(Cellule[][] grid) {
        this.grid = grid;
    }

    public void setCube(Cube cube) {
        this.cube = cube;
    }

    public Point getTileStart() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].getTile() == TileType.START) {
                    return new Point(i, j);
                }
            }
        }

        return null;
    }

    public void cleanBoard(){
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (!grid[i][j].getTile().isDefaultTile()) {
                    grid[i][j].setTile(TileType.EMPTY);  
                }
            }
        }
        tileStock.remove(TileType.EMPTY);
        tileStock.remove(TileType.START);
        tileStock.remove(TileType.FINISH);
        tileStock.remove(TileType.FORBIDDEN);
        tileStock.remove(TileType.BUTTON);
        tileStock.remove(TileType.BRIDGE_OPEN);
        tileStock.remove(TileType.BRIDGE_CLOSE);
    }
}
