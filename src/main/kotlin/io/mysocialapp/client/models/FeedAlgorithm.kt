package io.mysocialapp.client.models

import io.mysocialapp.client.extensions.fromJSONStringToMap
import io.mysocialapp.client.utils.MyObjectMapper

/**
 * Created by evoxmusic on 12/01/2019.
 */
data class FeedAlgorithm(val customFeedRequest: Map<String, Any?>? = null) {

    class Builder {
        private var mCustomFeedRequest: Map<String, Any?>? = null

        fun setCustomFeedRequest(jsonString: String): Builder {
            this.mCustomFeedRequest = jsonString.fromJSONStringToMap(MyObjectMapper.objectMapper)
            return this
        }

        fun setCustomFeedRequest(map: Map<String, Any?>): Builder {
            this.mCustomFeedRequest = map
            return this
        }

        fun build() = FeedAlgorithm(mCustomFeedRequest)
    }

}
