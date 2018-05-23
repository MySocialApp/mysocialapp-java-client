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

    fun blockingSearch(search: Search, page: Int = 0, size: Int = 10): Iterable<Users> = search(search).toBlocking().toIterable()

    fun search(search: Search, page: Int = 0, size: Int = 10): Observable<Users> {
        val queryParams = search.toQueryParams()

        return stream(page, size, object : PaginationResource<Users> {
            override fun onNext(page: Int, size: Int): List<Users> {
                return listOf(session.clientService.search.get(page, size, queryParams).map {
                    Users(it.resultsByType?.users?.matchedCount, it.resultsByType?.users?.data)
                }.toBlocking().first())
            }
        }).map { it.users?.forEach { u -> u.session = session }; it }
    }

    class Search(private val user: User) : ISearch {

        class Builder {
            private var mFirstName: String? = null
            private var mLastName: String? = null
            private var mGender: Gender? = null
            private var mLivingLocation: SimpleLocation? = null

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

            fun build(): Search {
                return Search(User(
                        firstName = mFirstName,
                        lastName = mLastName,
                        gender = mGender,
                        livingLocation = mLivingLocation?.let { Location(location = it) }
                ))
            }
        }

        override fun toQueryParams(): Map<String, String> {
            val m = mutableMapOf<String, String>()
            m["type"] = "USER" // limit responses to User type

            user.firstName?.let { m["first_name"] = it }
            user.lastName?.let { m["last_name"] = it }
            user.gender?.let { m["gender"] = it.name }
            user.livingLocation?.let {
                it.latitude?.toString()?.let { m["latitude"] = it }
                it.longitude?.toString()?.let { m["longitude"] = it }
            }

            return m
        }

    }
}