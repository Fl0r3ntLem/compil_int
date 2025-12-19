# Compilateur PCF - WASM, version finale

## Auteurs
- Florent LEMOINE
- Théo GRANIER

## Choix et déviations par rapport aux préconisations du TP
-  Tests à partir des noms de fichier avec test/wat/test_wat.sh et l'argument ```name``` dans src/generator/Generator (nécessite des petits changement dans le code, actuellement on utilise les points d'entrées du package src/test)

## Ce qui marche
- Parser et construction de l'AST
- Interpréteur
- Typage
- Generator
- Génération de code WAT
- VM
- Exécution des .wat grâce à wat2wasm

## Ce qui ne marche pas / manque
- Éventuellement, un point d'entrée dans src/pcf/PCF avec ```@main``` pour tester des expressions plus "personnalisées"

## Utilisation de l'IA
- L'autocomplétion, quand pertinent
- Questions spécifiques aux fonctionnalités Scala
