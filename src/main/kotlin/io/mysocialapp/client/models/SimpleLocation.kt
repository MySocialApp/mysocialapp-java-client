package io.mysocialapp.client.models

import java.io.Serializable

/**
 * Created by evoxmusic on 11/08/16.
 */
data class SimpleLocation(override var latitude: Double? = null, override var longitude: Double? = null) : Serializable, BaseLocation