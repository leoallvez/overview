package io.github.leoallvez.take

import android.app.Application
import android.util.Log
import com.google.firebase.remoteconfig.*
import io.github.leoallvez.firebase.RemoteConfigWrapper

class TakeApp : Application() {

    override fun onCreate() {
        super.onCreate()
        val remote = RemoteConfigWrapper(FirebaseRemoteConfig.getInstance())
        remote.start {
            val name = remote.getString(key = "my_name")
            Log.i("my_name", name)
        }
    }
}