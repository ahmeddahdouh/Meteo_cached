# Météo Cahed 🌦️

**Météo Cahed** est une application météo pour Android, développée en utilisant les dernières technologies recommandées par Google. Elle permet aux utilisateurs de rechercher la météo pour n'importe quelle ville, de gérer une liste de favoris et d'obtenir les conditions météo pour leur position actuelle.

## ✨ Fonctionnalités

- **Recherche de villes** : Trouvez n'importe quelle ville dans le monde en temps réel.
- **Météo détaillée** : Obtenez les conditions actuelles, les températures minimales/maximales et la vitesse du vent.
- **Gestion des favoris** : Ajoutez vos villes préférées à une liste pour un accès rapide et facile.
- **Géolocalisation** : Affichez la météo pour votre position actuelle.
- **Thème dynamique** : L'interface s'adapte automatiquement entre le jour et la nuit, avec des palettes de couleurs et des animations fluides.
- **Interface moderne** : Entièrement construite avec Jetpack Compose pour une interface utilisateur déclarative et réactive.

## 🏛️ Architecture

Ce projet suit les principes de la **Clean Architecture** et utilise le pattern **MVVM (Model-View-ViewModel)**.

- **UI (Compose)** : Affiche les données et envoie les événements utilisateur au ViewModel. Les écrans sont des composables qui réagissent aux changements d'état.
- **ViewModel** : Contient la logique de l'interface utilisateur, prépare et gère l'état pour l'UI via des `StateFlow`.
- **Use Cases (Cas d'utilisation)** : Contiennent la logique métier de l'application (par exemple, `GetWeatherForecastUseCase`). Ils sont invoqués par les ViewModels.
- **Repository** : Centralise la gestion des données et abstrait les sources de données. Le `WeatherRepository` décide s'il faut récupérer les données depuis le réseau ou la base de données locale.
- **Data Sources** : Gèrent la communication directe avec l'API distante (avec Retrofit) et la base de données locale (avec Room et DataStore).

## 🛠️ Technologies et Bibliothèques

- **Langage** : [Kotlin](https://kotlinlang.org/)
- **UI** : [Jetpack Compose](https://developer.android.com/jetpack/compose) - Pour la construction de l'interface utilisateur native.
- **Architecture** : 
    - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Pour gérer l'état de l'UI.
    - [Coroutines & Flow](https://kotlinlang.org/docs/coroutines-overview.html) - Pour la programmation asynchrone.
- **Navigation** : [Navigation Compose](https://developer.android.com/jetpack/compose/navigation) - Pour la navigation entre les écrans.
- **Réseau** : 
    - [Retrofit](https://square.github.io/retrofit/) - Pour les appels à l'API REST.
    - [OkHttp](https://square.github.io/okhttp/) - Pour l'inspection du trafic réseau.
- **Sérialisation JSON** : [Kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) - Pour convertir les objets Kotlin en JSON et vice-versa.
- **Stockage local** :
    - [Room](https://developer.android.com/training/data-storage/room) - Pour la gestion de la base de données (villes favorites).
    - [Jetpack DataStore](https://developer.android.com/topic/libraries/architecture/datastore) - Pour le stockage de préférences simples (IDs des favoris).
- **Chargement d'images** : [Coil](https://coil-kt.github.io/coil/) - Pour charger et afficher les images des villes.
- **Géolocalisation** : [Google Play Services Location](https://developer.android.com/training/location) - Pour obtenir la position de l'utilisateur.

## 🚀 Installation

Pour compiler et exécuter le projet, suivez ces étapes :

1.  **Clonez le dépôt** :
    ```bash
    git clone https://github.com/votre-utilisateur/meteo_cahed.git
    ```
2.  **Ouvrez dans Android Studio** :
    - Ouvrez la dernière version d'[Android Studio](https://developer.android.com/studio).
    - Sélectionnez `File > Open` et naviguez jusqu'au dossier du projet cloné.

3.  **Synchronisez Gradle** :
    - Laissez Android Studio télécharger et synchroniser les dépendances Gradle.

4.  **Exécutez l'application** :
    - Sélectionnez un émulateur ou un appareil physique et cliquez sur le bouton `Run`.

**Note** : Ce projet utilise une API météo publique. Si une clé API est nécessaire, vous devrez peut-être en obtenir une et l'ajouter dans les fichiers de configuration appropriés.
