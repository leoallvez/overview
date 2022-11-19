package br.com.deepbyte.overview.abtest

interface AbTest<T> {
    fun execute(): T
}
