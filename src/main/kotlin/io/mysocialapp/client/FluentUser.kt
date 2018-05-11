package io.mysocialapp.client

import io.mysocialapp.client.extensions.PaginationResource
import io.mysocialapp.client.extensions.stream
import io.mysocialapp.client.models.BaseLocation
import io.mysocialapp.client.models.Location
import io.mysocialapp.client.models.User
import io.mysocialapp.client.models.Users
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

    fun blockingSearch(search: Search): Iterable<Users> = search(search).toBlocking().toIterable()

    fun search(search: Search): Observable<Users> = session.clientService.user.list(search.toQueryParams()).map {
        it.users?.forEach { u -> u.session = session }
        it
    }

    class Search(private val user: User) : ISearch {

        class Builder {
            private var mFirstName: String? = null
            private var mLastName: String? = null
            private var mLivingLocation: BaseLocation? = null

            fun setFirstName(firstName: String): Builder {
                this.mFirstName = firstName
                return this
            }

            fun setLastName(lastName: String): Builder {
                this.mLastName = lastName
                return this
            }

            fun setLivingLocation(location: BaseLocation): Builder {
                this.mLivingLocation = location
                return this
            }

            fun build() = Search(
                    User(firstName = mFirstName, lastName = mLastName, livingLocation = Location().apply {
                        latitude = mLivingLocation?.latitude ?: 0.0
                        longitude = mLivingLocation?.longitude ?: 0.0
                    })
            )
        }

        override fun toQueryParams(): Map<String, String> {
            val m = mutableMapOf<String, String>()

            user.firstName?.let { m["first_name"] = it }
            user.lastName?.let { m["last_name"] = it }
            user.livingLocation?.let {
                m["latitude"] = it.latitude!!.toString()
                m["longitude"] = it.longitude!!.toString()
            }

            return m
        }

    }

}