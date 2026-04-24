package br.dev.singular.overview.di.domain

import br.dev.singular.overview.data.repository.DataStoreRepository
import br.dev.singular.overview.data.repository.GenreLocalRepository
import br.dev.singular.overview.data.repository.GenreRemoteRepository
import br.dev.singular.overview.data.repository.MediaLocalRepository
import br.dev.singular.overview.data.repository.MediaRemoteRepository
import br.dev.singular.overview.data.repository.PersonRepository
import br.dev.singular.overview.data.repository.SuggestionRepository
import br.dev.singular.overview.data.repository.catalog.CatalogQueryLocalRepository
import br.dev.singular.overview.data.repository.catalog.CatalogRepository
import br.dev.singular.overview.di.data.CatalogTooltip
import br.dev.singular.overview.domain.usecase.CatalogQueryStateUseCase
import br.dev.singular.overview.domain.usecase.CatalogTooltipDismissedUseCase
import br.dev.singular.overview.domain.usecase.GetPersonByIdUseCase
import br.dev.singular.overview.domain.usecase.ICatalogQueryStateUseCase
import br.dev.singular.overview.domain.usecase.ICatalogTooltipDismissedUseCase
import br.dev.singular.overview.domain.usecase.IDeleteUseCase
import br.dev.singular.overview.domain.usecase.IGetPersonByIdUseCase
import br.dev.singular.overview.domain.usecase.catalog.DeleteCatalogUseCase
import br.dev.singular.overview.domain.usecase.catalog.GetAllCatalogUseCase
import br.dev.singular.overview.domain.usecase.catalog.IGetAllCatalogUseCase
import br.dev.singular.overview.domain.usecase.genre.FetchGenresByTypeUseCase
import br.dev.singular.overview.domain.usecase.genre.IFetchGenresByTypeUseCase
import br.dev.singular.overview.domain.usecase.genre.ISaveGenreUseCase
import br.dev.singular.overview.domain.usecase.genre.SaveGenreUseCase
import br.dev.singular.overview.domain.usecase.media.DeleteMediasUseCase
import br.dev.singular.overview.domain.usecase.media.DiscoverRemoteMediasUseCase
import br.dev.singular.overview.domain.usecase.media.GetAllLocalMediasUseCase
import br.dev.singular.overview.domain.usecase.media.IGetAllLocalMediasUseCase
import br.dev.singular.overview.domain.usecase.media.IGetRemoteMediasUseCase
import br.dev.singular.overview.domain.usecase.suggestion.DeleteSuggestionsUseCase
import br.dev.singular.overview.domain.usecase.suggestion.GetAllSuggestionsUseCase
import br.dev.singular.overview.domain.usecase.suggestion.IGetAllSuggestionsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Singleton
    @Provides
    @DeleteSuggestions
    fun provideDeleteSuggestionsUseCase(
        repo: SuggestionRepository
    ): IDeleteUseCase {
        return DeleteSuggestionsUseCase(getter = repo, deleter = repo)
    }

    @Singleton
    @Provides
    fun provideGetAllSuggestionsUseCase(
        suggestionRepo: SuggestionRepository,
        mediaRepo: MediaRemoteRepository,
    ): IGetAllSuggestionsUseCase {
        return GetAllSuggestionsUseCase(
            getterSuggestion = suggestionRepo,
            getterMedia = mediaRepo
        )
    }

    @Singleton
    @Provides
    fun provideGetAllLocalMediasUseCase(
        repo: MediaLocalRepository
    ): IGetAllLocalMediasUseCase {
        return GetAllLocalMediasUseCase(getter = repo)
    }

    // TODO: use same use case to search movies and tv shows
    @Singleton
    @Provides
    fun provideDiscoverRemoteMediasUseCase(
        repo: MediaRemoteRepository
    ): IGetRemoteMediasUseCase {
        return DiscoverRemoteMediasUseCase(getter = repo)
    }

    @Singleton
    @Provides
    @DeleteMedias
    fun provideDeleteMediasUseCase(
        repo: MediaLocalRepository
    ): IDeleteUseCase {
        return DeleteMediasUseCase(getter = repo, deleter = repo)
    }

    @Singleton
    @Provides
    fun provideGetPersonByIdUseCase(
        repo: PersonRepository
    ): IGetPersonByIdUseCase {
        return GetPersonByIdUseCase(getter = repo)
    }

    @Singleton
    @Provides
    fun provideGetAllCatalogUseCase(
        repo: CatalogRepository
    ): IGetAllCatalogUseCase {
        return GetAllCatalogUseCase(getter = repo)
    }

    @Singleton
    @Provides
    fun provideSalveGenreUseCase(
        repoLocal: GenreLocalRepository,
        repoRemote: GenreRemoteRepository
    ): ISaveGenreUseCase {
        return SaveGenreUseCase(setter = repoLocal, getter = repoRemote)
    }

    @Singleton
    @Provides
    fun provideFetchGenresByTypeUseCase(
        repo: GenreLocalRepository
    ): IFetchGenresByTypeUseCase {
        return FetchGenresByTypeUseCase(getter = repo)
    }

    @Singleton
    @Provides
    @DeleteCatalog
    fun provideDeleteCatalogUseCase(
        repo: CatalogRepository
    ): IDeleteUseCase {
        return DeleteCatalogUseCase(getter = repo, deleter = repo)
    }

    @Singleton
    @Provides
    fun provideCatalogTooltipDismissedUseCase(
        @CatalogTooltip repo: DataStoreRepository<Boolean>
    ): ICatalogTooltipDismissedUseCase {
        return CatalogTooltipDismissedUseCase(getter = repo, updater = repo)
    }

    @Singleton
    @Provides
    fun provideICatalogQueryUseCase(
        repo: CatalogQueryLocalRepository
    ): ICatalogQueryStateUseCase {
        return CatalogQueryStateUseCase(getter = repo, updater = repo, observer = repo)
    }
}
