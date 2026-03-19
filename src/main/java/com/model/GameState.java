package model;

import java.awt.Color;
import java.util.HashMap;

public class GameState {

    private int defaultGameWidth;
    private int defaultGameHeight;
    private int gameWidth;
    private int gameHeight;
    private Color gameBgColor;
    private Board board;
    private int boardSize;
    private Cube cube;
    private Direction direction;

    public GameState() {
        defaultGameWidth = 1080;
        defaultGameHeight = 720;
        this.boardSize = 10;//taille par default
        gameWidth = defaultGameWidth;
        gameHeight = defaultGameHeight;
        gameBgColor = Color.BLACK;
        this.direction = Direction.SOUTH;//direction par default
        this.cube = new Cube(0, 0, 2, direction);
        board = new Board(boardSize, cube, new HashMap<>());
        createBoard();

    }

    public GameState(Board b) {
        defaultGameWidth = 1080;
        defaultGameHeight = 720;
        this.boardSize = 10;//taille par default
        gameWidth = defaultGameWidth;
        gameHeight = defaultGameHeight;
        gameBgColor = Color.BLACK;
        board = b;
        this.cube = board.getCube();
        this.direction = this.cube.getCubeDirection();
    }

    public void createBoardFromInput() {

    }

    public void createBoard() {

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board.placeTile(TileType.EMPTY, Direction.SOUTH, i, j);
            }
        }
        board.placeTile(TileType.START, Direction.SOUTH, 0, 0);
    }

    public boolean isLosed() {
        return board.isForbiden();
    }

    public boolean isWinned() {
        return board.isFinish();
    }

    public void setGameDifaultDimension() {
        gameWidth = this.defaultGameWidth;
        gameHeight = this.defaultGameHeight;
    }

    public void setGameDimension(int Width, int Height) {
        this.gameWidth = Width;
        this.gameHeight = Height;
    }

    public void setBoardSize(int size) {
        this.boardSize = size;
    }

    public Board getBoard() {
        return board;
    }

    public Cube getCube() {
        return cube;
    }

    public Direction getDirection() {
        return direction;
    }

    public Color getGameBgColor() {
        return gameBgColor;
    }

    public int getGameWidth() {
        return gameWidth;
    }

    public int getGameHeight() {
        return gameHeight;
    }

    private TileType selectedTile;

    public void setSelectedTile(TileType type) {
        this.selectedTile = type;
    }

    public TileType getSelectedTile() {
        return this.selectedTile;
    }

}
