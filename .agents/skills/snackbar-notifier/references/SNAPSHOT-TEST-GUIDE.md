# Snackbar Snapshot Test Guide

This reference explains, step by step, how to create a snapshot test for a **screen that shows a snackbar** in the **Overview** project using **Paparazzi** animations.

## Why an animation

The snackbar is emitted from a `LaunchedEffect` and enters the screen with a slide animation. A static `snapshot { }` captures the first frame, **before** the snackbar exists, so the image looks exactly like the screen without it. Use `gif`, which records the frames over time.

## Step by step

### Step 1: Create a preview that shows the snackbar

In the screen file, add an `internal` preview that:

- wraps the screen in `UiSnackbarPreview`, which provides the `Scaffold` with the snackbar host;
- passes `hostState::show` to the `Actions` as `onShowSnackbar`;
- toggles the flag in a loop, so the snackbar keeps appearing.

```kotlin
@UiScreenPreview
@Composable
internal fun MovieDetailsScreenFavoriteAddedPreview() {
    // Active Interactive mode to see the snackbar
    var favoriteAdded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            favoriteAdded = true
            delay(5_000.milliseconds)
            favoriteAdded = false
            delay(1_000.milliseconds)
        }
    }

    UiSnackbarPreview { hostState ->
        MovieDetailsScreen(
            movieId = 1L,
            uiState = UiState.Success(data = fakeMovieDetails()),
            favoriteAdded = favoriteAdded,
            actions = rememberMovieDetailsActions(onShowSnackbar = hostState::show)
        )
    }
}
```

### Step 2: Add the test using `gif`

In the screen snapshot test class (it extends `UiScreenSnapshotTest`), add one test per snackbar. Keep the other states (`default`, `loading`, `error`) as static `snapshot` tests.

```kotlin
class MovieDetailsSnapshotTest : UiScreenSnapshotTest(snapshotPackage = "screens/media/movie") {

    @Test
    fun default() = snapshot {
        MovieDetailsScreenPreview()
    }

    @Test
    fun favoriteAdded() = gif(
        duration = 2_000L
    ) {
        MovieDetailsScreenFavoriteAddedPreview()
    }
}
```

`2_000L` records the snackbar entering and resting on screen. Use a longer duration (e.g. `6_000L`, as in `LoginSnapshotTest`) only when the dismissal or the loop must also be recorded: longer recordings of screens with images produce large files.

### Step 3: Record the animation

```bash
./gradlew :presentation:recordPaparazziDebug --tests '*MovieDetailsSnapshotTest'
```

One animated PNG per device profile (Nexus 5, Pixel 3A XL, Pixel Fold and Pixel Tablet) is written to `presentation/src/test/snapshots/<snapshotPackage>/videos/`. Static snapshots stay in `images/`.

### Step 4: Check the recording

A passing test does not prove the snackbar was rendered. Open one of the recorded files, or extract its last frame, and confirm the snackbar is visible with the expected icon, title and message.

```bash
python3 -c "
from PIL import Image
im = Image.open('<path to the recorded .png>')
im.seek(im.n_frames - 1)
im.convert('RGB').save('last-frame.png')
"
```

Also run `git status` and confirm that the static images of the other states were **not** changed by the recording.

### Step 5: Verify

```bash
./gradlew :presentation:verifyPaparazziDebug --tests '*MovieDetailsSnapshotTest'
```

## Key Requirements

- **`gif`, not `snapshot`:** Every test that expects a visible snackbar MUST use `gif`.
- **Preview Usage:** Call the internal `...Preview()` function that uses `UiSnackbarPreview`. Do not build the screen again inside the test.
- **One Test per Snackbar:** Each snackbar the screen can show (e.g. success and error) gets its own preview and its own `gif` test.
- **Commit the Recordings:** The files in `videos/` are the golden files and must be committed with the test.

## Isolated component

The component itself (`UiSnackbar`) is rendered with `visible = true` by default, so its variants and styles are tested with static snapshots, as in `UiSnackbarSnapshotTest.kt`.
