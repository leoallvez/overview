package br.dev.singular.overview.presentation.tagging

import android.os.Bundle

internal fun Map<String, Any>.toBundle() = Bundle().apply {
    forEach { (key, value) ->
        if (key.isNotBlank()) {
            when (value) {
                is String -> { if (value.isNotBlank()) putString(key, value) }
                is Long -> { if (value != 0L) putLong(key, value) }
            }
        }
    }
}
