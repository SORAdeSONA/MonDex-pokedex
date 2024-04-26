# Mondex

Mondex is a Pokédex app designed to explore and store creatures in a database. It allows users to explore Pokémon, items, moves, and more.

## Stack

Mondex is built using the following technologies:

- **Retrofit**: A type-safe HTTP client for Android and Java, used for making network requests to fetch data from external APIs.

- **ViewModel and LiveData**: Components of the Android Architecture Components library for managing UI-related data. ViewModel retains UI-related data across configuration changes, while LiveData provides lifecycle-aware data observation.

- **Navigation**: Android Jetpack's navigation components for handling navigation between fragments. It simplifies the implementation of navigation in Android apps.

- **Hilt**: Dependency injection library provided by Google for Android, built on top of Dagger. Hilt makes it easy to manage dependencies in your app.

- **Glide**: Image loading library for Android that simplifies the process of loading images from URLs, local storage, or other sources.

- **Paging**: Android Jetpack's paging library for loading data gradually and efficiently from a data source, allowing for smoother scrolling and reduced memory consumption.

- **Shared Preferences**: Android's lightweight data storage mechanism for storing key-value pairs. It's commonly used for storing app settings and preferences.

- **Room**: A persistence library provided by Android Jetpack for managing SQLite databases. Room provides an abstraction layer over SQLite, making it easier to work with databases in Android apps.

- **AdMob**: Google's mobile advertising platform for monetizing your app by displaying ads.

- **LeakCanary (debug only)**: A memory leak detection library for Android, used in debug builds to automatically detect memory leaks and help identify and fix memory-related issues.
