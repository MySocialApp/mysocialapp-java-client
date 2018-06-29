package io.mysocialapp.client

import io.mysocialapp.client.extensions.PaginationResource
import io.mysocialapp.client.extensions.prepareAsync
import io.mysocialapp.client.extensions.stream
import io.mysocialapp.client.extensions.toRequestBody
import io.mysocialapp.client.models.*
import rx.Observable

/**
 * Created by evoxmusic on 01/06/2018.
 */
class FluentGroup(private val session: Session) {

    fun blockingStream(limit: Int = Int.MAX_VALUE): Iterable<Group> = stream(limit).toBlocking().toIterable()

    fun stream(limit: Int = Int.MAX_VALUE): Observable<Group> = list(0, limit)

    fun blockingList(page: Int = 0, size: Int = 10): Iterable<Group> = list(page, size).toBlocking().toIterable()

    fun list(page: Int = 0, size: Int = 10): Observable<Group> {
        return stream(page, size, object : PaginationResource<Group> {
            override fun onNext(page: Int, size: Int): List<Group> {
                return session.clientService.group.list(page, size, false).toBlocking().first()
            }
        }).map { it.session = session; it }
    }

    fun blockingGet(id: Long): Group? = get(id).toBlocking()?.first()

    fun get(id: Long): Observable<Group> = session.clientService.group.get(id).map { it.session = session; it }

    fun blockingCreate(group: Group): Group? = create(group).toBlocking()?.first()

    fun create(group: Group): Observable<Group> {
        return session.clientService.group.post(group).map { it.session = session; it }.flatMap { g ->
            if (group.profileImageFile != null) {
                session.clientService.groupProfilePhoto.post(g.id, group.profileImageFile?.toRequestBody()!!).map { it.session = session;g }.prepareAsync()
            } else {
                Observable.just(g)
            }
        }.flatMap { g ->
            if (group.profileCoverImageFile != null) {
                session.clientService.groupProfileCoverPhoto.post(g.id, group.profileCoverImageFile?.toRequestBody()!!).map { it.session = session;g }.prepareAsync()
            } else {
                Observable.just(g)
            }
        }
    }

    fun blockingGetAvailableCustomFields(): Iterable<CustomField> = getAvailableCustomFields().toBlocking().toIterable()

    fun getAvailableCustomFields(): Observable<CustomField> = session.clientService.groupCustomField.list().flatMapIterable { it }

    fun blockingSearch(search: Search, page: Int = 0, size: Int = 10): Iterable<GroupsSearchResult> =
            search(search, page, size).toBlocking().toIterable()

    fun search(search: Search, page: Int = 0, size: Int = 10): Observable<GroupsSearchResult> {
        val queryParams = search.toQueryParams()

        return stream(page, size, object : PaginationResource<GroupsSearchResult?> {
            override fun onNext(page: Int, size: Int): List<GroupsSearchResult?> {
                return listOf(session.clientService.search.get(page, size, queryParams).map {
                    it.resultsByType?.groups ?: GroupsSearchResult(matchedCount = 0L)
                }.toBlocking().first())
            }
        }).map { it?.data?.forEach { u -> u.session = session }; it }
    }

    class Search(override val searchQuery: SearchQuery) : ISearch {

        class Builder {
            private var mName: String? = null
            private var mDescription: String? = null
            private var mFirstName: String? = null
            private var mLastName: String? = null
            private var mGender: Gender? = null
            private var mLocation: SimpleLocation? = null
            private var mLocationMaximumDistance: Double? = null
            private var mSortOrder: SortOrder? = null

            fun setName(name: String): Builder {
                this.mName = name
                return this
            }

            fun setDescription(description: String): Builder {
                this.mDescription = description
                return this
            }

            fun setOwnerFirstName(firstName: String): Builder {
                this.mFirstName = firstName
                return this
            }

            fun setOwnerLastName(lastName: String): Builder {
                this.mLastName = lastName
                return this
            }

            fun setLocation(location: SimpleLocation): Builder {
                this.mLocation = location
                return this
            }

            fun setLocationMaximumDistanceInMeters(maximumDistance: Double): Builder {
                this.mLocationMaximumDistance = maximumDistance
                return this
            }

            fun setLocationMaximumDistanceInKilometers(maximumDistance: Double): Builder {
                this.mLocationMaximumDistance = maximumDistance * 1000
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
                        livingLocation = mLocation?.let { Location(location = it) }
                ), name = mName, content = mDescription, maximumDistanceInMeters = mLocationMaximumDistance, sortOrder = mSortOrder))
            }
        }

        override fun toQueryParams(): MutableMap<String, String> {
            val m = super.toQueryParams()
            m["type"] = "GROUP" // limit responses to Group type
            return m
        }

    }
}