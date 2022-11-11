package br.com.deepbyte.take.abtest

interface AbTest<T> {
    fun execute(): T
}
