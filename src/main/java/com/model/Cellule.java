package model;
public class Cellule {
    private TileType tile;
    private Direction direction;

    public Cellule(TileType tile, Direction tileDirection) {
        this.tile = tile;
        this.direction = tileDirection;
    }

    public Cellule(TileType tile) {
        this.tile = tile;
        this.direction = null;
    }

    public void interact(Cube c, Position[] arrayCoor) {
        tile.interact(c,direction, arrayCoor);
    }

    public TileType getTile() {
        return tile;
    }

    public void setTile(TileType tile) {
        this.tile = tile;
    }

    public Direction getTileDirection() {
        return direction;
    }

    public void setTileDirection(Direction tileDirection) {
        this.direction = tileDirection;
    }

}
