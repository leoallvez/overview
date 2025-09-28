package br.dev.singular.overview.presentation.model

enum class MediaUiType {
    MOVIE,
    TV,
    ALL,
    UNKNOWN;

    companion object {
        fun getByName(name: String) = entries.first { it.name.lowercase() == name }
    }
}
