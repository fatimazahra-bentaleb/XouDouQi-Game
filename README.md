Xou Dou Qi - Jeu de la Jungle 🐘

Un jeu de stratégie traditionnel chinois également connu sous le nom de "Jungle Game" ou "Dou Shou Qi", implémenté en Java avec interface console.
📋 Description
Xou Dou Qi (鬥獸棋) est un jeu de plateau stratégique chinois souvent appelé "les échecs des animaux". Deux joueurs s'affrontent avec des pièces représentant différents animaux hiérarchisés, l'objectif étant d'atteindre le sanctuaire adverse.
🎮 Fonctionnalités
Core Game

Logique de jeu complète : Implémentation des règles officielles du Xou Dou Qi
Interface console intuitive : Commandes textuelles simples et claires
Gestion automatique des tours : Alternance automatique entre les deux joueurs
Validation des mouvements : Vérification de la légalité de tous les coups

Gestion des joueurs

Système de comptes : Création et connexion des joueurs
Base de données embarquée : Stockage persistant avec H2
Historique des parties : Sauvegarde et consultation des résultats précédents
Statistiques : Suivi des victoires, défaites et scores

🎯 Règles du jeu
Plateau

Dimensions : 9×7 cases
Éléments spéciaux :

2 lacs au centre
2 sanctuaires (un par joueur)
6 cases pièges (3 par joueur)



Pièces et hiérarchie
Chaque joueur dispose de 8 animaux classés par force :

🐘 ÉLÉPHANT (le plus fort)
🦁 LION
🐅 TIGRE
🐆 PANTHÈRE
🐕 CHIEN
🐺 LOUP
🐱 CHAT
🐭 RAT (le plus faible)

Règles de capture

Une pièce peut capturer une pièce de rang égal ou inférieur
Exception : Le RAT peut capturer l'ÉLÉPHANT
Les captures se font par substitution (pas en diagonale)

Mouvements spéciaux

LION et TIGRE : Peuvent sauter par-dessus les lacs (sauf si un rat bloque le passage)
RAT : Seule pièce pouvant nager dans les lacs
Cases pièges : Les pièces ennemies deviennent vulnérables à toutes les captures

Condition de victoire
Le premier joueur à faire entrer une de ses pièces dans le sanctuaire adverse remporte la partie.
🛠️ Technologies utilisées

Java : Langage principal
H2 : Base de données embarquée
Console/Terminal : Interface utilisateur
