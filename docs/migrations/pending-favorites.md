# Pending Favorites Migration

Temporary code introduced in **v4.0.0** to preserve the favorites of users upgrading from
**v3.3.1 or older**, when favorites moved from the local Room database to Firebase Firestore.

It is meant to be removed in a version after v4.0.0, once the removal criteria below are met.
The removal is tracked in [#876](https://github.com/leoallvez/overview/issues/876).

## Context

Up to v3.3.1, favorites were stored in the local `media` table (`is_liked = 1`) of the
`database_overview` Room database (schema version 4).

In v4.0.0, favorites live in Firestore under `users/{userId}/favorites`, and the 4 → 5 migration
drops the `media` table. Without this code, every upgrading user would lose their favorites.

## How It Works

1. **Migration 4 → 5:** before dropping `media`, liked rows are copied into the
   `pending_favorite` table.
2. **Sync after sign-in:** when a signed-in user's favorites are observed,
   `MediaFavoriteRepository.syncPending(userId)` uploads the pending favorites with
   `FavoriteRemoteDataSource.saveAll`, waits for the Firestore commit, and only then clears the
   table. On failure, the data is kept and the sync is retried on the next observation.

Users who never sign in keep their pending favorites on the device indefinitely.

## Affected Files

| Module  | File                                                       | Change                                           |
|---------|------------------------------------------------------------|--------------------------------------------------|
| `:app`  | `src/main/assets/database/migrations/migration_4_to_5.sql` | Creates and fills `pending_favorite`             |
| `:app`  | `di/data/StorageModule.kt`                                 | `providePendingFavoriteDao`                      |
| `:app`  | `di/data/local/LocalDataSourceModule.kt`                   | `bindPendingFavoriteLocalDataSource`             |
| `:data` | `model/PendingFavoriteDataModel.kt`                        | Room entity (new file)                           |
| `:data` | `local/database/dao/PendingFavoriteDao.kt`                 | DAO (new file)                                   |
| `:data` | `local/source/PendingFavoriteLocalDataSource.kt`           | Data source (new file)                           |
| `:data` | `local/database/AppDatabase.kt`                            | Entity and `pendingFavoriteDao()` registration   |
| `:data` | `network/source/FavoriteRemoteDataSource.kt`               | `saveAll` (awaits the commit)                    |
| `:data` | `repository/media/MediaFavoriteRepository.kt`              | `pendingDataSource` dependency and `syncPending` |
| `:data` | `schemas/.../AppDatabase/5.json`                           | `pending_favorite` table                         |
| `:data` | `src/test/.../PendingFavoriteLocalDataSourceTest.kt`       | Tests (new file)                                 |
| `:data` | `src/test/.../FavoriteRemoteDataSourceTest.kt`             | `saveAll` tests                                  |
| `:data` | `src/test/.../MediaFavoriteRepositoryTest.kt`              | Pending favorites tests                          |

## Removal Criteria

Removing this code means that any user still on v3.3.1 or older who upgrades afterwards loses
their favorites. Remove it only when:

- [ ] The share of active installs on v3.x in the Google Play Console is negligible.
- [ ] At least one release after v4.0.0 has shipped with this code.

## Removal Checklist

The `pending_favorite` table already exists on users' devices, so removing the entity requires a
new schema version. Removing it from `AppDatabase` without a migration makes Room fail to open
the database.

### Database

- [ ] Create `app/src/main/assets/database/migrations/migration_5_to_6.sql` with:

  ```sql
  DROP TABLE IF EXISTS `pending_favorite`;
  ```

- [ ] Bump `AppDatabase` to `version = 6` and remove `PendingFavoriteDataModel::class` and
  `pendingFavoriteDao()`.
- [ ] Register `Migration(from = 5, to = 6, context)` in `StorageModule`.
- [ ] Build to export `6.json`. Do not edit `5.json`: it records a shipped schema.
- [ ] Remove the `pending_favorite` statements from `migration_4_to_5.sql`, keeping the
  `DROP TABLE IF EXISTS media;`. Optional: if they stay, 5 → 6 drops the table anyway.

### Code

- [ ] Delete `PendingFavoriteDataModel.kt`, `PendingFavoriteDao.kt`,
  `PendingFavoriteLocalDataSource.kt` and `PendingFavoriteLocalDataSourceTest.kt`.
- [ ] Remove `providePendingFavoriteDao` from `StorageModule` and
  `bindPendingFavoriteLocalDataSource` from `LocalDataSourceModule`.
- [ ] Remove `pendingDataSource`, `syncPending` and the `.onStart { syncPending(userId) }` call
  from `MediaFavoriteRepository`, along with the related tests.
- [ ] Remove `saveAll` from `IFavoriteRemoteDataSource` / `FavoriteRemoteDataSource` and its
  tests, unless it gained other callers.
- [ ] Delete this document.

### Verification

- [ ] `grep -rn "PendingFavorite\|pending_favorite\|syncPending" --include=*.kt --include=*.sql .`
  returns only `migration_4_to_5.sql` (if kept) and `migration_5_to_6.sql`.
- [ ] `./gradlew testDebugUnitTest` and `./gradlew :app:assembleDebug` pass.
- [ ] Upgrading a device from the previous release opens the app without Room errors.
