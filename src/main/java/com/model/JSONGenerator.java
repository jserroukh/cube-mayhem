package model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe pour générer des fichiers JSON de niveaux à partir d'un Board.
 */
public class JSONGenerator {

    /**
     * Génère un fichier JSON pour un niveau à partir d'un Board.
     * Le nom du fichier est automatiquement déterminé en fonction des fichiers existants.
     *
     * @param directoryPath Dossier où enregistrer le fichier JSON.
     * @param board         Le plateau de jeu à exporter.
     */
    public static void generateLevel(String directoryPath, Board board) {
        try {
            ensureDirectoryExists(directoryPath);

            int nextLevelNumber = getNextLevelNumber(directoryPath);
            String filePath = directoryPath + File.separator + "level" + nextLevelNumber + ".json";

            ObjectNode levelData = createLevelData(board);

            writeJsonFile(filePath, levelData);

            System.out.println("Niveau généré avec succès dans le fichier : " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void ensureDirectoryExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Dossier créé : " + directoryPath);
            } else {
                System.out.println("Erreur lors de la création du dossier : " + directoryPath);
            }
        }
    }

    private static int getNextLevelNumber(String directoryPath) {
        File directory = new File(directoryPath);
        int maxLevel = 0;
        Pattern pattern = Pattern.compile("level(\\d+)\\.json");

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                Matcher matcher = pattern.matcher(file.getName());
                if (matcher.matches()) {
                    int levelNumber = Integer.parseInt(matcher.group(1));
                    if (levelNumber > maxLevel) {
                        maxLevel = levelNumber;
                    }
                }
            }
        }
        return maxLevel + 1;
    }

    private static ObjectNode createLevelData(Board board) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode levelData = objectMapper.createObjectNode();

        levelData.put("size", board.getSize());

        levelData.set("cube", createCubeData(board));
        levelData.set("tileStock", createTileStockData(board));
        levelData.set("grid", createGridData(board));

        return levelData;
    }

    private static ObjectNode createCubeData(Board board) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode cubeData = objectMapper.createObjectNode();
        Cube cube = board.getCube();
        cubeData.put("x", cube.getX());
        cubeData.put("y", cube.getY());
        cubeData.put("cubeSpeed", cube.getCubeSpeed());
        cubeData.put("cubeDirection", cube.getCubeDirection().toString());
        return cubeData;
    }

    private static ObjectNode createTileStockData(Board board) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode tileStockData = objectMapper.createObjectNode();
        for (Map.Entry<TileType, Integer> entry : board.getTileStock().entrySet()) {
            tileStockData.put(entry.getKey().toString(), entry.getValue());
        }
        return tileStockData;
    }

    private static ArrayNode createGridData(Board board) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode gridArray = objectMapper.createArrayNode();
        Cellule[][] grid = board.getGrid();

        for (int i = 0; i < board.getSize(); i++) {
            ArrayNode rowArray = objectMapper.createArrayNode();
            for (int j = 0; j < board.getSize(); j++) {
                ObjectNode cellData = objectMapper.createObjectNode();
                cellData.put("tile", grid[i][j].getTile().toString());
                cellData.put("direction", grid[i][j].getTileDirection().toString());
                rowArray.add(cellData);
            }
            gridArray.add(rowArray);
        }
        return gridArray;
    }

    private static void writeJsonFile(String filePath, ObjectNode levelData) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), levelData);
    }
}


