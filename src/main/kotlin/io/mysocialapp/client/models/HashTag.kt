package io.mysocialapp.client.models

/**
 * Created by evoxmusic on 13/09/15.
 */
class HashTag(val mIndices: IntArray? = null) : TagEntityAbstract() {

    override var text: String? = null
    private var startIndex: Int = 0
    private var endIndex: Int = 0

    override val textShown: String?
        get() = text

    override var indices: IntArray?
        get() = intArrayOf(startIndex, endIndex)
        set(indices) {
            if (indices == null) {
                return
            }

            val xIndices = if (indices.size == 2) {
                indices
            } else if (mIndices?.size == 2) {
                mIndices
            } else {
                null
            } ?: return

            this.startIndex = xIndices[0]
            this.endIndex = xIndices[1]
        }


}
