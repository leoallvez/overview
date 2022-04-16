package io.github.leoallvez.take.data.repository.discovery

import io.github.leoallvez.take.data.db.dao.MovieDao
import io.github.leoallvez.take.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DiscoveryDataSource @Inject constructor(
    private val movie: MovieDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

}
