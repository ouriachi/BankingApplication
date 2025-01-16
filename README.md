Objectif :
Création d’une application bancaire capable de gérer des comptes, y compris des comptes d’épargne avec un taux d’intérêt de 5 %.

Fonctionnalités demandées :
Création de comptes :

Permettre la création d’un compte (courant ou épargne) en saisissant un nom, un solde initial et un type de compte. Chaque compte doit avoir un identifiant unique.
Dépôt :

Permettre le dépôt d’argent sur un compte via son identifiant. Le montant est ajouté au solde.
Retrait :

Permettre le retrait d’argent via l’identifiant du compte :
Pour un compte courant : empêcher le solde négatif.
Pour un compte d’épargne : vérifier que le retrait ne dépasse pas un plafond (ex. : 1000 euros par mois).
Affichage du solde :

Permettre de consulter le solde d’un compte via son identifiant.
Calcul des intérêts (comptes d’épargne uniquement) :

Calculer et ajouter mensuellement un intérêt fixe de 5 % au solde.
Gestion des erreurs :

Gérer les cas d’erreur (compte inexistant, solde insuffisant, dépassement des plafonds de retrait) avec des messages appropriés.
Tests unitaires :

Vérifier le bon fonctionnement des méthodes via des tests unitaires.


Technologies utilisées : 

JAVA 17 , Spring Boot 3.1.4 , Lombok, Maven , Spring Boot Test
