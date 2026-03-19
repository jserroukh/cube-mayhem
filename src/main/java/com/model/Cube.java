package model;
public class Cube {
    private long x;
    private long y;
    private long cubeSpeed;
    private Direction cubeDirection;

    private boolean isSliding = false;
    private boolean isTping = false;
 
    public Cube(long x, long y, long cubeSpeed, Direction cubeDirection) {
        this.x = x;
        this.y = y;
        this.cubeSpeed = cubeSpeed;
        this.cubeDirection = cubeDirection;
    }
    public Position getPosition(){
        Position p=new Position((int)x,(int) y);
        return p;
    }
    public void setPosition(Position p){
        setX(p.getX());
        setY(p.getY());
        System.out.println(p.getX()+"  "+p.getY());
    }
    public Direction getCubeDirection() {
        return cubeDirection;
    }
    public void setCubeDirection(Direction cubeDirection) {
        this.cubeDirection = cubeDirection;
    }
    
    public long getX() {
        return x;
    }
    public void setX(long x) {
        this.x = x;
    }
    public long getY() {
        return y;
    }
    public void setY(long y) {
        this.y = y;
    }
    public long getCubeSpeed() {
        return cubeSpeed;
    }
    public void setCubeSpeed(long cubeSpeed) {
        this.cubeSpeed = cubeSpeed;
    }
    public boolean getIsSliding(){
        return isSliding;
    }

    public void setIsSliding(boolean b){
        isSliding = b;
    }

     public boolean getTping(){
        return isTping;
    }
    public void setIsTping(boolean b){
        isTping = b;
    }
}
