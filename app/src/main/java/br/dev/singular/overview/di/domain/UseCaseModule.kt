package br.dev.singular.overview.di.domain

import br.dev.singular.overview.data.repository.MediaLocalRepository
import br.dev.singular.overview.data.repository.MediaRemoteRepository
import br.dev.singular.overview.data.repository.PersonRepository
import br.dev.singular.overview.data.repository.SuggestionRepository
import br.dev.singular.overview.domain.usecase.GetPersonByIdUseCase
import br.dev.singular.overview.domain.usecase.IDeleteUseCase
import br.dev.singular.overview.domain.usecase.IGetPersonByIdUseCase
import br.dev.singular.overview.domain.usecase.media.DeleteMediasUseCase
import br.dev.singular.overview.domain.usecase.media.GetAllLocalMediasUseCase
import br.dev.singular.overview.domain.usecase.media.IGetAllLocalMediasUseCase
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
        return DeleteSuggestionsUseCase(repo, repo)
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
    @DeleteMedias
    fun provideDeleteMediasUseCase(
        repo: MediaLocalRepository
    ): IDeleteUseCase {
        return DeleteMediasUseCase(repo, repo)
    }

    @Singleton
    @Provides
    fun provideGetPersonByIdUseCase(
        repo: PersonRepository
    ): IGetPersonByIdUseCase {
        return GetPersonByIdUseCase(repo)
    }
}
