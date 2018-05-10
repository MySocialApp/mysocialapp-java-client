package io.mysocialapp.client.models

/**
 * Created by evoxmusic on 03/08/15.
 */
class Group : BaseWall(), WallTextable {

    var name: String? = null
    var description: String? = null
    var isOpen: Boolean = false
    var location: Location? = null
    var profilePhoto: Photo? = null
    var profileCoverPhoto: Photo? = null
    var members: List<GroupMember>? = null
    private var isMember: Boolean = false
    var isApprovable: Boolean = false
    var distanceInMeters: Int = 0
    var isSelected: Boolean = false
    var totalMembers: Int = 0
    var groupMemberAccessControl: GroupMemberAccessControl? = null
    var customFields: List<CustomField>? = null

    override val bodyImageURL: String?
        get() = if (profilePhoto != null) profilePhoto!!.bodyImageURL else null

}