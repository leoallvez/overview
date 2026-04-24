package br.dev.singular.overview.domain.repository

import kotlinx.coroutines.flow.Flow

interface Update<T> {
    suspend fun update(item: T)
}

interface Delete<T> {
    suspend fun delete(vararg items: T)
}

interface Get<T> {
    suspend fun get(): T
}

interface GetAll<T> {
    suspend fun getAll(): List<T>
}

interface GetPage<T, P> {
    suspend fun getPage(param: P): Page<T>
}

interface GetByParam<T, P> {
    suspend fun getByParam(param: P): T
}

interface GetById<T> {
    suspend fun getById(id: Long): T?
}

interface Observe<T> {
    fun observe(): Flow<T>
}

data class Page<T>(
    val items: List<T> = emptyList(),
    val currentPage: Int = 0,
    val isLastPage: Boolean = false
) {
    operator fun plus(other: Page<T>): Page<T> {
        return Page(
            items = this.items + other.items,
            currentPage = maxOf(this.currentPage, other.currentPage),
            isLastPage = this.isLastPage || other.isLastPage
        )
    }

    fun <R> map(transform: (T) -> R): Page<R> {
        return Page(
            items = items.map(transform),
            currentPage = currentPage,
            isLastPage = isLastPage
        )
    }
}