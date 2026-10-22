package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.model.MediaDataType
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.Date
import javax.inject.Inject

interface IFavoriteRemoteDataSource {
    fun save(userId: String, models: List<MediaDataModel>)
    suspend fun saveAll(userId: String, models: List<MediaDataModel>)
    fun delete(userId: String, model: MediaDataModel)
    fun observe(userId: String): Flow<List<MediaDataModel>>
}

class FavoriteRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : IFavoriteRemoteDataSource {

    override fun save(userId: String, models: List<MediaDataModel>) {
        models.chunked(MAX_BATCH_SIZE).forEach { chunk ->
            val batch = firestore.batch()
            chunk.forEach { batch.set(document(userId, it), it.toDocument()) }
            batch.commit().addOnFailureListener { Timber.e(it) }
        }
    }

    override suspend fun saveAll(userId: String, models: List<MediaDataModel>) {
        models.chunked(MAX_BATCH_SIZE).forEach { chunk ->
            val batch = firestore.batch()
            chunk.forEach { batch.set(document(userId, it), it.toDocument()) }
            batch.commit().await()
        }
    }

    override fun delete(userId: String, model: MediaDataModel) {
        document(userId, model).delete().addOnFailureListener { Timber.e(it) }
    }

    override fun observe(userId: String): Flow<List<MediaDataModel>> = callbackFlow {
        val registration = favorites(userId)
            .orderBy(LAST_UPDATE, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                } else if (snapshot != null) {
                    trySend(snapshot.documents.map { it.toData() })
                }
            }
        awaitClose { registration.remove() }
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
