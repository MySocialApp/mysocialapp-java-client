package io.mysocialapp.client

import io.mysocialapp.client.models.SearchQuery

/**
 * Created by evoxmusic on 11/05/2018.
 */
interface ISearch {

    val searchQuery: SearchQuery

    fun toQueryParams(): MutableMap<String, String> {
        val m = mutableMapOf<String, String>()

        searchQuery.q?.let { m["q"] = it }

        searchQuery.user?.firstName?.let { m["first_name"] = it }
        searchQuery.user?.lastName?.let { m["last_name"] = it }
        searchQuery.user?.presentation?.let { m["content"] = it }
        searchQuery.user?.gender?.let { m["gender"] = it.name }
        searchQuery.user?.livingLocation?.let {
            it.latitude?.toString()?.let { m["latitude"] = it }
            it.longitude?.toString()?.let { m["longitude"] = it }
        }

        searchQuery.maximumDistanceInMeters?.toString()?.let { m["maximum_distance"] = it }

        return m
    }

}