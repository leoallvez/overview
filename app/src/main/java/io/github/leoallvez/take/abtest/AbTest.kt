package io.github.leoallvez.take.abtest

interface AbTest<T> {
    fun execute(): T
}
