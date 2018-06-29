package io.mysocialapp.client

import io.mysocialapp.client.extensions.*
import io.mysocialapp.client.models.*
import rx.Observable
import java.util.*

/**
 * Created by evoxmusic on 02/06/2018.
 */
class FluentEvent(private val session: Session) {

    fun blockingStream(limit: Int = Int.MAX_VALUE, fromDate: Date = Date()): Iterable<Event> = stream(limit, fromDate).toBlocking().toIterable()

    fun stream(limit: Int = Int.MAX_VALUE, fromDate: Date = Date()): Observable<Event> = list(0, limit, fromDate)

    fun blockingList(page: Int = 0, size: Int = 10, fromDate: Date = Date()): Iterable<Event> = list(page, size, fromDate).toBlocking().toIterable()

    fun list(page: Int = 0, size: Int = 10, fromDate: Date = Date()): Observable<Event> {
        val queryMap = mapOf("sort_field" to "start_date", "date_field" to "start_date", "limited" to "false", "from_date" to fromDate.toISO8601())

        return stream(page, size, object : PaginationResource<Event> {
            override fun onNext(page: Int, size: Int): List<Event> {
                return session.clientService.event.list(page, size, queryMap).toBlocking().first()
            }
        }).map { it.session = session; it }
    }

    fun blockingGet(id: Long): Event? = get(id).toBlocking()?.first()

    fun get(id: Long): Observable<Event> = session.clientService.event.get(id).map { it.session = session; it }

    fun blockingCreate(event: Event): Event? = create(event).toBlocking()?.first()

    fun create(event: Event): Observable<Event> {
        return session.clientService.event.post(event).map { it.session = session; it }.flatMap { e ->
            if (event.profileImageFile != null) {
                session.clientService.eventProfilePhoto.post(e.id, event.profileImageFile?.toRequestBody()!!).map { it.session = session;e }.prepareAsync()
            } else {
                Observable.just(e)
            }
        }.flatMap { e ->
            if (event.profileCoverImageFile != null) {
                session.clientService.eventProfileCoverPhoto.post(e.id, event.profileCoverImageFile?.toRequestBody()!!).map { it.session = session;e }.prepareAsync()
            } else {
                Observable.just(e)
            }
        }
    }

    fun blockingGetAvailableCustomFields(): Iterable<CustomField> = getAvailableCustomFields().toBlocking().toIterable()

    fun getAvailableCustomFields(): Observable<CustomField> = session.clientService.eventCustomField.list().flatMapIterable { it }

    fun blockingSearch(search: Search, page: Int = 0, size: Int = 10): Iterable<EventsSearchResult> =
            search(search, page, size).toBlocking().toIterable()

    fun search(search: Search, page: Int = 0, size: Int = 10): Observable<EventsSearchResult> {
        val queryParams = search.toQueryParams()

        return stream(page, size, object : PaginationResource<EventsSearchResult?> {
            override fun onNext(page: Int, size: Int): List<EventsSearchResult?> {
                return listOf(session.clientService.search.get(page, size, queryParams).map {
                    it.resultsByType?.events ?: EventsSearchResult(matchedCount = 0L)
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
            private var mFromDate: Date? = null
            private var mToDate: Date? = null
            private var mDateField: DateField? = null

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

            fun setFromDate(date: Date): Builder {
                this.mFromDate = date
                return this
            }

            fun setToDate(date: Date): Builder {
                this.mToDate = date
                return this
            }

            fun setDateField(dateField: DateField): Builder {
                this.mDateField = dateField
                return this
            }

            fun build(): Search {
                return Search(SearchQuery(user = User(
                        firstName = mFirstName,
                        lastName = mLastName,
                        gender = mGender,
                        livingLocation = mLocation?.let { Location(location = it) }
                ), name = mName, content = mDescription, maximumDistanceInMeters = mLocationMaximumDistance,
                        sortOrder = mSortOrder, startDate = mFromDate, endDate = mToDate,
                        dateField = mDateField?.name ?: DateField.START_DATE.name))
            }

            enum class DateField {
                START_DATE,
                END_DATE
            }
        }

        override fun toQueryParams(): MutableMap<String, String> {
            val m = super.toQueryParams()
            m["type"] = "EVENT" // limit responses to Event type
            return m
        }
    }
}