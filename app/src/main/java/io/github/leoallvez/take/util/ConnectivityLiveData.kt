package io.github.leoallvez.take.util

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import javax.inject.Inject

private fun Application.getConnectivityManager(): ConnectivityManager {
    return this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}

class ConnectivityLiveData(
    private val _connectivityManager: ConnectivityManager
) : LiveData<Boolean>() {

    @Inject constructor(app: Application) : this(app.getConnectivityManager())

    private val _networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            postValue(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            postValue(false)
        }
    }

    override fun onActive() {
        super.onActive()
        val builder = NetworkRequest.Builder()
        _connectivityManager.registerNetworkCallback(builder.build(),_networkCallback)
    }

    override fun onInactive() {
        super.onInactive()
        _connectivityManager.unregisterNetworkCallback(_networkCallback)
    }
}
