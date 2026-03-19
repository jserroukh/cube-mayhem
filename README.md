# Cube Mayhem

Jeu de stratégie au tour par tour en Java, avec un moteur 3D développé from scratch sans bibliothèque externe.

## Description

Le joueur place des tuiles sur une grille pour guider un cube vers son objectif. Les tuiles interagissent entre elles et créent des chaînes de réactions, rendant le jeu à la fois tactique et dynamique.

## Fonctionnalités

- Moteur de rendu 3D from scratch via AffineTransform et matrices 4x4
- Visuel 2D et 3D disponibles (même modèle, deux vues)
- Éditeur de niveaux — création et export de niveaux en JSON
- Architecture MVC stricte (GameState, Board, Cube, GameEngine)
- Animations du cube (translation, rotation)
- Calcul de visibilité des faces cachées (produit vectoriel, normale)

## Technologies

- Java 22
- JavaFX / Swing
- Jackson (parsing JSON)
- Architecture MVC

## Lancement
```bash
java -jar Mayhem.jar
```

Nécessite Java 22 ou supérieur.

## Architecture
```
src/
├── model/       # Logique du jeu (GameState, Board, Cube, GameEngine)
├── view/        # Affichage 2D et 3D (GamePanel, GameView3D)
├── controller/  # Gestion des entrées et boucle de jeu
└── assets/      # Ressources graphiques
```

## Auteurs

SERROUKH Jabir — Université Paris Cité