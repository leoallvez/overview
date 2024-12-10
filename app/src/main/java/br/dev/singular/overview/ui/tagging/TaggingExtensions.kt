package br.dev.singular.overview.ui.tagging

fun String.toTagging(): String {
    return this.replace(" ", "-").lowercase()
}
