package br.dev.singular.overview.di.data

import androidx.datastore.preferences.core.booleanPreferencesKey
import br.dev.singular.overview.data.local.source.DataStoreDataSource
import br.dev.singular.overview.data.repository.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CatalogTooltip

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    @CatalogTooltip
    fun provideCatalogTooltipRepository(
        dataSource: DataStoreDataSource
    ): DataStoreRepository<Boolean> {
        return DataStoreRepository(
            key = booleanPreferencesKey(name = "catalog_tooltip_dismissed"),
            dataSource = dataSource,
            defaultValue = false
        )
    }

}
