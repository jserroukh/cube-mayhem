package views;

import java.util.List;

public class Polygon {
    private Coord3D[] vertices;
    private Coord3D normal;

    public Polygon(Coord3D[] vertices) {
        if (vertices.length < 3) {
            throw new IllegalArgumentException("Un polygone doit avoir au moins trois sommets.");
        }
        this.vertices = vertices;
    }

    public Coord3D[]  getVertices() {
        return vertices;
    }

    public Coord3D getNormal(){
        return normal;
    }

    public void setVertices(Coord3D[] vertices) {
        if (vertices.length< 3) {
            throw new IllegalArgumentException("Un polygone doit avoir au moins trois sommets.");
        }
        this.vertices = vertices;
    }

    public void updateNormal(){
        Coord3D A = vertices[0];
        Coord3D B = vertices[1];
        Coord3D C = vertices[2];
        // System.out.println("a "+(int) A.getX()+" "+(int) A.getY()+" "+(int) A.getZ()+"");
        // System.out.println("b "+(int)B.getX()+" "+(int)B.getY()+" "+(int)B.getZ()+"");
        // System.out.println("c "+(int)C.getX()+" "+(int)C.getY()+" "+(int)C.getZ()+"");

        Coord3D v1 =  A.subVectors(B);
        Coord3D v2 =  C.subVectors(B);
        //  System.out.println("v1 "+(int)v1.getX()+" "+(int)v1.getY()+" "+(int)v1.getZ()+"");
        //  System.out.println("v2 "+(int)v2.getX()+" "+(int)v2.getY()+" "+(int)v2.getZ()+"\n");

        Coord3D prodVect = v1.vectorProduct(v2);
        //  System.out.println("produit:  "+(int)prodVect.getX()+" "+(int)prodVect.getY()+" "+(int)prodVect.getZ());
        double norme = prodVect.norme();

        this.normal = prodVect.externProduct(1/norme);
        //  System.out.println("nroamle:  "+(int)normal.getX()+" "+(int)normal.getY()+" "+(int)normal.getZ()+"");
    }

}
