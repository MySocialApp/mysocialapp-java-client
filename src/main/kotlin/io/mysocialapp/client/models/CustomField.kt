package io.mysocialapp.client.models

import java.io.Serializable
import java.util.*

/**
 * Created by evoxmusic on 14/02/2018.
 */
data class CustomField(val field: Field? = null,
                       val data: Data = Data(),
                       var interfaceLanguage: UserSettings.InterfaceLanguage = UserSettings.InterfaceLanguage.EN) : Serializable {

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

    data class Data(var fieldId: Long? = null,
                    var fieldIdStr: String? = null,
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

    val fieldType: FieldType?
        get() = this.field?.fieldType

    val fieldPosition: Int?
        get() = this.field?.position

    val label: String?
        get() = this.field?.labels?.get(interfaceLanguage)

    val description: String?
        get() = this.field?.descriptions?.get(interfaceLanguage)

    val placeholder: String?
        get() = this.field?.placeholders?.get(interfaceLanguage)

    val possibleValues: List<String>?
        get() = this.field?.values?.get(interfaceLanguage)

    private fun initDataFieldId() {
        data.fieldId = this.field?.id
        data.fieldIdStr = this.field?.idStr
    }

    var value: Any?
        get() = data.value
        set(value) {
            initDataFieldId()
            data.value = value
        }

    var booleanValue: Boolean?
        get() = data.value as? Boolean
        set(value) {
            initDataFieldId()
            data.value = if (value?.toString()?.isBlank() == true) null else value
        }

    var dateValue: Date?
        get() = data.value as? Date
        set(value) {
            initDataFieldId()
            data.value = if (value?.toString()?.isBlank() == true) null else value
        }

    var stringsValue: List<String>?
        get() = data.value as? List<String>
        set(value) {
            initDataFieldId()
            data.value = value
        }

    var stringValue: String?
        get() = data.value as? String
        set(value) {
            initDataFieldId()
            data.value = if (value?.isBlank() == true) null else value
        }

    var numberValue: Double?
        get() = data.value as? Double
        set(value) {
            initDataFieldId()
            data.value = if (value?.toString()?.isBlank() == true) null else value
        }

    var doubleValue: Double?
        get() = data.value as? Double
        set(value) {
            initDataFieldId()
            data.value = if (value?.toString()?.isBlank() == true) null else value
        }

    var locationValue: BaseLocation?
        get() = data.value as? SimpleLocation
        set(value) {
            initDataFieldId()
            data.value = if (value?.toString()?.isBlank() == true) null else value
        }

}