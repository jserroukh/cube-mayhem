package model;

public class GameEngine {

    private GameState gameState;
    private Board board;
    private Cube cube;

    public GameEngine(GameState gameState) {
        this.gameState = gameState;
        this.cube = gameState.getCube();
        this.board = gameState.getBoard();
    }

    public void update() {
        move2D();
    }

    private void applyInteractions() {
        int x = (int) cube.getX();
        int y = (int) cube.getY();
        board.interact(board.getGrid()[x][y]);
        cube.getCubeDirection().interact(cube);
    }

    public void move(){
                

        int x=(int)cube.getX();
        int y=(int)cube.getY();
        Direction temp = cube.getCubeDirection();
        System.out.println(""+x+" "+y+" "+ cube.getCubeDirection());
        System.out.println(gameState.isWinned());

        board.interact(board.getGrid()[x][y]);
        cube.getCubeDirection().interact(cube); //deplace avant d'interact
        
        if (temp!=cube.getCubeDirection()) 
            System.out.println("Changement "+temp +" to " + cube.getCubeDirection());
        
    }

    public void move2D() {
        applyInteractions();
    }

    public boolean isLost() {
        return gameState.isLosed();
    }

    public boolean isWin() {
        return gameState.isWinned();
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setCubeDirection(Direction dir) {
        cube.setCubeDirection(dir);
    }
}
