package io.mysocialapp.client.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

/**
 * Created by evoxmusic on 31/03/15.
 */
class Event : BaseWall(), WallTextable, Localizable {

    var name: String? = null
    var description: String? = null
    var isCancelled: Boolean = false
    var maxSeats: Int = 0
    var freeSeats: Int = 0 // can be used, but only when in query param or direct /event/:id limited=false
    var takenSeats: Int = 0 // can be used, but only when in query param or direct /event/:id limited=false
    var totalMembers: Int? = null
    var startDate: Date? = null
    var endDate: Date? = null
    var members: ArrayList<EventMember>? = null
    var eventMemberAccessControl: EventMemberAccessControl? = null
    var location: Location? = null
    var isMember: Boolean = false
    @JsonProperty("static_maps_url")
    var staticMapsURL: String? = null
    var report: String? = null
    var distanceInMeters: Int? = null
    var profileCoverPhoto: Photo? = null
    var isAvailable: Boolean = true
    var remainingSecondsBeforeStart: Long = 0
    var customFields: List<CustomField>? = null

    override val bodyImageURL: String?
        get() = displayedPhoto?.highURL ?: staticMapsURL

    override val bodyImageText: String?
        get() = displayedName

    override var bodyMessage: String?
        get() = displayedName
        set(value) = Unit

    override fun getLocality(): BaseLocation? {
        return location
    }

}
