package br.dev.singular.overview.domain.repository

interface Create<T> {
    suspend fun create(vararg items: T)
}

interface Update<T> {
    suspend fun update(vararg items: T)
}

interface GetAll<T> {
    suspend fun getAll(): List<T>
}

interface GetById<T> {
    suspend fun getById(id: Int): T?
}

interface Delete {
    suspend fun deleteById(id: Int)
}

interface Writer<T> : Create<T>, Update<T>, Delete

interface Reader<T> : GetAll<T>, GetById<T>

interface Repository<T> : Writer<T>, Reader<T>
