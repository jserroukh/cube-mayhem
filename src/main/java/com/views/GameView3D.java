package views;

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;
import javax.xml.stream.events.StartDocument;

import model.Board;
import model.Cellule;
import model.Cube;
import model.Direction;
import model.GameEngine;
import model.GameState;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.HashMap;

import model.Board;
import model.Cube;
import model.TileType;

public class GameView3D extends JPanel {
    private Polyhedron cubeVisual;
    private Transform3D transform;
    private int angle = 180;
    private double posX = 0;
    private double posY = 0;
    private int speed = 1;
    private Timer timer;
    private boolean isWin = false;
    private boolean isLoosed= false;

    private double hauteur = 0;
    private double visualScale = 0.7;
    private double gap = Math.sqrt(2)*160*visualScale/2;
    
    Board board;
    Cube cube;
    private Direction direction;
    private GameEngine gameEngine;

    private Image flowerTile;
    private Image flecheTile;
    private Image finishTile;
    private Image SlideTile;
    private Image win;
    private Image ratio;
    private Image rotator;
    private Image portal;

    /**
     * Constructeur de GameView en 3D
     */
    public GameView3D(Cube cube, Board board, GameEngine gameEngine) {
        JFrame frame = new JFrame("Cube 3D avec Images");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true); // Supprime barre, titre / boutton, en haut,
        // utiles plus tard (une fois l'interface propre)
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(1024, 748);
        frame.setIconImage(new ImageIcon("assets\\imgs\\Bleu.png").getImage());
        frame.add(this);
        frame.setVisible(true);


        // Chargement de l'image
        flowerTile = new ImageIcon("assets\\imgs\\Gris.png")
                .getImage();
        flecheTile = new ImageIcon("assets\\imgs\\FlecheHaut.png")
                .getImage();
        finishTile = new ImageIcon("assets\\imgs\\Finish.png")
                .getImage();
        SlideTile = new ImageIcon("assets\\imgs\\Slide.png")
                .getImage();
        win = new ImageIcon("assets\\imgs\\WIN.png")
                .getImage();
        ratio = new ImageIcon("assets\\imgs\\RATIO.png")
                .getImage();
        portal = new ImageIcon("assets\\imgs\\Portal.png")
                .getImage();
        rotator = new ImageIcon("assets\\imgs\\Rotator.png")
                .getImage();

        this.cube = cube;
        posX = 0;// cube.getX();
        posY = 0;//cube.getY();
        this.board = board;
        this.gameEngine = gameEngine;



        // Définition des sommets du cube
        Coord3D p1 = new Coord3D(0, 0, 0);
        Coord3D p2 = new Coord3D(1, 0, 0);
        Coord3D p3 = new Coord3D(1, 1, 0);
        Coord3D p4 = new Coord3D(0, 1, 0);
        Coord3D p5 = new Coord3D(0, 0, 1);
        Coord3D p6 = new Coord3D(1, 0, 1);
        Coord3D p7 = new Coord3D(0, 1, 1);
        Coord3D p8 = new Coord3D(1, 1, 1);

        // Faces du cube
        Coord3D[] vertices = { p4, p3, p2, p1 };
        Polygon face1 = new Polygon(vertices);
        Coord3D[] vertices2 = { p5, p6, p8, p7 };
        Polygon face2 = new Polygon(vertices2);
        Coord3D[] vertices3 = { p1, p2, p6, p5 };
        Polygon face3 = new Polygon(vertices3);
        Coord3D[] vertices4 = { p2, p3, p8, p6 };// p8 possible troisieme
        Polygon face4 = new Polygon(vertices4);
        Coord3D[] vertices5 = { p3, p4, p7, p8 };
        Polygon face5 = new Polygon(vertices5);
        Coord3D[] vertices6 = { p4, p1, p5, p7 };// p7 possible dernier
        Polygon face6 = new Polygon(vertices6);

        Polygon[] faces = { face1, face2, face3, face4, face5, face6 };
        cubeVisual = new Polyhedron(faces);

