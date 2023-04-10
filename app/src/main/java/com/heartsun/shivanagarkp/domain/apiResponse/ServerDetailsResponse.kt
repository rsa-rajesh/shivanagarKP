package com.heartsun.shivanagarkp.domain.apiResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ServerDetailsResponse(

    @SerializedName("response_code")
    @Expose
    val responseCode: String,

    @SerializedName("response_message")
    @Expose
    val responseMessage: String,

    @SerializedName("database_name")
    @Expose
    val databaseName: String?,

    @SerializedName("db_server_name")
    @Expose
    val dbServerName: String?,

    @SerializedName("db_user_name")
    @Expose
    val dbUserName: String?,

    @SerializedName("db_password")
    @Expose
    val dbPassword: String?
)

data class WalletAuthResponse(

    @SerializedName("response_code")
    @Expose
    val responseCode: String,

    @SerializedName("response_message")
    @Expose
    val responseMessage: String,

    @SerializedName("v_username")
    @Expose
    val v_username: String?,

    @SerializedName("v_password")
    @Expose
    val v_password: String?,

    )

data class WalletTokenResponse(

    @SerializedName("token")
    @Expose
    val token: String,

    )

data class BillInquiryResponse(

    @SerializedName("response_code")
    @Expose
    val response_code: String,

    @SerializedName("response_message")
    @Expose
    val response_message: String,

    @SerializedName("amount")
    @Expose
    val amount: Float,

    @SerializedName("properties")
    @Expose
    val properties: Propertiess

)

data class Propertiess(

    @SerializedName("MemID")
    @Expose
    val MemID: String,

    @SerializedName("Name")
    @Expose
    val Name: String,

    @SerializedName("Address")
    @Expose
    val Address: String,

    @SerializedName("Due")
    @Expose
    val Due: Float,

    @SerializedName("Discount")
    @Expose
    val Discount: Float,

    @SerializedName("Charge")
    @Expose
    val Charge: Float
)


data class ConfirmPaymentResponse(

    @SerializedName("response_code")
    @Expose
    val response_code: String,

    @SerializedName("request_id")
    @Expose
    val request_id: String,

    @SerializedName("response_message")
    @Expose
    val response_message: String,

    @SerializedName("amount")
    @Expose
    val amount: String,

    @SerializedName("reference_code")
    @Expose
    val reference_code: String,
)

data class ConfirmPaymentSuccessResponse(

    @SerializedName("response_code")
    @Expose
    val response_code: String,

    @SerializedName("response_message")
    @Expose
    val response_message: String,


)