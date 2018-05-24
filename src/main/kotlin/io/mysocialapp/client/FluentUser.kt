package io.mysocialapp.client

import io.mysocialapp.client.extensions.PaginationResource
import io.mysocialapp.client.extensions.stream
import io.mysocialapp.client.models.*
import rx.Observable

/**
 * Created by evoxmusic on 05/05/2018.
 */
class FluentUser(private val session: Session) {

    fun blockingStream(limit: Int = Int.MAX_VALUE): Iterable<Users> = stream(limit).toBlocking().toIterable()

    fun stream(limit: Int = Int.MAX_VALUE): Observable<Users> = list(0, limit)

    fun blockingList(page: Int = 0, size: Int = 10): Iterable<Users> = list(page, size).toBlocking().toIterable()

    fun list(page: Int = 0, size: Int = 10): Observable<Users> {
        return stream(page, size, object : PaginationResource<Users> {
            override fun onNext(page: Int, size: Int): List<Users> {
                return listOf(session.clientService.user.list(page, size).toBlocking().first())
            }
        }).map { it.users?.forEach { u -> u.session = session }; it }
    }

    fun blockingGet(id: Long): User? = get(id).toBlocking()?.first()

    fun get(id: Long): Observable<User> = session.clientService.user.get(id).map { it.session = session; it }

    fun blockingGetByExternalId(id: String): User? = getByExternalId(id).toBlocking()?.first()

    fun getByExternalId(id: String): Observable<User> = session.clientService.userExternal.get(id).map { it.session = session; it }

    fun blockingSearch(search: Search, page: Int = 0, size: Int = 10): UsersSearchResult? =
            search(search, page, size).toBlocking().first()

    fun search(search: Search, page: Int = 0, size: Int = 10): Observable<UsersSearchResult> {
        val queryParams = search.toQueryParams()

        return stream(page, size, object : PaginationResource<UsersSearchResult?> {
            override fun onNext(page: Int, size: Int): List<UsersSearchResult?> {
                return listOf(session.clientService.search.get(page, size, queryParams).map {
                    it.resultsByType?.users
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
            private var mPresentation: String? = null

            fun setFirstName(firstName: String): Builder {
                this.mFirstName = firstName
                return this
            }

            fun setLastName(lastName: String): Builder {
                this.mLastName = lastName
                return this
            }

            fun setGender(gender: Gender): Builder {
                this.mGender = gender
                return this
            }

            fun setLivingLocation(location: SimpleLocation): Builder {
                this.mLivingLocation = location
                return this
            }

            fun setLivingLocationMaximumDistanceInMeters(maximumDistance: Double): Builder {
                this.mLivingLocationMaximumDistance = maximumDistance
                return this
            }

            fun setLivingLocationMaximumDistanceInKilometers(maximumDistance: Double): Builder {
                this.mLivingLocationMaximumDistance = maximumDistance * 1000
                return this
            }

            fun setPresentation(presentation: String): Builder {
                this.mPresentation = presentation
                return this
            }

            fun build(): Search {
                return Search(SearchQuery(user = User(
                        firstName = mFirstName,
                        lastName = mLastName,
                        gender = mGender,
                        presentation = mPresentation,
                        livingLocation = mLivingLocation?.let { Location(location = it) }
                ), maximumDistanceInMeters = mLivingLocationMaximumDistance))
            }
        }

        override fun toQueryParams(): MutableMap<String, String> {
            val m = super.toQueryParams()
            m["type"] = "USER" // limit responses to User type
            return m
        }

    }
}