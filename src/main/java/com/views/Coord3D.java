package views;

import java.awt.geom.Point2D;

public class Coord3D {

    private double x;
    private double y;
    private double z;

    public Coord3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public Coord3D vectorProduct(Coord3D coord3d) {
        double nx = coord3d.y * this.z - coord3d.z * this.y;
        double ny = coord3d.z * this.x - coord3d.x * this.z;
        double nz = coord3d.x * this.y - coord3d.y * this.x;

        return new Coord3D(nx, ny, nz);
    }

    public double scalaireProduct(Coord3D coord3d) {
        return coord3d.x * this.x + coord3d.y * this.y + coord3d.z * this.z;
    }

    public static Point2D.Double projectIsometric(Coord3D coord3d) {
        double x = coord3d.getX();
        double y = coord3d.getY();
        double z = coord3d.getZ();

        double x2D = (Math.sqrt(3) / 2) * x - (Math.sqrt(3) / 2) * z;
        double y2D = (1.0 / 2) * x + y + (1.0 / 2) * z;

        return new Point2D.Double(x2D, y2D);
    }

    public double norme() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public Coord3D sumVectors(Coord3D v2) {
        return new Coord3D(this.x + v2.x, this.y + v2.y, this.z + v2.z);
    }

    public Coord3D subVectors(Coord3D v2) {
        return new Coord3D(this.x - v2.x, this.y - v2.y, this.z - v2.z);
    }

    public Coord3D externProduct(double d) {
        return new Coord3D(this.x * d, this.y * d, this.z * d);
    }

}
