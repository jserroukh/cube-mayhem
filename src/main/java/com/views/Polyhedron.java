package views;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Polyhedron {
    private Polygon[] faces;
    public int count =0;

    public Polyhedron(Polygon[] faces) {
        if (faces.length < 4) { // polyèdre :au moins 4 faces
            throw new IllegalArgumentException("Un polyèdre doit avoir au moins 4 faces.");
        }
        this.faces = faces;
        }

    public Polygon[] getFaces() {
        return faces;
    }

    public void setFaces(Polygon[] faces) {
        if (faces.length < 4) {
            throw new IllegalArgumentException("Un polyèdre doit avoir au moins 4 faces.");
        }
        this.faces = faces;
    }

    public void afficherCoordonneesEtFaces() {
        String[] faceNames = {"Face 1", "Face 2", "Face 3", "Face 4", "Face 5", "Face 6"};
        for (int i = 0; i < faces.length; i++) {
            System.out.println(faceNames[i] + ":");
            for (Coord3D vertex : faces[i].getVertices()) {
                System.out.println("  " + vertex.getX()+"  " + vertex.getY()+"  " + vertex.getZ());
            }
            System.out.println("-"); // Séparateur entre les faces
        }
    }

    public boolean isFaceVisible(Polygon face, Coord3D cameraDirection){
        face.updateNormal();
        Coord3D faceNormal = face.getNormal();
        double prod = faceNormal.scalaireProduct(cameraDirection);
        // System.out.println("prod final "+prod);
        //System.out.println((count%6+1)+"\n");
        count++;
            Coord3D vertex = findPointNotInFace(face);
           // System.out.println("vertex :  "+(int) vertex.getX()+" "+(int) vertex.getY()+" "+(int) vertex.getZ()+"");
            Coord3D vertex2 = vertex.subVectors(face.getVertices()[0]);
            //System.out.println("vertex2 :  "+(int) vertex2.getX()+" "+(int) vertex2.getY()+" "+(int) vertex2.getZ()+"");

           // System.out.println(" norme avec le centre: "+faceNormal.scalaireProduct(vertex2));
            if (faceNormal.scalaireProduct(vertex2)<=0 ){
                prod = -prod;
            }
           //System.out.println("final normale: "+prod+"\n\n");
            if(prod>0){
                return true;
            }
        return false;
    }

    public Coord3D findPointNotInFace(Polygon face) {
        Set<Coord3D> faceVertices = new HashSet<>(List.of(face.getVertices())); 
    
        for (Polygon poly : faces) {
            if (!poly.equals(face)) { 
                for (Coord3D vertex : poly.getVertices()) {
                    if (!faceVertices.contains(vertex)) { 
                        return vertex;
                    }
                }
            }
        }
        return null; 
    }

    @Override
    public String toString() {
        return "Polyhedron{" + "faces=" + faces + '}';
    }
}
