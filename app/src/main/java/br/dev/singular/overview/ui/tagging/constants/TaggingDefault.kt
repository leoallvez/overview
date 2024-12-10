package br.dev.singular.overview.ui.tagging.constants

import br.dev.singular.overview.data.model.media.Media

sealed class TaggingDefault(val path: String) {
    object DETAILS {
        fun toTaggingMedia(media: Media): String {
            return "media:${media.getType()}}"
        }
    }
}