        // Initialisation de la transformation
        transform = new Transform3D(new double[][] {
                { 1, 0, 0, 0 },
                { 0, 1, 0, 0 },
                { 0, 0, 1, 0 },
                { 0, 0, 0, 1 }
        });

        nextMove();
 
    }

    @Override
    protected void paintComponent(Graphics g) {
        //System.out.println(posX+" "+posY+" logique: "+cube.getX()+" "+cube.getY());

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Centrer le cube
        int width = getWidth();
        int height = getHeight();
        g2d.translate(width / 2, height / 4);

        createMap1(g2d);

        double[][] rotationX1 = transform.createRotationMatrixX(45);
        double[][] rotationY1 = transform.createRotationMatrixY(-45);
        double[][] scale = transform.createScaleMatrix(visualScale);

        double[][] rotationX = transform.createRotationMatrixX(-angle);
        double[][] rotationXNorth = transform.createRotationMatrixX(angle);
        double[][] rotationZ = transform.createRotationMatrixZ(-angle);
        double[][] rotationZWest = transform.createRotationMatrixZ(angle);

        /*
        pour deplace le cube d'un cran vers la droite, on utilise le theoreme de pythagore car le plan est tournée
        (vue isometrique) :
        racine carre de (x²+x²) = taille de l'image ici 160 * scale
        donc x= 56.57

        Pour faire tourner le cube nous le faisons tourner autour de l'axe des x ou l'axe des y pour qu'il aille dans un sens ou dans l'autre en changeant seulement
        l'angle de la transformation, il est donc nécessaire de parfois le déplacer d'un cran vers la gauche ou la droite pour qu'ils soient bien au bon endroit
        La multitude de transformation de translation permet de faire ses décalages d'un cran.
        */

        double[][] translationEA = transform.createTranslationMatrixX(  gap * posX,hauteur + gap * posX, 0);
        double[][] translationEA2 = transform.createTranslationMatrixX(  -gap * posY - gap,hauteur + gap * posY - gap, 0);
        double[][] translationEA3 = transform.createTranslationMatrixX(  -gap * posY ,hauteur  + gap * posY , 0);
        double[][] translation = transform.createTranslationMatrixX(  gap * posX + gap,hauteur + gap * posX - gap, 0);
        double[][] translation2 = transform.createTranslationMatrixX( -gap * posY,hauteur + gap * posY, 0);
        
        //System.out.println(posX+"  "+posY);

        

        Transform3D transformCord = new Transform3D(scale);
        

        if (this.direction == Direction.WEST){
            transformCord.compose(rotationX);
            transformCord.compose(translationEA);
            transformCord.compose(translationEA2);    

        }else if (this.direction == Direction.EAST){
            transformCord.compose(rotationXNorth);
            transformCord.compose(translationEA);
            transformCord.compose(translationEA3); //

        }else if (this.direction == Direction.SOUTH){
            transformCord.compose(rotationZ);
            transformCord.compose(translationEA); 
            transformCord.compose(translation2);

        }else if ( this.direction == Direction.NORTH) {
            transformCord.compose(rotationZWest);       
            transformCord.compose(translation);//
            transformCord.compose(translation2);
        }
      
        // les transformation pour mettre en isometrique ne sont pas a compose ici
        // (rotationX1/rotationY1/toCube)

        // cube.afficherCoordonneesEtFaces();

        Polyhedron transformedPolyhedron = transformCord.applyToCube(cubeVisual);

        for (int i = 0; i < transformedPolyhedron.getFaces().length; i++) {
            Polygon face = transformedPolyhedron.getFaces()[i];
            if (transformedPolyhedron.isFaceVisible(face, new Coord3D(-1, -1, -1))) {
                // refaire avec toutes les transfos (en fonction de la direction)
                transformCord = new Transform3D(rotationX1);
                
                
                
                if (this.direction == Direction.WEST){
                    transformCord.compose(translationEA);
                    transformCord.compose(translationEA2);
                    transformCord.compose(rotationY1);
                    transformCord.compose(rotationX);
                    
                }else if (this.direction == Direction.EAST) {
                    transformCord.compose(translationEA);
                    transformCord.compose(translationEA3);
                    transformCord.compose(rotationY1);
                    transformCord.compose(rotationXNorth);                   
                }
                else if (this.direction == Direction.SOUTH) {
                    transformCord.compose(translationEA);
                    transformCord.compose(translation2);
                    transformCord.compose(rotationY1);
                    transformCord.compose(rotationZ);                   
                }
                else if ( this.direction == Direction.NORTH) {
                    transformCord.compose(translation);
                    transformCord.compose(translation2);
                    transformCord.compose(rotationY1);
                    transformCord.compose(rotationZWest);     
                }
                transformCord.compose(scale);

                transformCord.toCube(i, null, transformCord);// place les images sur les faces du cube

                AffineTransform transformAffine = transformCord.toTransform2D();
                g2d.drawImage(flowerTile, transformAffine, null);

            }
        }

        createMap2(g2d);
        
        if(isWin) g2d.drawImage(win, -getWidth()/4, 0, this);
        if(isLoosed)
            g2d.drawImage(ratio, -getWidth()/4, 0, this);

        
    }

    public void createMap1(Graphics2D g2d) {
        if (isLoosed) {
            createMap(g2d, 0, (int) posX+1,0,(int) posY,0);
        }else createMap(g2d, 0, board.getSize(),0, board.getSize(),0);
    }
    public void createMap2(Graphics2D g2d) {
        if (isLoosed) createMap(g2d, (int) posX,  board.getSize(),(int)posY, board.getSize(),1);
    }

    public void createMap(Graphics2D g2d,int debutNS, int finNS,int debutEW, int finEW,int t) {

        Cellule[][] grid = board.getGrid();
       
        int debut = 0;
        int fin = board.getSize();

        for (int k = debutNS; k < finNS; k++) {
            if (k == finNS-1 && t==0) {
                fin =finEW;
                debut = debutEW;
            }else if (k!=debutNS && t==1) {
                fin =board.getSize();;
                debut = 0;
            }else if (k==debutNS && t==1) {
                fin =finEW;
                debut = debutEW;
            }
            for (int j = debut; j <fin; j++) {

                double[][] rotationX1 = transform.createRotationMatrixX(45);
                double[][] rotationY1 = transform.createRotationMatrixY(-45);
                double[][] scale = transform.createScaleMatrix(visualScale);

                double[][] translation = transform.createTranslationMatrixX(gap * k, gap * k, 0);
                double[][] translation2 = transform.createTranslationMatrixX(-gap * j, gap * j, 0);

                for (int i = 0; i < cubeVisual.getFaces().length; i++) {
                    if (i == 1 || i == 3 || i == 4) {
                        Transform3D transformCord = new Transform3D(rotationX1);
                        transformCord.compose(translation);
                        transformCord.compose(translation2);
                        transformCord.compose(rotationY1);
                        transformCord.compose(scale);

                        transformCord.toCube(i, null, transformCord); // place les images sur les faces du cube

                        AffineTransform transformAffine = transformCord.toTransform2D();

                        if (grid[k][j].getTile() == TileType.ARROW){
                            if (i == 4){//l'image 4 et celle du dessus 
                                if(grid[k][j].getTileDirection() == Direction.NORTH){ // retourne l'image en fonction de la direction
                                    AffineTransform transform = new AffineTransform();
                                    transform.rotate(Math.toRadians(90));
                                    transform.translate(0, -160);
                                    transformAffine.concatenate(transform);
                                }
                                else if(grid[k][j].getTileDirection() == Direction.WEST){
                                    AffineTransform transform = new AffineTransform();
                                    transform.rotate(Math.toRadians(180));
                                    transform.translate(-160, -160);
                                    transformAffine.concatenate(transform);
                                
                                }
                                else if(grid[k][j].getTileDirection() == Direction.SOUTH){
                                    AffineTransform transform = new AffineTransform();
                                    transform.rotate(Math.toRadians(270));
                                    transform.translate(-160, 0);
                                    transformAffine.concatenate(transform);
                                
                                }
                                g2d.drawImage(flecheTile, transformAffine, null);
                            } 
                            else g2d.drawImage(flowerTile, transformAffine, null);
                        
                        }
                        else if (grid[k][j].getTile() == TileType.SLIDE){
                            if (i == 4){//l'image 4 et celle du dessus 
                                if(grid[k][j].getTileDirection() == Direction.NORTH){ // retourne l'image en fonction de la direction
                                    AffineTransform transform = new AffineTransform();
                                    transform.rotate(Math.toRadians(90));
                                    transform.translate(0, -160);
                                    transformAffine.concatenate(transform);
                                }
                                else if(grid[k][j].getTileDirection() == Direction.WEST){
                                    AffineTransform transform = new AffineTransform();
                                    transform.rotate(Math.toRadians(180));
                                    transform.translate(-160, -160);
                                    transformAffine.concatenate(transform);
                                
                                }
                                else if(grid[k][j].getTileDirection() == Direction.SOUTH){
                                    AffineTransform transform = new AffineTransform();
                                    transform.rotate(Math.toRadians(270));
                                    transform.translate(-160, 0);
                                    transformAffine.concatenate(transform);
                                
                                }
                                g2d.drawImage(SlideTile, transformAffine, null);
                            } 
                        }
                            else if (grid[k][j].getTile() == TileType.ROTATOR){
                             if (i == 4){//l'image 4 et celle du dessus 
                                if(grid[k][j].getTileDirection() == Direction.NORTH){ // retourne l'image en fonction de la direction
                                    AffineTransform transform = new AffineTransform();
                                    transform.rotate(Math.toRadians(90));
                                    transform.translate(0, -160);
                                    transformAffine.concatenate(transform);
                                }
                                else if(grid[k][j].getTileDirection() == Direction.WEST){
                                    AffineTransform transform = new AffineTransform();
                                    transform.rotate(Math.toRadians(180));
                                    transform.translate(-160, -160);
                                    transformAffine.concatenate(transform);
                                
                                }
                                else if(grid[k][j].getTileDirection() == Direction.SOUTH){
                                    AffineTransform transform = new AffineTransform();
                                    transform.rotate(Math.toRadians(270));
                                    transform.translate(-160, 0);
                                    transformAffine.concatenate(transform);
                                
                                }
                                g2d.drawImage(rotator, transformAffine, null);
                            } 
                            else g2d.drawImage(flowerTile, transformAffine, null);
                        
                            }
                            else if (grid[k][j].getTile() == TileType.PORTAL){
                                if (i == 4){//l'image 4 et celle du dessus 
                                    g2d.drawImage(portal, transformAffine, null);
                                } 
                                else g2d.drawImage(flowerTile, transformAffine, null);
                            
                            }
                            else if (grid[k][j].getTile() == TileType.FINISH){
                                if (i==4) {
                                    g2d.drawImage(finishTile, transformAffine, null);
                                }
                                else g2d.drawImage(flowerTile, transformAffine, null);

                            }
                            else if (grid[k][j].getTile() != TileType.FORBIDDEN) {
                                g2d.drawImage(flowerTile, transformAffine, null);

                            }
                    }
                }
            }
         }
        
    }
    

    public void makeATurn(Direction dir) {
        direction = dir;
       
            if (this.direction == Direction.WEST){
                angle = 180;
                timer = new Timer(20/speed, e -> {
                angle += 3;
                
                    if (angle >= 270) {   
                        timer.stop();
                        posX--;
                        nextMove();
                    }
                    repaint();

                });
            }else if (this.direction == Direction.EAST){
                angle = 90;
                timer = new Timer(20/speed, e -> {
                angle += 3;
                
                    if (angle >= 180) {   
                        timer.stop();
                        posX++;
                        nextMove();
                    }
                    repaint();
                    
                });
            }else if (this.direction == Direction.SOUTH){
                angle = 90;
                timer = new Timer(20/speed, e -> {
                angle += 3;
                
                    if (angle >= 180) {   
                        timer.stop();
                        posY++;
                        nextMove();
                    }
                    repaint();

                });
            }else if (this.direction == Direction.NORTH){
                angle = 180;
                timer = new Timer(20/speed, e -> {
                angle += 3;
                
                    if (angle >= 270) {   
                        timer.stop();
                        posY--;
                        nextMove();
                    }
                    repaint();

                });
            }
           
                
            
         
        timer.start();
      //  System.out.println(angle);
        
    }

    public void makeASlide(Direction dir, Direction lastDirection){
        
        double pas = 0.05;  
        double startX =posX;
        double startY =posY;
        switch(dir){
            case Direction.WEST :
                angle = 180;
                timer = new Timer(40/speed, e -> {
                    posX-=pas;
                    if (Math.abs(posX -startX) >= 1.0) {    //des que l'acart entre la position avant et apres mouvement est egal a 1, modulo l'imprecision des doubles
                        
                        timer.stop();
                        posX = Math.round(posX);                //on arrondi la position pour pas avoir de ,00001 
                        cube.setIsSliding(false);
                        cube.setCubeDirection(lastDirection);   //reprend l'ancienne direction car c'est un slider
                        nextMove();
                    }
                    repaint();
                });
            break;
            case Direction.EAST :
                angle =90;
                timer = new Timer(40/speed, e -> {
                    posX+=pas;
                    if (Math.abs(posX -startX) >= 1.0) {  
                        
                        timer.stop();
                        posX = Math.round(posX);
                        cube.setIsSliding(false);
                        cube.setCubeDirection(lastDirection);
                        nextMove();
                    }
                    repaint();
                });
            break;
            case Direction.SOUTH :
                angle =90;
                timer = new Timer(40/speed, e -> {
                    posY+=pas;
                    if (Math.abs(posY -startY) >= 1.0) { 
                        
                        timer.stop();
                        posY = Math.round(posY);
                        cube.setIsSliding(false);
                        cube.setCubeDirection(lastDirection);
                        nextMove();
                    }
                    repaint();
                });
            break;
            case Direction.NORTH :
                angle = 180;
                timer = new Timer(40/speed, e -> {
                    posY-=pas;
                    if (Math.abs(posY -startY) >= 1.0) { 
                         
                        timer.stop();
                        posY = Math.round(posY);
                        cube.setIsSliding(false);
                        cube.setCubeDirection(lastDirection);
                        nextMove();

                    }
                    repaint();
                });
            break;
        } 
        timer.start();
     }

    public void makeATp(Direction dir){
        posX=cube.getX();
        posY=cube.getY();
        cube.setIsTping(false);

        nextMove();
    }

    public void nextMove(){
       
        if (!gameEngine.getGameState().isWinned() && !gameEngine.getGameState().isLosed()){

            Direction lastDirection = direction;
            gameEngine.move();                      //fait le prochain mouvement de la logique du cube
            direction = cube.getCubeDirection();    //recupere la nouvelle direction
            
            boolean isCubeSliding = cube.getIsSliding();   
            boolean isTping = cube.getTping();        
            if(isCubeSliding){
                makeASlide(direction,lastDirection);
            }else if(isTping){
                makeATp(direction); 
            }else makeATurn(direction);



        }else if (gameEngine.getGameState().isWinned()){
            isWin =true;
            posX--; //en fonction de direction final ( car un mouvemtent de trop )
            timer = new Timer(5, e -> {
                hauteur-=3;
                if (hauteur <=-1000) { 
                    timer.stop();
                }
                repaint();
            });
            timer.start();
        }else{
            isLoosed = true;
            timer = new Timer(5, e -> {
                hauteur+=3;
                if (hauteur >=1000) { 
                    timer.stop();
                }
                repaint();
            });
            timer.start();
        }
    }
      
   
 

}
