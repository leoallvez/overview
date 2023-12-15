# Overview Android App
[![CI](https://github.com/leoallvez/take/actions/workflows/ci.yml/badge.svg)](https://github.com/leoallvez/take/actions/workflows/ci.yml)

## About
Overview is a user-friendly app that consolidates content from major streaming services into a unified interface. It offers personalized recommendations, easy search and filtering, watchlist management, and cross-platform compatibility. With real-time updates, it simplifies content discovery and provides a comprehensive overview of available movies and TV shows across different platforms.

### Main libraries

| **Library**                   | **Description**                                                                                        |
|-------------------------------|--------------------------------------------------------------------------------------------------------|
| [Compose][1]                  | Modern Android UI toolkit for building native UIs with a declarative syntax.                           |
| [DataStore][2]                | Jetpack library for data persistence using Kotlin coroutines and Flow.                                 |
| [Navigation Compose][3]       | Navigation library for Jetpack Compose, facilitating navigation between composables.                   |
| [Hilt][4]                     | Dependency injection library for Android that is built on top of Dagger.                               |
| [Room][5]                     | Persistence library that provides an abstraction layer over SQLite for offline data storage.           |
| [AndroidX][6]                 | Android extension libraries, part of Jetpack, offering backward-compatible versions of Android APIs.   |
| [KTX Extensions][7]           | Kotlin extensions for Android development, enhancing code readability and conciseness.                 |
| [Live Data][8]                | Observable data holder class that is lifecycle-aware, part of the Android Architecture Components.     |
| [Paging][9]                   | Library for handling large datasets and paginated data sources efficiently.                            |
| [JUnit][10]                   | Testing framework for Java and Kotlin, widely used for unit testing in Android development.            |
| [Mockk][11]                   | Mocking library for Kotlin, useful for creating mocks and stubs in testing.                            |
| [Retrofit][12]                | HTTP client for Android and Java that simplifies network requests and communication with web services. |
| [Timber][13]                  | Logging library for Android, providing a simple way to log messages with additional context.           |
| [Kluent][14]                  | Fluent assertion library for Kotlin, improving the readability of assertions in tests.                 |
| [Gradle’s Kotlin DSL][15]     | Kotlin-based Domain Specific Language for Gradle build scripts.                                        |
| [Gradle Version Catalogs][16] | Centralized management of library versions in Gradle builds, improving dependency version consistency. |
| [Remote Config][17]           | Firebase service for remotely configuring your app's behavior without publishing app updates.          |
| [Analytics][18]               | Analytics solution by Firebase, providing insights into user behavior and app performance.             |
| [Crashlytics][19]             | Firebase service for crash reporting, helping developers identify and fix issues causing app crashes.  |
| [Coil][20]                    | Coil is a Kotlin-first image loading library for Android that prioritizes simplicity and performance.  | 


[1]: https://developer.android.com/jetpack/compose?hl=pt-br
[2]: https://developer.android.com/topic/libraries/architecture/datastore?hl=en
[3]: https://developer.android.com/jetpack/compose/navigation?hl=en
[4]: https://developer.android.com/training/dependency-injection/hilt-android?hl=en
[5]: https://developer.android.com/training/data-storage/room
[6]: https://developer.android.com/jetpack/androidx
[7]: https://developer.android.com/kotlin/ktx
[8]: https://developer.android.com/topic/libraries/architecture/livedata
[9]: https://developer.android.com/topic/libraries/architecture/paging/v3-overview?hl=en

[10]: https://junit.org/junit4/
[11]: https://mockk.io/
[12]: https://square.github.io/retrofit/
[13]: https://github.com/JakeWharton/timber
[14]: https://github.com/MarkusAmshove/Kluent
[15]: https://docs.gradle.org/current/userguide/kotlin_dsl.html#kotlin_dsl
[16]: https://developer.android.com/build/migrate-to-catalogs?hl=en

[17]: https://firebase.google.com/docs/remote-config
[18]: https://firebase.google.com/docs/analytics
[19]: https://firebase.google.com/docs/crashlytics
[20]: https://github.com/coil-kt/coil



## Setup
This repository contains environment variables that streamline the compilation process in the staging and production environments (PROD and HOMOL) for the Overview Android project. These variables encompass the API key, signature flags, and key storage information for different environments.

| Environment Variables    | Description                                                                                  |
|--------------------------|----------------------------------------------------------------------------------------------|
| `OVER_TMDB_API_KEY`      | API key for [**TMDB API**](https://developers.themoviedb.org/3/getting-started/introduction) |
| `OVER_ACTIVE_SIGNING`    | Boolean Flag to activate or not signing, value is **true** or **false**                      |
| `OVER_PROD_KEYSTORE`     | **Production** keystore file path                                                            |
| `OVER_PROD_PASSWORD`     | Password for **production** keystore                                                         |
| `OVER_PROD_KEY_ALIAS`    | Key alias for **production** keystore                                                        |
| `OVER_HOMOL_KEYSTORE`    | **Homologation** keystore file path                                                          |
| `OVER_HOMOL_PASSWORD`    | Password for **homologation** keystore                                                       |
| `OVER_HOMOL_KEY_ALIAS`   | Key alias for **homologation** keystore                                                      |

### Script
We recommend developing a script to systematically configure environment variables in your operating system. The use of scripts provides notable advantages in terms of automation, reproducibility, clarity, ease of modification, version control, batch execution, consistency, deployment efficiency, customization, and error handling when establishing environment variables. In the subsequent sections, we will illustrate the process of crafting such scripts for both Windows and Linux operating systems.

#### Script Windows

> Example of a windows script .bat file to set environment variables.
```bat

setx OVER_TMDB_API_KEY "f0d4ff18152fd5ff09fb0b86f20f5d4f"
setx OVER_ACTIVE_SIGNING true

setx OVER_PROD_KEYSTORE "C:\\Keystores\\overview\\prod\\prod_keystore.jks"
setx OVER_PROD_PASSWORD "set_here_your_password_value"
setx OVER_PROD_KEY_ALIAS "upload"

setx OVER_HOMOL_KEYSTORE "C:\\Keystore\\overview\\homol\\homol_keystore.jks"
setx OVER_HOMOL_PASSWORD "set_here_your_password_value"
setx OVER_HOMOL_KEY_ALIAS "upload"

```
#### Script Linux

> Example of a linux .bash script file to set environment variables.

```bash
#!/bin/bash

export OVER_TMDB_API_KEY="f0d4ff18152fd5ff09fb0b86f20f5d4f"
export OVER_ACTIVE_SIGNING=true

export OVER_PROD_KEYSTORE="~/Keystores/overview/prod/prod_keystore.jks"
export OVER_PROD_PASSWORD="set_here_your_password_value"
export OVER_PROD_KEY_ALIAS="upload"

export OVER_HOMOL_KEYSTORE="~/Keystores/overview/homol/homol_keystore.jks"
export OVER_HOMOL_PASSWORD="set_here_your_password_value"
export OVER_HOMOL_KEY_ALIAS="upload"

```

## Contributing

Thank you for considering contributing to this project! Follow these steps to contribute:

> We strongly recommend using [git flow](https://nvie.com/posts/a-successful-git-branching-model/) and [conventional commits](https://www.conventionalcommits.org/en/v1.0.0/) practices

- **Fork** the repository.
- Create a new branch for your feature (__git checkout -b feture/my-new-feature__).
- Make your changes and **commit** them (__git commit -m 'Add some feature'__).
- **Push** your branch to your fork (__git push origin feture/my-new-feature__).
- Create a **new Pull Request** to the **`develop`** branch of this repository.

Please make sure to provide a clear description of your changes when submitting a pull request. We appreciate your contributions!
