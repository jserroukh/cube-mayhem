package model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe pour analyser les fichiers JSON et les convertir en objets Java.
 */
public class JSONParser {

    /**
     * Lit un fichier JSON et retourne un objet Board correspondant.
     *
     * @param filePath Chemin du fichier JSON à lire.
     * @param cube Instance de Cube à mettre à jour.
     * @return L'objet Board correspondant au fichier JSON.
     */
    public static Board parseLevel(String filePath, Cube cube) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Lire le fichier JSON et le mapper sur une structure Java
            Board board = objectMapper.readValue(new File(filePath), Board.class);

            // Construire le stock de tuiles
            Map<TileType, Integer> tileStock = new HashMap<>();
            for (Map.Entry<TileType, Integer> entry : board.getTileStock().entrySet()) {
                tileStock.put(entry.getKey(), entry.getValue());
            }

            // Remplir la grille avec les tuiles
            for (Cellule[] grid : (Cellule[][]) board.getGrid()) {
                for (Cellule grid1 : grid) {
                    TileType tileType = grid1.getTile();
                    grid1.setTile(tileType);
                }
            }

            // Positionner le cube
            cube.setX(board.getCube().getX());
            cube.setY(board.getCube().getY());

            return board;

        } catch (Exception e) {
            return null;
        }
    }
}
