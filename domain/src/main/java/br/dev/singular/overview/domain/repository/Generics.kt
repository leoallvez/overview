package br.dev.singular.overview.domain.repository

interface Create<T> {
    suspend fun create(vararg items: T)
}

interface Update<T> {
    suspend fun update(vararg items: T)
}

interface Delete<T> {
    suspend fun delete(vararg items: T)
}

interface DeleteAll<T> {
    suspend fun deleteAll()
}

interface GetAll<T> {
    suspend fun getAll(): List<T>
}

interface GetPage<T, P> {
    suspend fun getPage(param: P): Page<T>
}

interface GetById<T> {
    suspend fun getById(id: Long): T?
}

data class Page<T>(
    val items: List<T> = emptyList(),
    val currentPage: Int = 0,
    val isLastPage: Boolean = false
)