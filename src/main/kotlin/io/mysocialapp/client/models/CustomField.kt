package io.mysocialapp.client.models

import java.io.Serializable
import java.util.*

/**
 * Created by evoxmusic on 14/02/2018.
 */
data class CustomField(val field: Field? = null,
                       var data: Data? = null) : Serializable {

    data class Field(val id: Long? = null,
                     val idStr: String? = null,
                     val fieldType: FieldType? = null,
                     val createdDate: Date? = null,
                     val updatedDate: Date? = null,
                     val accessControl: AccessControl? = null,
                     val position: Int? = null,
                     val important: Boolean? = null,
                     val defaultValue: Int? = null,
                     val labels: Map<UserSettings.InterfaceLanguage, String?>? = null,
                     val descriptions: Map<UserSettings.InterfaceLanguage, String?>? = null,
                     val placeholders: Map<UserSettings.InterfaceLanguage, String?>? = null,
                     val values: Map<UserSettings.InterfaceLanguage, List<String>>? = null) : Serializable

    data class Data(val fieldId: Long? = null,
                    val fieldIdStr: String? = null,
                    val createdDate: Date? = null,
                    val updatedDate: Date? = null,
                    var value: Any? = null) : Serializable

    enum class FieldType {
        INPUT_TEXT,
        INPUT_TEXTAREA,
        INPUT_NUMBER,
        INPUT_BOOLEAN,
        INPUT_DATE,
        INPUT_URL,
        INPUT_EMAIL,
        INPUT_PHONE,
        INPUT_LOCATION,
        INPUT_SELECT,
        INPUT_CHECKBOX,
    }

}