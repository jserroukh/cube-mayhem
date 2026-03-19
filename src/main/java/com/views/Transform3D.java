package views;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class Transform3D {
    private double[][] matrice;

    public Transform3D(double[][] matrice) {
        this.matrice = matrice;
    }

    public Coord3D apply(Coord3D coord3d) {
        double[] result = new double[4];
        result[0] = matrice[0][0] * coord3d.getX() + matrice[0][1] * coord3d.getY() + matrice[0][2] * coord3d.getZ()
                + matrice[0][3];
        result[1] = matrice[1][0] * coord3d.getX() + matrice[1][1] * coord3d.getY() + matrice[1][2] * coord3d.getZ()
                + matrice[1][3];
        result[2] = matrice[2][0] * coord3d.getX() + matrice[2][1] * coord3d.getY() + matrice[2][2] * coord3d.getZ()
                + matrice[2][3];
        result[3] = matrice[3][0] * coord3d.getX() + matrice[3][1] * coord3d.getY() + matrice[3][2] * coord3d.getZ()
                + matrice[3][3];
        
        return new Coord3D(result[0], result[1], result[2]);
    }

    public Polygon applyToFace(Polygon face) {
        Coord3D[] transformedVertices = new Coord3D[face.getVertices().length];
        for (int i = 0; i < transformedVertices.length; i++) {
            transformedVertices[i] = apply(face.getVertices()[i]);
        }
        return new Polygon(transformedVertices);
    }

    public Polyhedron applyToCube(Polyhedron cube) {
        Polygon[] transformedFaces = new Polygon[cube.getFaces().length];
        for (int i = 0; i < transformedFaces.length; i++) {
            transformedFaces[i] = applyToFace(cube.getFaces()[i]);
        }
        return new Polyhedron(transformedFaces);
    }


    public double[][] compose(double[][] matrice1) {
        double[][] result = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    result[i][j] += this.matrice[i][k] * matrice1[k][j];
                }
            }
        }
        this.matrice = result;
        return result;
    }

    public AffineTransform toTransform2D() {
        return new AffineTransform(
                matrice[0][0], matrice[1][0],
                matrice[0][1], matrice[1][1],
                matrice[0][3], matrice[1][3]);
    }

    public double[][] createRotationMatrixX(double angle) {
        double angle1 = Math.toRadians(angle);
        double cos = Math.cos(angle1);
        double sin = Math.sin(angle1);
        double[][] rotationX1 = {
                { 1, 0, 0, 0 },
                { 0, cos, -sin, 0 },
                { 0, sin, cos, 0 },
                { 0, 0, 0, 1 }
        };
        return rotationX1;
    }

    public double[][] createRotationMatrixY(double angle) {
        double angle1 = Math.toRadians(angle);
        double cos = Math.cos(angle1);
        double sin = Math.sin(angle1);
        double[][] rotationY1 = {
                { cos, 0, sin, 0 },
                { 0, 1, 0, 0 },
                { -sin, 0, cos, 0 },
                { 0, 0, 0, 1 }
        };
        return rotationY1;
    }

    public double[][] createRotationMatrixZ(double angle) {
        double angle1 = Math.toRadians(angle);
        double cos = Math.cos(angle1);
        double sin = Math.sin(angle1);
        double[][] rotationY1 = {
                { cos, -sin, 0, 0 },
                { sin, cos, 0, 0 },
                { 0, 0, 1, 0 },
                { 0, 0, 0, 1 }
        };
        return rotationY1;
    }

    public double[][] createTranslationMatrixX(double x, double y, double z) {
        double[][] rotationX1 = {
                { 1, 0, 0, x },
                { 0, 1, 0, y },
                { 0, 0, 1, z },
                { 0, 0, 0, 1 }
        };
        return rotationX1;
    }

    public double[][] createScaleMatrix(double x) {
        double[][] rotationX1 = {
                { x, 0, 0, 0 },
                { 0, x, 0, 0 },
                { 0, 0, x, 0 },
                { 0, 0, 0, 1 }
        };
        return rotationX1;
    }

    public double[][] getMatrice() {
        return this.matrice;
    }

    public void setMatrice(double[][] matrice) {
        this.matrice = matrice;
    }

    public void affiche() {
        for (int i = 0; i < matrice.length; i++) {
            for (int j = 0; j < matrice.length; j++) {
                System.out.print(" [" + matrice[i][j] + " ], ");
            }
            System.out.println(" ");
        }

    }

    public Transform3D toCube(int i, Polygon face, Transform3D transform) {

        double[][] rotationX1 = transform.createRotationMatrixX(90);
        double[][] rotationY1 = transform.createRotationMatrixY(-90);
        double[][] translationMoins = transform.createTranslationMatrixX(0, 0, -160);
        double[][] translationPlus = transform.createTranslationMatrixX(0, 0, 160);
        if (i == 4) {//good 4
            transform.compose(rotationX1);
        } 
        else if (i == 3 ) {//good 3
            transform.compose(rotationY1);
        }
        else if (i == 5) {//good 5
            transform.compose(rotationY1);
            transform.compose(translationMoins);
        }
        else if (i == 0) {//good 0
            transform.compose(translationPlus);
        }
        else if (i == 2) {// good 2
            transform.compose(rotationX1);
            transform.compose(translationMoins);
        }else if (i == 1) {//good 1      
        }

        return transform;

    }

    
}
