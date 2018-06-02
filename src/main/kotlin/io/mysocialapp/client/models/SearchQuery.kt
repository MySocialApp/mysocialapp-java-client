package io.mysocialapp.client.models

import java.util.*

/**
 * Created by evoxmusic on 24/05/2018.
 */
data class SearchQuery(val user: User? = null,
                       val q: String? = null,
                       val name: String? = null,
                       val content: String? = null,
                       val maximumDistanceInMeters: Double? = null,
                       val sortOrder: SortOrder? = null,
                       val startDate: Date? = null,
                       val endDate: Date? = null,
                       val dateField: String? = null)