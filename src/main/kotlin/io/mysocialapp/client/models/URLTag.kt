package io.mysocialapp.client.models

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by evoxmusic on 21/11/15.
 */
class URLTag : TagEntityAbstract() {

    @JsonProperty("original_url")
    var originalURL: String? = null
    @JsonProperty("original_url_to_display")
    var originalURLToDisplay: String? = null
    @JsonProperty("original_host_url")
    var originalHostURL: String? = null
    @JsonProperty("short_url")
    var shortURL: String? = null
    var title: String? = null
    var description: String? = null
    @JsonProperty("preview_url")
    var previewURL: String? = null
    var startIndex: Int = 0
    var endIndex: Int = 0

    override var text: String?
        get() = originalURL
        set(text) {

        }

    override var indices: IntArray?
        get() = intArrayOf(startIndex, endIndex)
        set(indices) {
            if (indices == null) {
                return
            }

            this.startIndex = indices[0]
            this.endIndex = indices[1]
        }

    override val textShown: String?
        get() = originalURLToDisplay


}
