# Analyse repo github
Projet dans le cadre du cours IFT3913.<br />
Le but de ce projet est de créer une application qui calcule les métriques mWMC (moyenne des complexités cyclomatiques de McCabe sur toutes les classes) et mcBC (moyenne de DC / WMC, avec DC étant la densité de commentaire en terme de lignes) pour différent commit d'un repo github.

# Guide d'utilisation 
Notes générales

    - La versions "First Version" est uniquement fonctionnel sur Windows et utilise seulement des exec.
    - La version "Official Version" est compatible sur Windows/Linux/MacOs et utilise JGit.
    - Les fichiers proto.jar, IFT3913_TP1.jar et config.properties doivent se trouver dans le même dossier pour l'éxecution (valide pour les 2 versions)
    - Pour exécuter :
          java -jar proto.jar <url du repo>


Config.properties

    - root : utilisé par IFT3913_TP1.jar (sans importance)
    - javaPath : spécifier le chemin de Java s'il y a une erreur de version lors de l'éxecution
    - sampleSize : taille de l'échantillon à analyser dans l'historique des commits



