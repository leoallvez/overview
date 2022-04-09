package io.github.leoallvez.take.data.api.repository.discovery

import io.github.leoallvez.take.data.db.TakeDatabase
import io.github.leoallvez.take.data.db.dao.MovieDao
import io.github.leoallvez.take.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DiscoveryDataSource @Inject constructor(
    private val database: TakeDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    private val movieDao: MovieDao by lazy {
        database.movieDao()
    }
}
