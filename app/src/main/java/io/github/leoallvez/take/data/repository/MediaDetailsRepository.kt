package io.github.leoallvez.take.data.repository

import android.util.Log
import javax.inject.Inject

class MediaDetailsRepository @Inject constructor() {

    fun getMediaDetails(id: Long, type: String) {
        Log.i("details_request", "id:$id, type:$type")
    }

}