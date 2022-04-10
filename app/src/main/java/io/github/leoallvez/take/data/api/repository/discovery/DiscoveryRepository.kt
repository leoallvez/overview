package io.github.leoallvez.take.data.api.repository.discovery

import io.github.leoallvez.take.data.model.ListSetup
import io.github.leoallvez.take.di.IoDispatcher
import io.github.leoallvez.take.experiment.ListSetupExperiment
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DiscoveryRepository @Inject constructor(
    private val dataSource: DiscoveryDataSource,
    private val experiment: ListSetupExperiment,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getListsSetup(): List<ListSetup> {
        return withContext(ioDispatcher) {
            experiment.execute()
        }
    }
}