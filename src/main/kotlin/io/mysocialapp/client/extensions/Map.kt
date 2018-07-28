package io.mysocialapp.client.extensions

import io.mysocialapp.client.models.*
import io.mysocialapp.client.utils.MyObjectMapper

/**
 * Created by evoxmusic on 28/07/2018.
 */
fun Map<String, Any?>.convertToBase(): Base? {
    return when (get("entity_type")?.let { EntityType.valueOf(it.toString()) }) {
        EntityType.TEXT_WALL_MESSAGE -> MyObjectMapper.objectMapper.convertValue(this, TextWallMessage::class.java)
        EntityType.PHOTO -> MyObjectMapper.objectMapper.convertValue(this, Photo::class.java)
        EntityType.EVENT -> MyObjectMapper.objectMapper.convertValue(this, Event::class.java)
        EntityType.RIDE -> MyObjectMapper.objectMapper.convertValue(this, Ride::class.java)
        EntityType.GROUP -> MyObjectMapper.objectMapper.convertValue(this, Group::class.java)
        EntityType.STATUS -> MyObjectMapper.objectMapper.convertValue(this, Status::class.java)
        EntityType.USER -> MyObjectMapper.objectMapper.convertValue(this, User::class.java)
        EntityType.PHOTO_ALBUM -> MyObjectMapper.objectMapper.convertValue(this, PhotoAlbum::class.java)
        EntityType.CONVERSATION -> MyObjectMapper.objectMapper.convertValue(this, Conversation::class.java)
        EntityType.LOCATION -> MyObjectMapper.objectMapper.convertValue(this, Location::class.java)
        EntityType.COMMENT -> MyObjectMapper.objectMapper.convertValue(this, Comment::class.java)
        EntityType.LIKE -> MyObjectMapper.objectMapper.convertValue(this, Like::class.java)
        EntityType.URL_TAG -> MyObjectMapper.objectMapper.convertValue(this, URLTag::class.java)
        EntityType.HASH_TAG -> MyObjectMapper.objectMapper.convertValue(this, HashTag::class.java)
        EntityType.USER_MENTION_TAG -> MyObjectMapper.objectMapper.convertValue(this, UserMentionTag::class.java)
        EntityType.CONVERSATION_MESSAGE -> MyObjectMapper.objectMapper.convertValue(this, ConversationMessage::class.java)
        EntityType.STORY -> null
        null -> null
    }
}