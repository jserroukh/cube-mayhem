package model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class JSONParseur {

    public static Board parseBoard(String filePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(new File(filePath));

            int size = root.get("size").asInt();

            // Parsing cube
            JsonNode cubeNode = root.get("cube");
            Cube cube = new Cube(
                    cubeNode.get("x").asLong(),
                    cubeNode.get("y").asLong(),
                    cubeNode.get("cubeSpeed").asLong(),
                    Direction.valueOf(cubeNode.get("cubeDirection").asText())
            );

            // Parsing tile stock
            Map<TileType, Integer> tileStock = new HashMap<>();
            JsonNode stockNode = root.get("tileStock");
            for (JsonNode entry : stockNode) {
                // Alternative: if tileStock is an object
                stockNode.fields().forEachRemaining(field -> {
                    TileType type = TileType.valueOf(field.getKey());
                    int value = field.getValue().asInt();
                    tileStock.put(type, value);
                });
                break; // prevent looping multiple times if object
            }

            // Create Board
            Board board = new Board(size, cube, tileStock);

            // Parsing grid
            JsonNode gridNode = root.get("grid");
            Cellule[][] grid = new Cellule[size][size];
            for (int i = 0; i < size; i++) {
                JsonNode row = gridNode.get(i);
                for (int j = 0; j < size; j++) {
                    JsonNode cell = row.get(j);
                    TileType tile = TileType.valueOf(cell.get("tile").asText());
                    Direction dir = cell.has("direction") && !cell.get("direction").isNull()
                            ? Direction.valueOf(cell.get("direction").asText())
                            : null;
                    grid[i][j] = (dir != null) ? new Cellule(tile, dir) : new Cellule(tile);
                }
            }

            board.setGrid(grid);
            return board;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
