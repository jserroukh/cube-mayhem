package views;

import javax.swing.*;

public class TestCube extends JPanel {

    // private Polyhedron cube;
    // private Transform3D transform;
    // private double angle = 0;
    // public TestCube() {
    //     // Définition des sommets du cube
    //     Coord3D p1 = new Coord3D(-1, -1, -1);
    //     Coord3D p2 = new Coord3D(1, -1, -1);
    //     Coord3D p3 = new Coord3D(1, 1, -1);
    //     Coord3D p4 = new Coord3D(-1, 1, -1);
    //     Coord3D p5 = new Coord3D(-1, -1, 1);
    //     Coord3D p6 = new Coord3D(1, -1, 1);
    //     Coord3D p7 = new Coord3D(1, 1, 1);
    //     Coord3D p8 = new Coord3D(-1, 1, 1);
    //     // Définition des faces du cube
    //     Polygon face1 = new Polygon(Arrays.asList(p1, p2, p3, p4));
    //     Polygon face2 = new Polygon(Arrays.asList(p5, p6, p7, p8));
    //     Polygon face3 = new Polygon(Arrays.asList(p1, p2, p6, p5));
    //     Polygon face4 = new Polygon(Arrays.asList(p2, p3, p7, p6));
    //     Polygon face5 = new Polygon(Arrays.asList(p3, p4, p8, p7));
    //     Polygon face6 = new Polygon(Arrays.asList(p4, p1, p5, p8));
    //     cube = new Polyhedron(Arrays.asList(face1, face2, face3, face4, face5, face6));
    //     // Matrice identité pour commencer
    //     transform = new Transform3D(new double[][]{
    //         {1, 0, 0, 0},
    //         {0, 1, 0, 0},
    //         {0, 0, 1, 0},
    //         {0, 0, 0, 1}
    //     });
    //     // Timer
    //     Timer timer = new Timer(10, e -> {
    //         angle += Math.toRadians(1);
    //         repaint();
    //     });
    //     timer.start();
    // }
    // @Override
    // protected void paintComponent(Graphics g) {
    //     super.paintComponent(g);
    //     Graphics2D g2d = (Graphics2D) g;
    //     g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    //     double[][] rotationY = {
    //         {Math.cos(angle), 0, Math.sin(angle), 0},
    //         {0, 1, 0, 0},
    //         {-Math.sin(angle), 0, Math.cos(angle), 0},
    //         {0, 0, 0, 1}
    //     };
    //     transform.setMatrice(rotationY);
    //     int width = getWidth();
    //     int height = getHeight();
    //     g2d.translate(width / 2, height / 2);
    //     // Dessine faces
    //     for (Polygon face : cube.getFaces()) {
    //         Path2D path = new Path2D.Double();
    //         boolean first = true;
    //         for (Coord3D vertex : face.getVertices()) {
    //             // Appliquer transformation 3D
    //             Coord3D transformedVertex = transform.apply(vertex);
    //             Point2D.Double projected = Coord3D.projectIsometric(transformedVertex);
    //             double scale = 50;
    //             double x2D = projected.x * scale;
    //             double y2D = -projected.y * scale; // Inverser Y pour correspondre à l'écran
    //             if (first) {
    //                 path.moveTo(x2D, y2D);
    //                 first = false;
    //             } else {
    //                 path.lineTo(x2D, y2D);
    //             }
    //         }
    //         path.closePath();
    //         g2d.drawImage(null, null, getFocusCycleRootAncestor());
    //         g2d.setColor(new Color(100, 100, 255, 100)); // Semi-transparent
    //         g2d.fill(path);
    //         g2d.setColor(Color.BLACK);
    //         g2d.draw(path);
    //     }
    // }
    // public static void main(String[] args) {
    //     JFrame frame = new JFrame("Cube 3D avec Swing");
    //     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //     frame.setSize(600, 600);
    //     frame.add(new TestCube());
    //     frame.setVisible(true);
    // }
}
