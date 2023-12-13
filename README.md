# Overview
[![CI](https://github.com/leoallvez/take/actions/workflows/ci.yml/badge.svg)](https://github.com/leoallvez/take/actions/workflows/ci.yml)


## Environment Variables

| Variable Name            | Description                                                                                 |
|--------------------------|---------------------------------------------------------------------------------------------|
| `OVER_TMDB_API_KEY`      | API key for [**TMDB API**](https://developers.themoviedb.org/3/getting-started/introduction)|
| `OVER_ACTIVE_SIGNING`    | Flag to activate or not signing                                                             |
| `OVER_PROD_KEYSTORE`     | **Production** keystore file path                                                           |
| `OVER_PROD_PASSWORD`     | Password for **production** keystore                                                        |
| `OVER_PROD_KEY_ALIAS`    | Key alias for **production** keystore                                                       |
| `OVER_HOMOL_KEYSTORE`    | **Homologation** keystore file path                                                         |
| `OVER_HOMOL_PASSWORD`    | Password for **homologation** keystore                                                      |
| `OVER_HOMOL_KEY_ALIAS`   | Key alias for **homologation** keystore                                                     |


Android application to know where to watch a movie or series on streaming services.

## Configuration
It is crucial for the application to work the creation of the 'api.properties' in the project root,
this file should [**The Movie Database API**](https://developers.themoviedb.org/3/getting-started/introduction) 
API key:

```
API_KEY = "Your The movie Database API KEY"
```

learn more about the development process [here](https://tree.taiga.io/project/leoallvez-take/timeline).
