package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.util.fakeMediaDataModel
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.WriteBatch
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.util.Date

class FavoriteRemoteDataSourceTest {

    private val firestore: FirebaseFirestore = mockk()
    private val favorites: CollectionReference = mockk()
    private val document: DocumentReference = mockk(relaxed = true)
    private val batch: WriteBatch = mockk(relaxed = true)
    private val query: Query = mockk()
    private val registration: ListenerRegistration = mockk(relaxed = true)

    private lateinit var sut: IFavoriteRemoteDataSource

    @Before
    fun setup() {
        every {
            firestore.collection("users").document("uid").collection("favorites")
        } returns favorites
        every { favorites.document(any()) } returns document
        every { firestore.batch() } returns batch
        sut = FavoriteRemoteDataSource(firestore)
    }

    @Test
    fun `save should write each model in a document keyed by type and id`() {
        // arrange
        val data = slot<Map<String, Any>>()
        every { batch.set(document, capture(data)) } returns batch

        // act
        sut.save("uid", listOf(fakeMediaDataModel))

        // assert
        verify(exactly = 1) { favorites.document("movie_1") }
        verify(exactly = 1) { batch.commit() }
        assertEquals(1L, data.captured["id"])
        assertEquals("movie", data.captured["type"])
        assertEquals("Test Name", data.captured["title"])
        assertEquals("/test.jpg", data.captured["poster_path"])
        assertEquals(fakeMediaDataModel.lastUpdate, data.captured["last_update"])
    }

    @Test
    fun `save should not commit anything when there are no models`() {
        // act
        sut.save("uid", emptyList())

        // assert
        verify(exactly = 0) { firestore.batch() }
    }

    @Test
    fun `save should split the models in batches of 500`() {
        // act
        sut.save("uid", List(501) { fakeMediaDataModel.copy(id = it.toLong()) })

        // assert
        verify(exactly = 2) { batch.commit() }
    }

    @Test
    fun `delete should delete the document keyed by type and id`() {
        // act
        sut.delete("uid", fakeMediaDataModel)

        // assert
        verify(exactly = 1) { favorites.document("movie_1") }
        verify(exactly = 1) { document.delete() }
    }

    @Test
    fun `observe should map the documents to liked models ordered by last update`() = runTest {
        // arrange
        val date = Date(1000)
        val document = mockk<DocumentSnapshot> {
            every { getLong("id") } returns 7L
            every { getString("type") } returns "tv"
            every { getString("title") } returns "Dark"
            every { getString("poster_path") } returns "/dark.jpg"
            every { getDate("last_update") } returns date
        }
        val snapshot = mockk<QuerySnapshot> { every { documents } returns listOf(document) }
        mockSnapshotListener { it.onEvent(snapshot, null) }

        // act
        val result = sut.observe("uid").first().single()

        // assert
        assertEquals(7L, result.id)
        assertEquals(MediaDataType.TV, result.type)
        assertEquals("Dark", result.betterTitle)
        assertEquals("/dark.jpg", result.posterPath)
        assertEquals(true, result.isLiked)
        assertEquals(date, result.lastUpdate)
        verify(exactly = 1) { favorites.orderBy("last_update", Query.Direction.DESCENDING) }
    }

    @Test
    fun `observe should remove the listener when the collection stops`() = runTest {
        // arrange
        val snapshot = mockk<QuerySnapshot> { every { documents } returns emptyList() }
        mockSnapshotListener { it.onEvent(snapshot, null) }

        // act
        sut.observe("uid").first()

        // assert
        verify(exactly = 1) { registration.remove() }
    }

    @Test
    fun `observe should throw when the listener fails`() {
        // arrange
        val error = mockk<FirebaseFirestoreException>(relaxed = true)
        mockSnapshotListener { it.onEvent(null, error) }

        // act & assert
        assertThrows(FirebaseFirestoreException::class.java) {
            runTest { sut.observe("uid").first() }
        }
    }

    private fun mockSnapshotListener(onListen: (EventListener<QuerySnapshot>) -> Unit) {
        every { favorites.orderBy(any<String>(), any()) } returns query
        every { query.addSnapshotListener(any<EventListener<QuerySnapshot>>()) } answers {
            onListen(firstArg())
            registration
        }
    }
}
