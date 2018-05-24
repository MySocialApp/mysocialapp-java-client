package io.mysocialapp.client

import io.mysocialapp.client.extensions.PaginationResource
import io.mysocialapp.client.extensions.stream
import io.mysocialapp.client.models.*
import rx.Observable

/**
 * Created by evoxmusic on 28/04/2018.
 */
class FluentFeed(private val session: Session) {

    fun blockingStream(limit: Int = Int.MAX_VALUE): Iterable<Feed> = stream(limit).toBlocking().toIterable()

    fun stream(limit: Int = Int.MAX_VALUE): Observable<Feed> = list(0, limit)

    fun blockingList(page: Int = 0, size: Int = 10): Iterable<Feed> = list(page, size).toBlocking().toIterable()

    fun list(page: Int = 0, size: Int = 10): Observable<Feed> {
        return stream(page, size, object : PaginationResource<Feed> {
            override fun onNext(page: Int, size: Int): List<Feed> {
                return session.clientService.feed.list(page, size).toBlocking().first() ?: emptyList()
            }
        }).map { it.session = session; it }
    }

    fun blockingSendWallPost(textWallMessage: TextWallMessage): Feed? = sendWallPost(textWallMessage).toBlocking().first()

    fun sendWallPost(textWallMessage: TextWallMessage): Observable<Feed> {
        return session.account.get().map { it.blockingSendWallPost(textWallMessage) }
    }

    fun blockingSendWallPost(multipartPhoto: MultipartPhoto): Feed? = sendWallPost(multipartPhoto).toBlocking().first()

    fun sendWallPost(multipartPhoto: MultipartPhoto): Observable<Feed> {
        return session.account.get().map { it.blockingSendWallPost(multipartPhoto) }
    }

    fun blockingSearch(search: FluentFeed.Search, page: Int = 0, size: Int = 10): FeedsSearchResult? =
            search(search, page, size).toBlocking().first()

    fun search(search: FluentFeed.Search, page: Int = 0, size: Int = 10): Observable<FeedsSearchResult> {
        val queryParams = search.toQueryParams()

        return stream(page, size, object : PaginationResource<FeedsSearchResult?> {
            override fun onNext(page: Int, size: Int): List<FeedsSearchResult?> {
                return listOf(session.clientService.search.get(page, size, queryParams).map {
                    it.resultsByType?.feeds
                }.toBlocking().first())
            }
        }).map { it?.data?.forEach { u -> u.session = session }; it }
    }

    class Search(override val searchQuery: SearchQuery) : ISearch {

        class Builder {
            private var mFirstName: String? = null
            private var mLastName: String? = null
            private var mGender: Gender? = null
            private var mLivingLocation: SimpleLocation? = null
            private var mLivingLocationMaximumDistance: Double? = null
            private var mTextToSearch: String? = null

            fun setOwnerFirstName(firstName: String): Builder {
                this.mFirstName = firstName
                return this
            }

            fun setOwnerLastName(lastName: String): Builder {
                this.mLastName = lastName
                return this
            }

            fun setOwnerGender(gender: Gender): Builder {
                this.mGender = gender
                return this
            }

            fun setOwnerLivingLocation(location: SimpleLocation): Builder {
                this.mLivingLocation = location
                return this
            }

            fun setOwnerLivingLocationMaximumDistanceInMeters(maximumDistance: Double): Builder {
                this.mLivingLocationMaximumDistance = maximumDistance
                return this
            }

            fun setOwnerLivingLocationMaximumDistanceInKilometers(maximumDistance: Double): Builder {
                this.mLivingLocationMaximumDistance = maximumDistance * 1000
                return this
            }

            fun setTextToSearch(textToSearch: String): Builder {
                this.mTextToSearch = textToSearch
                return this
            }

            fun build(): Search {
                return Search(SearchQuery(user = User(
                        firstName = mFirstName,
                        lastName = mLastName,
                        gender = mGender,
                        livingLocation = mLivingLocation?.let { Location(location = it) }
                ), q = mTextToSearch, maximumDistanceInMeters = mLivingLocationMaximumDistance))
            }
        }

        override fun toQueryParams(): MutableMap<String, String> {
            val m = super.toQueryParams()
            m["type"] = "FEED" // limit responses to Feed type
            return m
        }

    }

}