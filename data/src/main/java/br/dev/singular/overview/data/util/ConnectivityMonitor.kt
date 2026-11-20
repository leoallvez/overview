package br.dev.singular.overview.data.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

interface IConnectivityMonitor {
    val isOnline: Flow<Boolean>
}

class ConnectivityMonitor(
    private val connectivityManager: ConnectivityManager
) : IConnectivityMonitor {

    @Inject
    constructor(
        @ApplicationContext context: Context
    ) : this(context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)

    override val isOnline: Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            override fun onLost(network: Network) {
                trySend(false)
            }
        }

        trySend(connectivityManager.activeNetwork != null)
        connectivityManager.registerDefaultNetworkCallback(callback)

        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }.distinctUntilChanged()
}
