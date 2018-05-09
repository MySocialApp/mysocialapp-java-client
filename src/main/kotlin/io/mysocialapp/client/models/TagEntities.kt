package io.mysocialapp.client.models

import java.io.Serializable

/**
 * Created by evoxmusic on 02/09/2017.
 */
data class TagEntities(val userMentionTags: List<UserMentionTag>? = null,
                       val urlTags: List<URLTag>? = null,
                       val hashTags: List<HashTag>? = null) : Serializable {

    fun getAllTagEntities(): ArrayList<TagEntity> {
        val tagEntities = arrayListOf<TagEntity>()

        userMentionTags?.let { tagEntities.addAll(it) }
        urlTags?.let { tagEntities.addAll(it) }
        hashTags?.let { tagEntities.addAll(it) }

        return tagEntities
    }

}