package io.mysocialapp.client.models

/**
 * Created by evoxmusic on 23/05/2018.
 */
data class SearchResults(val matchedCount: Long? = null,
                         val queryId: String? = null,
                         val page: Int? = null,
                         val size: Int? = null,
                         val matchedTypes: Set<SearchType>? = null,
                         val resultsByType: SearchResultTypes? = null)