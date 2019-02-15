package io.mysocialapp.client.models


/**
 * Created by evoxmusic on 15/09/15.
 */
class UserMentionTag : TagEntityAbstract() {

    val mentionedUser: User? = null
    val mIndices: IntArray? = null
    val target: Feed? = null
    private var startIndex: Int = 0
    private var endIndex: Int = 0

    override var text: String? = null
        get() = mentionedUser?.displayedName ?: ""

    override val textShown: String?
        get() {
            if (text == null) {
                return null
            }

            val sb = StringBuilder()
            val sString = text?.split(" ".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray() ?: emptyArray()

            for (i in sString.indices) {
                sb.append(sString[i])
                if (i < sString.size - 1)
                    sb.append("_")
            }

            return sb.toString()
        }

    override var indices: IntArray?
        get() = intArrayOf(startIndex, endIndex)
        set(indices) {
            if (indices == null) {
                this.startIndex = 0
                this.endIndex = 0
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
