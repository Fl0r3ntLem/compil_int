# Compilateur PCF - WASM, version finale

## Auteurs
- Florent LEMOINE
- Théo GRANIER

## Choix et déviations par rapport aux préconisations du TP
-  Tests à partir des noms de fichier avec test/wat/test_wat.sh et l'argument ```name``` dans src/generator/Generator

## Ce qui marche
- Parser et construction de l'AST
- Interpréteur
- Typage (? à cause de red19)
- Generator
- Génération de code WAT
- VM
- Exécution des .wat grâce à wat2wasm

## Ce qui ne marche pas / manque + les prochaines étapes
- Verbose -> rendre fonctionnel + ajouter des messages
- Test Red19 -> regarder le typer (le suspect n°1 a priori)

## Utilisation de l'IA
- L'autocomplétion, quand pertinent
- Questions spécifiques aux fonctionnalités Scala
