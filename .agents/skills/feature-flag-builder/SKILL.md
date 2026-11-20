---
name: feature-flag-builder
description: It creates a boolean feature flag controlled by Firebase Remote Config, with a local override in `local.properties` for debug builds, and delivers it to the screens through `FeatureFlags`. Use it when a feature must be shown or hidden remotely without publishing a new version of the app.
metadata:
  author: Singular
  version: "1.0"
  keywords:
  - remote config
  - feature flag
  - firebase
  - toggle
  - local.properties
---

## Core Workflow

- [ ] Step 1: Identify the flag name, the Remote Config key (`display_<feature>`, snake_case) and
  the screens that will read it. Ask the user for anything that is not clear.
- [ ] Step 2: Create a plan listing the files to change in each module and present it for approval
  before moving to the next steps.
- [ ] Step 3: In `:data`, add the key to `RemoteConfigKey` (e.g.
  `DISPLAY_<FEATURE>_KEY("display_<feature>")`).
- [ ] Step 4: In `:data`, create `Display<Feature>RemoteConfig` implementing `RemoteConfig<Boolean>`
  in `br.dev.singular.overview.data.remoteconfig`.
- [ ] Step 5: In `:data`, create `Display<Feature>RemoteConfigTest` covering the 4 cases (override
  `true`, override `false`, no override with remote `true`, no override with remote `false`).
- [ ] Step 6: In `app/build.gradle.kts`, declare `LOCAL_DISPLAY_<FEATURE>` with
  `optionalBooleanField`: `localFlag("flags.display_<feature>")` in `debug` and `null` in `release`.
- [ ] Step 7: In `RemoteConfigModule`, create the `@Display<Feature>` qualifier, its provider, and
  the new field in `provideFeatureFlags`.
- [ ] Step 8: Add the field to `FeatureFlags` and read it in the route that renders the screen (e.g.
  `showAds = flags.showAds`). Refer to the [feature-integrator](../feature-integrator/SKILL.md)
  skill.
- [ ] Step 9: The screen receives a plain `Boolean` parameter (`show<Feature>`) and hides the
  feature when it is `false`.
- [ ] Step 10: Add the key to the "Feature Flags (Remote Config)" table in `README.md`.
- [ ] Step 11: Remind the user to create the key as a **Boolean** in the Firebase console.
- [ ] Step 12: All tests must pass successfully.
- [ ] Step 13: Standardize the commit message using
  the [git-standardizer](../git-standardizer/SKILL.md) skill.

## Architecture Guidelines

A flag travels in one direction: Firebase → `:data` → Hilt → `MainActivity` → `AppNavHost` → route →
screen.

- **Remote source:** `IRemoteConfigProvider` (`RemoteConfigWrapper`) wraps Firebase. It is started
  in `CustomApplication` and fetches in the background. The minimum fetch interval is
  `REMOTE_CONFIG_FETCH_INTERVAL_IN_SECONDS` (`0` in debug, `3600` in release).
- **One class per flag:** `Display<Feature>RemoteConfig` resolves the final value as
  `_localOverride ?: remote`.
- **Local override:** `localFlag` (in `build-logic/convention`) reads `local.properties` and returns
  `true` for `on`, `false` for `off` and `null` otherwise. Release builds always use `null`.
- **Single entry point:** `provideFeatureFlags` builds the `FeatureFlags` data class. `MainActivity`
  only injects `FeatureFlags` and passes it to `AppNavHost`.
- **Read once:** Flags are read when the Activity is created. A change in the console reaches the
  app after a fetch completes and the Activity is created again.
- **Missing key:** Firebase returns `false` for a key that does not exist, so a new feature stays
  hidden until the key is created.

### Local override values

| `local.properties`            | Debug result        | Release result      |
|:------------------------------|:--------------------|:--------------------|
| `flags.display_<feature>=on`  | Always shown        | Remote Config value |
| `flags.display_<feature>=off` | Always hidden       | Remote Config value |
| No line                       | Remote Config value | Remote Config value |

## Code Examples

Reference implementation: the `display_ai_prediction` flag that controls the AI prediction card.

### Remote config class (`:data`)

```kotlin
data class DisplayAiPredictionRemoteConfig(
    private val _localOverride: Boolean?,
    private val _remoteSource: IRemoteConfigProvider
) : RemoteConfig<Boolean> {
    override fun execute(): Boolean {
        return _localOverride ?: _remoteSource.getBoolean(DISPLAY_AI_PREDICTION_KEY)
    }
}
```

### Build config (`app/build.gradle.kts`)

```kotlin
buildTypes {
    release {
        optionalBooleanField("LOCAL_DISPLAY_AI_PREDICTION", null)
    }
    debug {
        optionalBooleanField("LOCAL_DISPLAY_AI_PREDICTION", localFlag("flags.display_ai_prediction"))
    }
}
```

### Hilt module (`RemoteConfigModule`)

```kotlin
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DisplayAiPrediction

@Provides
@DisplayAiPrediction
fun providerDisplayAiPredictionRemote(
    remoteSource: IRemoteConfigProvider
): RemoteConfig<Boolean> {
    return DisplayAiPredictionRemoteConfig(
        _localOverride = BuildConfig.LOCAL_DISPLAY_AI_PREDICTION,
        _remoteSource = remoteSource
    )
}

@Provides
fun provideFeatureFlags(
    @DisplayAds displayAds: RemoteConfig<Boolean>,
    @DisplayAiPrediction displayAiPrediction: RemoteConfig<Boolean>
): FeatureFlags {
    return FeatureFlags(
        showAds = displayAds.execute(),
        showAiPrediction = displayAiPrediction.execute()
    )
}
```

### Route

```kotlin
MovieDetailsScreen(
    movieId = movieId,
    showAds = flags.showAds,
    showAiPrediction = flags.showAiPrediction,
    uiState = viewModel.uiState.collectAsState().value,
    actions = actions
)
```

### Unit test (`:data`)

```kotlin
@Test
fun `execute should return false when localOverride is false even if remote is true`() {
    // arrange
    val sut = DisplayAiPredictionRemoteConfig(_localOverride = false, _remoteSource = remoteSource)
    every { remoteSource.getBoolean(DISPLAY_AI_PREDICTION_KEY) } returns true

    // act
    val result = sut.execute()

    // assert
    assertFalse(result)
    verify(exactly = 0) { remoteSource.getBoolean(any()) }
}
```

## Mandatory Rules

- **Qualified Bindings:** Every `RemoteConfig<Boolean>` binding MUST have its own qualifier. Never
  provide or inject an unqualified `RemoteConfig<Boolean>`.
- **Single Entry Point:** Activities and routes MUST only read `FeatureFlags`. DO NOT inject
  `RemoteConfig` or `IRemoteConfigProvider` into the UI.
- **No Flags in Release:** The `release` build type MUST declare every `LOCAL_DISPLAY_*` field as
  `null`.
- **Plain Screen Parameters:** Screens in `:presentation` MUST receive a `Boolean`, never
  `FeatureFlags` or a Remote Config type.
- **Key Naming:** Keys MUST be snake_case and start with `display_` for show/hide flags.
- **Documentation:** Every new key MUST be added to the table in `README.md`.
- **Plan Approval:** You MUST NOT modify files until the user approves the plan in Step 2.
- **Tests Success:** All new tests MUST pass before completing the task.
