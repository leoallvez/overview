package io.github.leoallvez.take.experiment

interface AbExperiment<T> {
    fun execute(): T?
}