package io.mysocialapp.client

import io.mysocialapp.client.extensions.PaginationResource
import io.mysocialapp.client.extensions.stream
import io.mysocialapp.client.models.*
import rx.Observable

/**
 * Created by evoxmusic on 28/04/2018.
 */
class FluentNewsFeed(private val session: Session) {

    @JvmOverloads
    fun blockingStream(limit: Int = Int.MAX_VALUE): Iterable<Feed> = stream(limit).toBlocking().toIterable()

    @JvmOverloads
    fun stream(limit: Int = Int.MAX_VALUE): Observable<Feed> = list(0, limit)

    @JvmOverloads
    fun blockingList(page: Int = 0, size: Int = 10): Iterable<Feed> = list(page, size).toBlocking().toIterable()

    @JvmOverloads
    fun list(page: Int = 0, size: Int = 10): Observable<Feed> {
        return stream(page, size, object : PaginationResource<Feed> {
            override fun getRealResultObject(response: List<Feed>): List<Any>? = response

            override fun onNext(page: Int, size: Int): List<Feed> {
                return session.clientService.feed.list(page, size).toBlocking().first() ?: emptyList()
            }
        }).map { it.session = session; it }
    }

    fun blockingGet(id: Long): Feed? = get(id).toBlocking()?.first()

    fun get(id: Long): Observable<Feed> = session.clientService.feed.get(id).map { it.session = session; it }

    fun blockingCreate(feedPost: FeedPost): Feed? = create(feedPost).toBlocking().first()

    fun create(feedPost: FeedPost): Observable<Feed> {
        return session.account.get().map { it.blockingCreateFeedPost(feedPost) }
    }

    @Deprecated("use blockingCreate(..) instead", ReplaceWith("blockingCreate(feedPost)"))
    fun blockingSendWallPost(feedPost: FeedPost): Feed? = sendWallPost(feedPost).toBlocking().first()

    @Deprecated("use create(..) instead")
    fun sendWallPost(feedPost: FeedPost): Observable<Feed> {
        return session.account.get().map { it.blockingCreateFeedPost(feedPost) }
    }

    @JvmOverloads
    fun blockingSearch(search: FluentNewsFeed.Search, page: Int = 0, size: Int = 10): Iterable<FeedsSearchResult> =
            search(search, page, size).toBlocking().toIterable()

    @JvmOverloads
    fun search(search: FluentNewsFeed.Search, page: Int = 0, size: Int = 10): Observable<FeedsSearchResult> {
        val queryParams = search.toQueryParams()

        return stream(page, size, object : PaginationResource<FeedsSearchResult?> {
            override fun getRealResultObject(response: List<FeedsSearchResult?>): List<Any>? = response.firstOrNull()?.data

            override fun onNext(page: Int, size: Int): List<FeedsSearchResult?> {
                return listOf(session.clientService.search.get(page, size, queryParams).map {
                    it.resultsByType?.feeds ?: FeedsSearchResult(matchedCount = 0L)
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
            private var mSortOrder: SortOrder? = null

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

            fun setOrder(sortOrder: SortOrder): Builder {
                this.mSortOrder = sortOrder
                return this
            }

            fun build(): Search {
                return Search(SearchQuery(user = User(
                        firstName = mFirstName,
                        lastName = mLastName,
                        gender = mGender,
                        livingLocation = mLivingLocation?.let { Location(location = it) }
                ), q = mTextToSearch, maximumDistanceInMeters = mLivingLocationMaximumDistance, sortOrder = mSortOrder))
            }
        }

        override fun toQueryParams(): MutableMap<String, String> {
            val m = super.toQueryParams()
            m["type"] = "FEED" // limit responses to Feed type
            return m
        }

    }

}