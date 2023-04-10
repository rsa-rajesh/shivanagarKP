package com.heartsun.shivanagarkp.domain


import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("logistic_staff")
    val logisticStaff: String?,
    @SerializedName("notification_type")
    val notificationType: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("shipment")
    val shipment: Shipment?
) {
    data class Shipment(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("shipment_code")
        val shipmentCode: String?
    )
}