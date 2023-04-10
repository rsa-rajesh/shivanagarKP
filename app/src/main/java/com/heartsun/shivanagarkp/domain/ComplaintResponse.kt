package com.heartsun.shivanagarkp.domain


import com.google.gson.annotations.SerializedName

data class ComplaintResponse(
    @SerializedName("ComptID")
    val ComptID: Int?,
    @SerializedName("MemberID")
    val MemberID: Int?,
    @SerializedName("ComplaintMsg")
    val ComplaintMsg: String?,
    @SerializedName("ComptDate")
    val ComptDate: String?,
    @SerializedName("IsRectified")
    val IsRectified: Int?
) {}