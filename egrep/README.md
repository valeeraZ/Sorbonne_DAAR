# Description
Ce répertoire contient le code source pour le projet 
"Clone de `egrep` avec support partiel des ERE".

# Exemple
![](https://dev.azure.com/zslyvain/9285f0e6-8055-4a5c-aec3-50d9555ac078/_apis/git/repositories/4eb461c6-bb1f-489f-978b-686e8c32decf/items?path=%2F1633464363989_8655.png&versionDescriptor%5BversionOptions%5D=0&versionDescriptor%5BversionType%5D=0&versionDescriptor%5Bversion%5D=master&resolveLfs=true&%24format=octetStream&api-version=5.0)

# Utilisation

## Utilisez un IDE
Vous pouvez utilser Eclipse/Intellij IDEA qui dispose des plugins Ant pour créer l'archive en `jar` et exécuter le programme.  
* Remplissez le champ "arguments of application" avec `<ERE-pattern> <chemin-de-fichier>` dont
    * `<ERE-pattern>` est une expression régulière de la norme ERE
    * `<chemin-de-fichier>` est le chemin relatif du fichier


## Compiler et exécuter par CLI
* Munissez-vous de l'outil `Ant` et JDK (version >= 1.8)
* Utilsez `ant dist` pour créer l'archive en `jar` dans le répertoire `build`
* `cd build` 
* `java -jar egrep.jar <ERE-pattern> <chemin-de-fichier>` pour exécuter le programme dont les 2 arguments sont utilisés pareillement comme dans la partie précédente. 

