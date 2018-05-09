package io.mysocialapp.client.models

import java.io.Serializable

/**
 * Created by evoxmusic on 15/08/16.
 */
interface BaseLocation : Serializable {

    val latitude: Double?
    val longitude: Double?

}