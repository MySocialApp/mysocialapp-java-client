package io.mysocialapp.client.models

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by evoxmusic on 23/05/2018.
 */
data class SearchResultTypes(@JsonProperty("USER") val users: UsersSearchResult? = null,
                             @JsonProperty("FEED") val feeds: FeedsSearchResult? = null,
                             @JsonProperty("GROUP") val groups: GroupsSearchResult? = null,
                             @JsonProperty("EVENT") val events: EventsSearchResult? = null)

interface SearchResultValue<T> {
    val matchedCount: Long?
    val data: List<T>?
}

data class UsersSearchResult(override val matchedCount: Long? = null,
                             override val data: List<User>? = null) : SearchResultValue<User>

data class FeedsSearchResult(override val matchedCount: Long? = null,
                             override val data: List<Feed>? = null) : SearchResultValue<Feed>

data class GroupsSearchResult(override val matchedCount: Long? = null,
                              override val data: List<Group>? = null) : SearchResultValue<Group>

data class EventsSearchResult(override val matchedCount: Long? = null,
                              override val data: List<Event>? = null) : SearchResultValue<Event>