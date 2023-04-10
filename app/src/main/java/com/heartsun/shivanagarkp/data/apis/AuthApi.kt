package com.heartsun.shivanagarkp.data.apis

import com.heartsun.shivanagarkp.data.Prefs
import com.heartsun.shivanagarkp.di.DataSourceProperties.BASE_PATH
import com.heartsun.shivanagarkp.domain.apiResponse.*
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface AuthApi {
    companion object {
        private const val userId = Prefs.UserID
        private const val GetServerDetails = "$BASE_PATH/getcredentials.php"
        private const val GetWalletAuth = "$BASE_PATH/getcredentialsMemberApp.php"
        private const val GetWalletToken = "$BASE_PATH/tokenrequest.php"
        private const val getBillInquiry = "$BASE_PATH/billinquiry.php"
        private const val postPayment = "$BASE_PATH/savepayment.php"
        private const val confirmPayment = "$BASE_PATH/confirmwalletpayment.php"
    }

    @GET(GetServerDetails)
    suspend fun getServerDetails(@Query("MobAppID") appID: String): Response<ServerDetailsResponse>

    @GET(GetWalletAuth)
    suspend fun getWalletAuth(@Query("MobAppID") appID: String, @Query("WalletName") walletName: String): Response<WalletAuthResponse>

    @GET(GetWalletToken)
    suspend fun getWalletToken(@Header("Authorization") userPass:String, @Query("CounterCode") counterCode: String): Response<WalletTokenResponse>

    @GET(getBillInquiry)
    suspend fun getBillInquiry(@Header("Authorization") basic: String, @Query("CounterCode") counterCode: String, @Query("token") vToken: String, @Query("sid") memberID: String?, @Query("request_id") requestId: String): Response<BillInquiryResponse>

    @POST(postPayment)
    suspend fun postPayment(@Header("Authorization") basic: String, @Body body:RequestBody): Response<ConfirmPaymentResponse>

    @POST(confirmPayment)
    suspend fun confirmSuccessPayment(@Header("Authorization") basic: String, @Query("CounterCode") counterCode: String, @Query("pay_ref_id") referenceCode: String?, @Query("sid") memberID: String?, @Query("token") token: String?, @Query("amount") amount: Float?): Response<ConfirmPaymentSuccessResponse>

}