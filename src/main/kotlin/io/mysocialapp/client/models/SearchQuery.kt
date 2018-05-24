package io.mysocialapp.client.models

/**
 * Created by evoxmusic on 24/05/2018.
 */
data class SearchQuery(val user: User? = null,
                       val q: String? = null,
                       val maximumDistanceInMeters: Double? = null)