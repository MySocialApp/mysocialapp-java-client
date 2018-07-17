package io.mysocialapp.client.models

/**
 * Created by evoxmusic on 11/01/15.
 */
class Location(var location: SimpleLocation? = null) : Base(), BaseLocation {

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Location

        if (location != other.location) return false
        if (country != other.country) return false
        if (district != other.district) return false
        if (state != other.state) return false
        if (postalCode != other.postalCode) return false
        if (city != other.city) return false
        if (streetName != other.streetName) return false
        if (streetNumber != other.streetNumber) return false
        if (completeAddress != other.completeAddress) return false
        if (completeCityAddress != other.completeCityAddress) return false

        return true
    }

    override fun hashCode(): Int {
        var result = location?.hashCode() ?: 0
        result = 31 * result + (country?.hashCode() ?: 0)
        result = 31 * result + (district?.hashCode() ?: 0)
        result = 31 * result + (state?.hashCode() ?: 0)
        result = 31 * result + (postalCode?.hashCode() ?: 0)
        result = 31 * result + (city?.hashCode() ?: 0)
        result = 31 * result + (streetName?.hashCode() ?: 0)
        result = 31 * result + (streetNumber?.hashCode() ?: 0)
        result = 31 * result + (completeAddress?.hashCode() ?: 0)
        result = 31 * result + (completeCityAddress?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Location(location=$location, country=$country, district=$district, state=$state, postalCode=$postalCode, " +
                "city=$city, streetName=$streetName, streetNumber=$streetNumber, completeAddress=$completeAddress, " +
                "completeCityAddress=$completeCityAddress)"
    }

}
