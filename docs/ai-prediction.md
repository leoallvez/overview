# AI Prediction

The media details screen shows an AI prediction card with the user's **affinity** with the movie or
TV show, from 0% to 100%. The affinity is predicted by [Jev](https://docs.typesafe.ai), a TypeSafe
model that answers typed questions (yes/no, choice or score) instead of generating text, based on
the user's favorites.

The card is controlled by the `display_ai_prediction` feature flag (see the README).

## Flow

```text
UiAiPredictionCard (tap)
  → AiPredictionViewModel.onPredict(media)
    → GetPredictionUseCase(input)              reads the favorites, picks the 10 most recent
      → PredictionRepository.getByParam(input) returns the cached prediction, if any
        → PredictionRemoteDataSource           POST https://api.typesafe.ai/v1/systemone
```

1. **Card:** the prediction is only requested when a signed-in user taps the card. Signed-out
   users see the `LoginRequired` state.
2. **Use case:** reads the favorites from `MediaFavoriteRepository` (ordered by `last_update`) and
   removes the media itself. Then:
    - **No other favorites:** returns `Prediction.Unavailable` without calling the API.
    - **Otherwise:** sends the **10 most recent** favorites (`MAX_FAVORITES`) and the genre count
      of **all** favorites, so users with many favorites keep their overall taste in the request.
3. **Repository:** returns the prediction cached in the `prediction` table, keyed by
   `(type, id)`. Otherwise, it calls the API and caches the result when it is `Available`.
4. **Data source:** builds the Jev request, sends it and converts the score to a percentage.

## Request

The full example is in [prediction-request.json](prediction-request.json).

- **`model`:** `BuildConfig.JEV_MODEL` (`jev-1.13.0`).
- **`state`:** the content Jev evaluates:
    - **`media`:** original title, type and genres (by their original, untranslated names).
    - **`favorites.titles`:** original title and type of each favorite sent.
    - **`favorites.genres`:** how many favorites have each genre.
- **`questions.affinity`:** a `score` question with 5 levels, from "would not enjoy it at all" (0)
  to "would certainly enjoy it" (4), defined in `PredictionRemoteDataSource`.

Titles and genres are sent untranslated so that the media and the favorites are compared in the
same language, whatever the app locale is. When the original value is missing, the translated one
is used instead.

## Response

The full example is in [prediction-response.json](prediction-response.json).

Jev returns `answers.affinity.score`, the probability-weighted average of the levels, so it can be
any value from 0 to 4. The app reads only `score` and converts it:

```text
affinityPercentage = round(score / (levels - 1) * 100)
```

For example, a score of `3.36` with 5 levels gives `3.36 / 4 * 100 = 84%`. The other fields
(`confidence`, `legend`, `probabilities` and `usage`) are ignored.

Because the score is an average, it rarely reaches 0% or 100%. Most predictions fall in the
middle of the scale.

## Prediction States

| Situation                                | Domain                   | Card            |
|------------------------------------------|--------------------------|-----------------|
| Score received                           | `Prediction.Available`   | `Predicted`     |
| No favorites other than the media itself | `Prediction.Unavailable` | `Unavailable`   |
| Network or server error                  | `Prediction.Error`       | `Error` (retry) |
| Response without a score                 | `Prediction.Error`       | `Error` (retry) |
| User signed out                          | —                        | `LoginRequired` |

## Favorites Data

Favorites are stored in Firestore under `users/{userId}/favorites`. Since the Jev integration, each
document also stores:

| Field            | Type            | Source                                                   |
|------------------|-----------------|----------------------------------------------------------|
| `original_title` | string          | `MediaDetailsUiModel.metadata.originalTitle`             |
| `genres`         | array of string | Genre `originalName`, or the translated `name` if empty  |

Both are written when the media is favorited from the details screen, the only place where
favorites are added. Documents saved earlier don't have them: they are read with empty values, so
the translated `title` is sent instead and their genres are not counted until the media is
favorited again.

## Cache

Predictions are stored in the `prediction` Room table and are cleared when:

- a favorite is added or removed (`MediaFavoriteRepository.update`), since the prediction depends
  on the favorites;
- the user signs out (`UserRepository`).

Only `Available` predictions are cached: errors and `Unavailable` are requested again on the next
tap.

## Configuration

| `BuildConfig` field | Value                                       |
|---------------------|---------------------------------------------|
| `JEV_API_URL`       | `https://api.typesafe.ai/`                  |
| `JEV_API_KEY`       | `OVER_JEV_API_KEY` environment variable     |
| `JEV_MODEL`         | `jev-1.13.0`                                |

To test it locally:

```bash
export OVER_JEV_API_KEY="your-key"
```

```properties
# local.properties
flags.display_ai_prediction=on
```

The key is sent as `Authorization: Bearer <key>` by `JevAuthInterceptor`, and the header is
redacted from the HTTP logs. Jev uses its own Retrofit and OkHttp client (`ApiModule`), without
the TMDB key and locale interceptors.

> **Security:** the key is embedded in the APK and can be extracted from it. Moving the call to a
> backend (e.g. a Cloud Function) would keep the key off the device.

## Affected Files

| Module          | File                                                    | Role                                       |
|-----------------|---------------------------------------------------------|--------------------------------------------|
| `:domain`       | `model/Media.kt`                                        | `originalTitle` and `genres`               |
| `:domain`       | `model/PredictionInput.kt`                              | Media, favorites and genre count           |
| `:domain`       | `usecase/prediction/GetPredictionUseCase.kt`            | Favorites selection and genre count        |
| `:data`         | `network/JevApiService.kt`                              | Jev endpoint                               |
| `:data`         | `network/interceptor/JevAuthInterceptor.kt`             | `Authorization` header                     |
| `:data`         | `network/request/JevRequest.kt`                         | Request body                               |
| `:data`         | `network/response/JevResponse.kt`                       | Response body                              |
| `:data`         | `network/source/PredictionRemoteDataSource.kt`          | Question, request and score conversion     |
| `:data`         | `network/source/FavoriteRemoteDataSource.kt`            | `original_title` and `genres` in Firestore |
| `:data`         | `repository/prediction/PredictionRepository.kt`         | Cache                                      |
| `:data`         | `util/mappers/domainToData/PredictionInput.kt`          | Original title fallback for favorites      |
| `:presentation` | `ui/utils/mappers/uiToDomain/MediaDetails.kt`           | Original title and genres from the details |
| `:presentation` | `ui/components/prediction/AiPredictionViewModel.kt`     | Card state                                 |
| `:app`          | `di/data/ApiModule.kt`                                  | Jev Retrofit client                        |
| `:app`          | `di/domain/UseCaseModule.kt`                            | Use case dependencies                      |

## Changing the Prediction

- **Question or levels:** edit `AFFINITY_QUESTION` in `PredictionRemoteDataSource`. The
  conversion uses the number of levels, so it follows any change (Jev accepts up to 10). Cached
  predictions don't expire, so users keep the old values after an update until their favorites
  change. If that matters, clear the `prediction` table in a database migration.
- **Number of favorites:** edit `MAX_FAVORITES` in `GetPredictionUseCase`.
- **Model:** edit `JEV_MODEL` in `data/build.gradle.kts`. Available models are listed in the
  [Jev models documentation](https://docs.typesafe.ai/models.md).
