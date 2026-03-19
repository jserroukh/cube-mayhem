package model;

public enum TileType {
    START("Start") {
        public void interact(Cube c, Direction d, Position[] arrayPosition) {c.setCubeDirection(d);}
        public void putPortalPosition(Position p, Position[] arrayPosition) {}
        public void removePortalPosition(Position p, Position[] arrayPosition) {}
    },
    FINISH("Finish") {
        public void interact(Cube c, Direction d, Position[] arrayPosition) {}
        public void putPortalPosition(Position p, Position[] arrayPosition) {}
        public void removePortalPosition(Position p, Position[] arrayPosition) {}
    },
    EMPTY("Empty") {
        public void interact(Cube c, Direction d, Position[] arrayPosition) {}
        public void putPortalPosition(Position p, Position[] arrayPosition) {}
        public void removePortalPosition(Position p, Position[] arrayPosition) {}
    },
    ARROW("Arrow") {
        public void interact(Cube c, Direction d, Position[] arrayPosition) {
            c.setCubeDirection(d);
        }
        public void putPortalPosition(Position p, Position[] arrayPosition) {}
        public void removePortalPosition(Position p, Position[] arrayPosition) {}
    },
    SLIDE("Slide") {
        public void interact(Cube c, Direction d, Position[] arrayPosition) {
            c.setIsSliding(true);
            c.setCubeDirection(d);
        }
        public void putPortalPosition(Position p, Position[] arrayPosition) {}
        public void removePortalPosition(Position p, Position[] arrayPosition) {}
    },
    ROTATOR("Rotator") {
        public void interact(Cube c, Direction d, Position[] arrayPosition) {c.setCubeDirection(c.getCubeDirection().rotate90());}
        public void putPortalPosition(Position p, Position[] arrayPosition) {}
        public void removePortalPosition(Position p, Position[] arrayPosition) {}
    },
    FORBIDDEN("Forbidden") {
        public void interact(Cube c, Direction d, Position[] arrayPosition) {}
        public void putPortalPosition(Position p, Position[] arrayPosition) {}
        public void removePortalPosition(Position p, Position[] arrayPosition) {}
    },
    BUTTON("Button") {
        public void interact(Cube c, Direction d, Position[] arrayPosition) {
            arrayPosition[6] = new Position(-1, -1);
        }
        public void putPortalPosition(Position p, Position[] arrayPosition) {}
        public void removePortalPosition(Position p, Position[] arrayPosition) {}
    },
    BRIDGE_OPEN("Bridge_Open") {
        public void interact(Cube c, Direction d, Position[] arrayPosition) {}
        public void putPortalPosition(Position p, Position[] arrayPosition) {}
        public void removePortalPosition(Position p, Position[] arrayPosition) {}
    },
    BRIDGE_CLOSE("Bridge_Close") {
        public void interact(Cube c, Direction d, Position[] arrayPosition) {}
        public void putPortalPosition(Position p, Position[] arrayPosition) {}
        public void removePortalPosition(Position p, Position[] arrayPosition) {}
    },
    PORTAL("Portal") {
        public void interact(Cube c, Direction d, Position[] arrayPosition) {
            c.setIsTping(true);
            Position current = c.getPosition();
            if (!arrayPosition[0].isNull() && !arrayPosition[1].isNull()) {
                if (arrayPosition[0].equals(current)) {
                    c.setPosition(arrayPosition[1]);
                } else if (arrayPosition[1].equals(current)) {
                    c.setPosition(arrayPosition[0]);
                }
            }
        }
        public void putPortalPosition(Position p, Position[] arrayPosition) {
            if (arrayPosition[0].isNull()) {
                arrayPosition[0] = p;
            } else {
                arrayPosition[1] = p;
            }
        }
        public void removePortalPosition(Position p, Position[] arrayPosition) {
            Position p2 = new Position(0, 0);
            if (arrayPosition[0].equals(p)) {
                arrayPosition[0] = p2;
            } else if (arrayPosition[1].equals(p)) {
                arrayPosition[1] = p2;
            }
        }
    },
    PORTAL_2("Portal_2") {
        public void interact(Cube c, Direction d, Position[] arrayPosition) {
            c.setIsTping(true);
            Position p = c.getPosition();
            if (arrayPosition[2].equals(p)) {
                c.setPosition(arrayPosition[3]);
            } else if (arrayPosition[3].equals(p)) {
                c.setPosition(arrayPosition[2]);
            }
        }
        public void putPortalPosition(Position p, Position[] arrayPosition) {
            if (arrayPosition[2].isNull()) {
                arrayPosition[2] = p;
            } else {
                arrayPosition[3] = p;
            }
        }
        public void removePortalPosition(Position p, Position[] arrayPosition) {
            Position p2 = new Position(0, 0);
            if (arrayPosition[2].equals(p)) {
                arrayPosition[2] = p2;
            } else if (arrayPosition[3].equals(p)) {
                arrayPosition[3] = p2;
            }
        }
    },    
    PORTAL_3("Portal_3") {
        public void interact(Cube c, Direction d, Position[] arrayPosition) {
            c.setIsTping(true);
            Position p = c.getPosition();
            if (arrayPosition[4].equals(p)) {
                c.setPosition(arrayPosition[5]);
            } else if (arrayPosition[5].equals(p)) {
                c.setPosition(arrayPosition[4]);
            }
        }
        public void putPortalPosition(Position p, Position[] arrayPosition) {
            if (arrayPosition[4].isNull()) {
                arrayPosition[4] = p;
            } else {
                arrayPosition[5] = p;
            }
        }
        public void removePortalPosition(Position p, Position[] arrayPosition) {
            Position p2 = new Position(0, 0);
            if (arrayPosition[4].equals(p)) {
                arrayPosition[4] = p2;
            } else if (arrayPosition[5].equals(p)) {
                arrayPosition[5] = p2;
            }
        }
    };

    private String tile;

    private TileType(String tile) {
        this.tile = tile;
    }

    public abstract void interact(Cube c, Direction d, Position[] arrayCoor);
    public abstract void putPortalPosition(Position p, Position[] arrayPosition);
    public abstract void removePortalPosition(Position p, Position[] arrayPosition);

    public String getTileType() {
        return tile;
    }

    public void setTile(String tile) {
        this.tile = tile;
    }
    public boolean isDefaultTile() {
        return this == START || this == FINISH || this == EMPTY ||
               this == FORBIDDEN || this == BUTTON ||
               this == BRIDGE_OPEN || this == BRIDGE_CLOSE;
    }
}