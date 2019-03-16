package io.mysocialapp.client

import io.mysocialapp.client.extensions.PaginationResource
import io.mysocialapp.client.extensions.stream
import io.mysocialapp.client.models.*
import rx.Observable

/**
 * Created by evoxmusic on 05/05/2018.
 */
class FluentUser(private val session: Session) {

    @JvmOverloads
    fun blockingStream(limit: Int = Int.MAX_VALUE, options: Options = Options(), requestAsAdmin: Boolean = false): Iterable<Users> =
            stream(limit, options, requestAsAdmin).toBlocking().toIterable()

    @JvmOverloads
    fun stream(limit: Int = Int.MAX_VALUE, options: Options = Options(), requestAsAdmin: Boolean = false): Observable<Users> =
            list(0, limit, options, requestAsAdmin)

    @JvmOverloads
    fun blockingList(page: Int = 0, size: Int = 10, options: Options = Options(), requestAsAdmin: Boolean = false): Iterable<Users> =
            list(page, size, options, requestAsAdmin).toBlocking().toIterable()

    @JvmOverloads
    fun list(page: Int = 0, size: Int = 10, options: Options = Options(), requestAsAdmin: Boolean = false): Observable<Users> {
        return stream(page, size, object : PaginationResource<Users> {
            override fun getRealResultObject(response: List<Users>): List<Any>? = response.firstOrNull()?.users

            override fun onNext(page: Int, size: Int): List<Users> {
                if (requestAsAdmin) {
                    return listOf(session.clientService.adminUser.list(page, size, options.queryParams).toBlocking().first())
                }

                return listOf(session.clientService.user.list(page, size, options.queryParams).toBlocking().first())
            }
        }).map { it.users?.forEach { u -> u.session = session }; it }
    }

    fun blockingGet(id: Long): User? = get(id).toBlocking()?.first()

    fun get(id: Long): Observable<User> = session.clientService.user.get(id).map { it.session = session; it }

    fun blockingGetByExternalId(id: String): User? = getByExternalId(id).toBlocking()?.first()

    fun getByExternalId(id: String): Observable<User> = session.clientService.userExternal.get(id).map { it.session = session; it }

    @JvmOverloads
    fun blockingSearch(search: Search, page: Int = 0, size: Int = 10): Iterable<UsersSearchResult> =
            search(search, page, size).toBlocking().toIterable()

    @JvmOverloads
    fun search(search: Search, page: Int = 0, size: Int = 10): Observable<UsersSearchResult> {
        val queryParams = search.toQueryParams()

        return stream(page, size, object : PaginationResource<UsersSearchResult?> {
            override fun getRealResultObject(response: List<UsersSearchResult?>): List<Any>? = response.firstOrNull()?.data

            override fun onNext(page: Int, size: Int): List<UsersSearchResult?> {
                return listOf(session.clientService.search.get(page, size, queryParams).map {
                    it.resultsByType?.users ?: UsersSearchResult(matchedCount = 0L)
                }.toBlocking().first())
            }
        }).map { it?.data?.forEach { u -> u.session = session }; it }
    }

    class Search(override val searchQuery: SearchQuery) : ISearch {

        class Builder {
            private var mFullName: String? = null
            private var mFirstName: String? = null
            private var mLastName: String? = null
            private var mGender: Gender? = null
            private var mLivingLocation: SimpleLocation? = null
            private var mLivingLocationMaximumDistance: Double? = null
            private var mPresentation: String? = null
            private var mSortOrder: SortOrder? = null

            fun setFullName(fullName: String): Builder {
                this.mFullName = fullName
                return this
            }

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

            fun setLocation(location: SimpleLocation): Builder {
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

            fun setOrder(sortOrder: SortOrder): Builder {
                this.mSortOrder = sortOrder
                return this
            }

            fun build(): Search {
                return Search(SearchQuery(user = User(
                        firstName = mFirstName,
                        lastName = mLastName,
                        gender = mGender,
                        presentation = mPresentation,
                        livingLocation = mLivingLocation?.let { Location(location = it) }
                ), maximumDistanceInMeters = mLivingLocationMaximumDistance, sortOrder = mSortOrder, q = mFullName))
            }
        }

        override fun toQueryParams(): MutableMap<String, String> {
            val m = super.toQueryParams()
            m["type"] = "USER" // limit responses to User type
            return m
        }
    }

    data class Options(val sortField: String? = null,
                       val location: BaseLocation? = null,
                       val limited: Boolean? = null) {

        class Builder {
            private var mSortField: String? = null
            private var mLocation: BaseLocation? = null
            private var mLimited: Boolean? = false

            fun setSortField(name: String): Builder {
                this.mSortField = name
                return this
            }

            fun setLocation(location: SimpleLocation): Builder {
                this.mLocation = location
                return this
            }

            fun setLimited(limited: Boolean): Builder {
                this.mLimited = limited
                return this
            }

            fun build(): Options {
                return Options(sortField = mSortField, location = mLocation, limited = mLimited)
            }
        }

        val queryParams: MutableMap<String, String> by lazy {
            val m = mutableMapOf<String, String>()

            sortField?.let { m["sort_field"] = it }

            location?.takeIf { it.latitude != null && it.longitude != null }?.let {
                m["latitude"] = it.latitude.toString()
                m["longitude"] = it.longitude.toString()
            }

            limited?.let { m["limited"] = it.toString() }

            m
        }
    }
}
