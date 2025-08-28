package br.dev.singular.overview.di.domain

import br.dev.singular.overview.data.repository.MediaLocalRepository
import br.dev.singular.overview.data.repository.MediaRemoteRepository
import br.dev.singular.overview.data.repository.SuggestionRepository
import br.dev.singular.overview.domain.usecase.media.GetAllLocalMediasUseCase
import br.dev.singular.overview.domain.usecase.media.IGetAllLocalMediasUseCase
import br.dev.singular.overview.domain.usecase.media.DeleteMediasCacheUseCase
import br.dev.singular.overview.domain.usecase.suggestion.DeleteSuggestionsUseCase
import br.dev.singular.overview.domain.usecase.suggestion.GetAllSuggestionsUseCase
import br.dev.singular.overview.domain.usecase.media.IDeleteMediasCacheUseCase
import br.dev.singular.overview.domain.usecase.suggestion.IDeleteSuggestionsUseCase
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
    fun provideDeleteSuggestionUseCase(
        repo: SuggestionRepository
    ): IDeleteSuggestionsUseCase {
        return DeleteSuggestionsUseCase(repo)
    }

    @Singleton
    @Provides
    fun provideGetAllSuggestionsUseCase(
        suggestionRepo: SuggestionRepository,
        mediaRepo: MediaRemoteRepository,
    ): IGetAllSuggestionsUseCase {
        return GetAllSuggestionsUseCase(
            suggestionRepo,
            mediaRepo
        )
    }

    @Singleton
    @Provides
    fun provideGetAllLocalMediasUseCase(
        repo: MediaLocalRepository
    ): IGetAllLocalMediasUseCase {
        return GetAllLocalMediasUseCase(repo)
    }

    @Singleton
    @Provides
    fun provideDeleteMediasCacheUseCase(
        repo: MediaLocalRepository
    ): IDeleteMediasCacheUseCase {
        return DeleteMediasCacheUseCase(repo, repo)
    }
}
