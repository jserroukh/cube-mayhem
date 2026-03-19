package model;
public enum Direction {
    NORTH("North"){
        public void interact(Cube c){
            c.setY(c.getY()-1);
        }
        public Direction rotate90() {
            return EAST;
        }

    },
    EAST("East"){
        public Direction rotate90() {
            return SOUTH;
        }
        public void interact(Cube c){
            c.setX(c.getX()+1);
        }

    },
    SOUTH("South"){
        public Direction rotate90() {
            return WEST;
        }
        public void interact(Cube c){
            c.setY(c.getY()+1);
        }
        
    },
    WEST("West"){   
        public Direction rotate90() {
            return NORTH;
        }
        public void interact(Cube c){
            c.setX(c.getX()-1);
        }
    };

   /*  private static  String str[] = { "North", "East" ,"South","West"};

    String str() {
        return str[this.ordinal()];
    }


    public Direction turn() {
        return values()[(this.ordinal() + 1) % values().length];
    }*/


    private String direction; 

    Direction(String direction) {
        this.direction = direction;
    }

    public abstract void interact(Cube c);
    public abstract Direction rotate90();
    
    public String getLabel() {
        return direction;
    }

    public void setLabel(String dir) {
        this.direction = dir;
    }

   /*  public static void main(String[] args) {

        TyleDirection direction = TyleDirection.NORTH;
        System.out.println("Direction : " + direction + " (" + direction.getLabel() + ")");

        direction.setLabel("Haut");
        System.out.println("Direction modifiée : " + direction + " (" + direction.getLabel() + ")");
    }*/
}
