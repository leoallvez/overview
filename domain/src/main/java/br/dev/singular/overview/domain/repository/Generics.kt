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

interface GetById<T> {
    suspend fun getById(id: Int): T?
}

interface Writer<T> : Create<T>, Update<T>, Delete<T>

interface Reader<T> : GetAll<T>, GetById<T>

interface Repository<T> : Writer<T>, Reader<T>
