## Meteo Cached

Application météo Jetpack Compose suivant l’architecture MVVM + UDF imposée dans le TP. L’app s’appuie sur l’API Open-Meteo (modèle `meteofrance_seamless`), gère la recherche de villes, les favoris persistés (Room + DataStore), la géolocalisation et le mode hors-ligne.

### Architecture

- **UI layer (`ui/`)** : écrans Compose (Home, Search, Details), composants réutilisables, navigation Compose. Les `Route` collectent l’état des ViewModels via `collectAsStateWithLifecycle()`. Les états sont immuables (`data class`) et appliquent l’UDF (intent → ViewModel → StateFlow → UI).
- **Domain layer (`domain/usecases`)** : use cases ciblés (`SearchCityUseCase`, `GetWeatherForecastUseCase`, `GetFavoriteCitiesUseCase`, `AddToFavoritesUseCase`, `RemoveFromFavoritesUseCase`) injectés dans les ViewModels via `AppViewModelProvider`.
- **Data layer (`data/`)** :
  - `remote/WeatherApiService` (Retrofit + Kotlinx Serialization) pour la géolocalisation et les prévisions.
  - `datasource/RemoteWeatherDataSource`, `LocalWeatherDataSource`, `UserPreferencesDataSource` (DataStore).
  - `local/` Room (`FavoriteCityDao`, `WeatherDao`, `WeatherDatabase`) pour le cache météo/favoris.
  - `repository/WeatherRepositoryImpl` combine les flux Room + DataStore + réseau et expose `Flow<ApiResult<…>>`.
  - `model/` contient les data classes API, domaine, mappers et `ApiResult`.

### Fonctionnalités principales

- **Accueil** : barre de recherche (redirige vers l’écran dédié), bouton « Utiliser ma position », carte météo géolocalisée, liste des favoris en `LazyColumn` (clé = `id`, `rememberSaveable` pour la saisie).
- **Recherche** : requête `https://geocoding-api.open-meteo.com/v1/search?name=…`, affichage des résultats et navigation vers le détail.
- **Détail** : température actuelle, min/max, vent, condition + icône, bouton ajouter/retirer des favoris.
- **API météo** : `https://api.open-meteo.com/v1/forecast?...&models=meteofrance_seamless`.
- **Favoris** : Add/remove synchronisés entre DataStore (accès rapide) et Room (cache complet).
- **Hors-ligne** : si l’appel réseau échoue → retour `ApiResult.Error` avec données cache + message « mode hors ligne ».
- **Erreurs** : gestion Timeout/No network/API vide via `ApiResult.Error` + `StateMessageCard`.
- **Navigation** : `NavHost` Compose (`home`, `search`, `details/{...}`) avec `NavController`.

### Stack technique

- Kotlin 2.0.21, Compose Material 3, Navigation Compose
- ViewModel + `StateFlow` + Coroutines (Dispatchers.IO pour I/O)
- Retrofit + Kotlinx Serialization + OkHttp Logging
- Room + KSP, DataStore Preferences
- Play Services Location, Coil (icônes/images extensibles)

### Lancement

```bash
./gradlew :app:assembleDebug
```

Ou ouvrez le projet dans Android Studio (Giraffe+) et exécutez l’application sur un device réel (permission localisation requise pour le bouton « Utiliser ma position »).

### Tests / vérifications recommandées

- Rechercher plusieurs villes, ajouter/retirer des favoris → vérifier la persistance après redémarrage.
- Activer le mode avion puis relancer un refresh : l’app doit afficher les données Room + message hors-ligne.
- Tester la géolocalisation (autorisation accordée/refusée) pour s’assurer que la remontée d’erreurs est correcte.

### Notes

- `WeatherSummary` centralise toutes les données météo affichées dans l’UI (température, min/max, vent, icône, statut favori, provenance cache).
- Les `ViewModel` orchestrent uniquement des intentions utilisateur (refresh, search, toggle favorite). Toute la logique des données reste dans les use cases / repository.
- Le dossier `res/values` contient les `strings.xml` et `dimens.xml` imposés par le cours (Material3 reste configurable).

