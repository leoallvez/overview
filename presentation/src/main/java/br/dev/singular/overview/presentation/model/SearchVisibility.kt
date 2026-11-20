package br.dev.singular.overview.presentation.model

private const val SEARCH_MIN_ITEMS = 12

/**
 * Defines whether a list screen should display its search field.
 *
 * @param itemCount The number of items available to be searched.
 * @param query The current search query.
 * @return True when there are enough items to search or when a query is being typed.
 */
internal fun shouldShowSearch(itemCount: Int, query: String): Boolean =
    itemCount >= SEARCH_MIN_ITEMS || query.isNotEmpty()
