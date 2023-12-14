# Overview Android App
[![CI](https://github.com/leoallvez/take/actions/workflows/ci.yml/badge.svg)](https://github.com/leoallvez/take/actions/workflows/ci.yml)

## About
Overview is a user-friendly app that consolidates content from major streaming services into a unified interface. It offers personalized recommendations, easy search and filtering, watchlist management, and cross-platform compatibility. With real-time updates, it simplifies content discovery and provides a comprehensive overview of available movies and TV shows across different platforms.

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

> Example of a Windows .bat Script File to Set Environment Variables
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

> Example of a Linux .bash Script File to Set Environment Variables
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
