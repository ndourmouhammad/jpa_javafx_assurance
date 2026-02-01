# Gestion des Assurances (JavaFX + JPA)

Ce projet est une application de bureau JavaFX permettant de gérer des contrats d'assurance. Elle utilise JPA (Hibernate) pour la persistance des données dans une base de données PostgreSQL.

## Fonctionnalités

- Gestion (CRUD) des assurances (Ajout, Modification, Suppression, Affichage).
- Filtrage et recherche en temps réel.
- Interface utilisateur réactive et moderne.
- Persistance des données via PostgreSQL.

## Prérequis

Avant de commencer, assurez-vous d'avoir installé :
- **Java JDK 17** ou supérieur.
- **Maven 3.6+**.
- **PostgreSQL** (installé et en cours d'exécution).

## Installation et Configuration

### 1. Cloner le projet

```bash
git clone https://github.com/ndourmouhammad/jpa_javafx_assurance.git
cd javafx_jpa_assurance
```

### 2. Configuration de la base de données

1. Connectez-vous à votre instance PostgreSQL.
2. Créez une base de données nommée `jpa_javafx_g1_assurance` :
   ```sql
   CREATE DATABASE jpa_javafx_g1_assurance;
   ```
3. Vérifiez la configuration dans le fichier `src/main/resources/META-INF/persistence.xml`.
   - Par défaut, l'utilisateur est `postgres` et le mot de passe est `root`.
   - Modifiez ces valeurs si nécessaire :
     ```xml
     <property name="javax.persistence.jdbc.user" value="postgres" />
     <property name="javax.persistence.jdbc.password" value="root" />
     ```

### 3. Compilation et Installation

Utilisez Maven pour installer les dépendances et compiler le projet :

```bash
mvn clean install
```

## Exécution de l'application

Pour lancer l'application, vous pouvez utiliser le plugin Maven JavaFX :

```bash
mvn javafx:run
```

Ou via la classe `Launcher` si vous utilisez un IDE (IntelliJ IDEA, Eclipse) :
`org.example.javafx_jpa_assurance.Launcher`

## Structure du projet

- `src/main/java`: Contient le code source Java (Entités, Référentiels, Contrôleurs).
- `src/main/resources`: Contient les fichiers de configuration (Fxml, persistence.xml).
- `pom.xml`: Fichier de configuration Maven.
- `README.md`: Ce fichier contient la documentation du projet.
