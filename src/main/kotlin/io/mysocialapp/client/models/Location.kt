package io.mysocialapp.client.models

/**
 * Created by evoxmusic on 11/01/15.
 */
class Location : Base(), BaseLocation {

    var location: SimpleLocation? = null
    var country: String? = null
    var district: String? = null
    var state: String? = null
    var postalCode: String? = null
    var city: String? = null
    var streetName: String? = null
    var streetNumber: String? = null
    var completeAddress: String? = null
    var completeCityAddress: String? = null

    override var latitude: Double?
        get() = location?.latitude
        set(latitude) {
            this.location?.latitude = latitude
        }

    override var longitude: Double?
        get() = location?.longitude
        set(longitude) {
            this.location?.longitude = longitude
        }

}
