package br.dev.singular.overview.di.domain

import br.dev.singular.overview.data.repository.MediaRepository
import br.dev.singular.overview.data.repository.SuggestionRepository
import br.dev.singular.overview.domain.usecase.media.GetAllFavoriteMediasUseCase
import br.dev.singular.overview.domain.usecase.media.IGetAllFavoriteMediasUseCase
import br.dev.singular.overview.domain.usecase.suggestion.DeleteSuggestionsUseCase
import br.dev.singular.overview.domain.usecase.suggestion.GetAllSuggestionsUseCase
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
    fun provideDeleteSuggestionUseCase(repo: SuggestionRepository): IDeleteSuggestionsUseCase {
        return DeleteSuggestionsUseCase(repo)
    }

    @Singleton
    @Provides
    fun provideGetAllSuggestionsUseCase(
        suggestionRepo: SuggestionRepository,
        mediaRepo: MediaRepository,
    ): IGetAllSuggestionsUseCase {
        return GetAllSuggestionsUseCase(
            suggestionRepo,
            mediaRepo
        )
    }

    @Singleton
    @Provides
    fun provideGetAllFavoriteMediasUseCase(repo: MediaRepository): IGetAllFavoriteMediasUseCase {
        return GetAllFavoriteMediasUseCase(repo)
    }
}
