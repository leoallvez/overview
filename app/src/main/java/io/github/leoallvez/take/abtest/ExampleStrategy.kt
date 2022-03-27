package io.github.leoallvez.take.abtest

import android.util.Log
import io.github.leoallvez.firebase.RemoteConfigKey.*
import io.github.leoallvez.firebase.RemoteSource
import io.github.leoallvez.take.data.model.Movie
import io.github.leoallvez.take.helper.fromJsonOrNull
import javax.inject.Inject

class ExampleStrategy @Inject constructor(
    private val remoteSource: RemoteSource,
) : AbStrategy {

    var valueToChange: Movie? = null
        private set

    override fun execute() {
        val json = remoteSource.getString(JSON_KEY)
        if(json.isNotEmpty()) {
            valueToChange = json.fromJsonOrNull()
            Log.i("json_example", "movie: $valueToChange")
        }
    }
}