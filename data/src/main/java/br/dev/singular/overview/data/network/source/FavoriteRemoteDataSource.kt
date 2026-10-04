package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.model.MediaDataType
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.Date
import javax.inject.Inject

interface IFavoriteRemoteDataSource {
    fun save(userId: String, models: List<MediaDataModel>)
    fun delete(userId: String, model: MediaDataModel)
    suspend fun getAll(userId: String): List<MediaDataModel>
}

class FavoriteRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : IFavoriteRemoteDataSource {

    // Writes are not awaited because Firestore only completes them after the server
    // acknowledges, which never happens while offline. They are queued and sent later.
    override fun save(userId: String, models: List<MediaDataModel>) {
        models.chunked(MAX_BATCH_SIZE).forEach { chunk ->
            val batch = firestore.batch()
            chunk.forEach { batch.set(document(userId, it), it.toDocument()) }
            batch.commit().addOnFailureListener { Timber.e(it) }
        }
    }

    override fun delete(userId: String, model: MediaDataModel) {
        document(userId, model).delete().addOnFailureListener { Timber.e(it) }
    }

    override suspend fun getAll(userId: String): List<MediaDataModel> {
        return favorites(userId).get().await().documents.map { it.toData() }
    }

    private fun favorites(userId: String) =
        firestore.collection(USERS).document(userId).collection(FAVORITES)

    private fun document(userId: String, model: MediaDataModel) =
        favorites(userId).document("${model.type.key}_${model.id}")

    private fun MediaDataModel.toDocument() = mapOf(
        ID to id,
        TYPE to type.key,
        TITLE to betterTitle,
        POSTER_PATH to posterPath,
        LAST_UPDATE to lastUpdate
    )

    private fun DocumentSnapshot.toData(): MediaDataModel {
        val title = getString(TITLE).orEmpty()
        return MediaDataModel(
            id = getLong(ID) ?: 0,
            name = title,
            title = title,
            posterPath = getString(POSTER_PATH).orEmpty(),
            type = MediaDataType.fromKey(getString(TYPE).orEmpty()),
            isLiked = true,
            lastUpdate = getDate(LAST_UPDATE) ?: Date()
        )
    }

    private companion object {
        const val USERS = "users"
        const val FAVORITES = "favorites"
        const val ID = "id"
        const val TYPE = "type"
        const val TITLE = "title"
        const val POSTER_PATH = "poster_path"
        const val LAST_UPDATE = "last_update"
        const val MAX_BATCH_SIZE = 500
    }
}
