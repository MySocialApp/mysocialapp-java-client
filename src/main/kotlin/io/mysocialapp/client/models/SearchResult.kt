package io.mysocialapp.client.models

/**
 * Created by evoxmusic on 23/05/2018.
 */
data class SearchResult(val matchedCount: Long = 0,
                        var data: List<Map<String, Any?>>? = null)