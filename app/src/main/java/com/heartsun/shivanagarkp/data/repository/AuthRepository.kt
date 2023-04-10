package com.heartsun.shivanagarkp.data.repository

import android.content.Context
import androidcommon.handler.doTryCatch
import androidcommon.handler.handleResponse
import androidcommon.utils.UiState
import com.heartsun.shivanagarkp.data.apis.AuthApi
import com.heartsun.shivanagarkp.domain.*
import com.heartsun.shivanagarkp.domain.apiResponse.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.Credentials
import okhttp3.MultipartBody

import okhttp3.RequestBody




interface AuthRepository {
    fun getRates(): WaterRateListResponse
    fun getMembers(): MembersListResponse
    fun getNotices(): NoticesListResponse
    fun getAboutOrg(): AboutOrgResponse
    fun getContacts(): ContactsListResponse
    fun getFilesRequirement(): DocumentTypesResponse
    fun sendRegistrationRequest(details: RegistrationRequest?, context: Context): String?
    fun getBillDetails(memberId: Int): BillDetailsResponse
    fun getUserDetails(phoneNo: String, pin: String): UserDetailsResponse
    fun requestPin(phoneNo: String, memberId: String): String?
    fun changePinCode(phoneNo: String?, memberId: String?, newPin: String): String?
    fun addComplaint(message: String, memberID: String?, phoneNo: String?): String
    fun getComplaintList(
        memberID: String?,
        phoneNo: String?
    ): MutableList<ComplaintResponse>?

    fun getActivities(): ActivitiesListResponse
    fun getSliderImages(): SliderListResponse
    suspend fun getServerDetailsFromServer(appID: String): UiState<ServerDetailsResponse>?
    fun getLedgerDetails(memberId: Int): LedgerDetailsResponse
    suspend fun getWalletAuth(appID: String, walletName: String): UiState<WalletAuthResponse>?
    suspend fun getWalletToken(
        vName: String,
        vPassword: String,
        counterCode: String
    ): UiState<WalletTokenResponse>?

    suspend fun getBillInquiry(
        vName: String,
        vPassword: String,
        counterCode: String,
        vToken: String,
        memberID: String?,
        requestId: String
    ): UiState<BillInquiryResponse>?

    suspend fun postPayment(
        vName: String,
        vPassword: String,
        counterCode: String,
        vToken: String,
        memberID: String?,
        requestId: String,
        payAmount: Float,
        walletToken: String
    ): UiState<ConfirmPaymentResponse>?

    suspend fun confirmSuccess(vName: String, vPassword: String, counterCode: String, referenceCode: String?, memberID: String?, amount:Float): UiState<ConfirmPaymentSuccessResponse>?
    fun getWalletList(): WalletListResponse
     fun getAllMembers(): AllMembersListResponse
}

class AuthRepoImpl(
    private val authApi: AuthApi,
    private val connection: ConnectionToServer,
    private val dispatcher: CoroutineDispatcher,
    private val context: Context
) : AuthRepository {
    override fun getRates(): WaterRateListResponse {
        return connection.getRates()
    }

    override fun getMembers(): MembersListResponse {
        return connection.getMembers()
    }

    override fun getNotices(): NoticesListResponse {
        return connection.getNotices(context)
    }

    override fun getAboutOrg(): AboutOrgResponse {
        return connection.getAboutOrg()
    }

    override fun getContacts(): ContactsListResponse {
        return connection.getContactList()
    }

    override fun getFilesRequirement(): DocumentTypesResponse {
        return connection.getRequiredFiles()
    }

    override fun sendRegistrationRequest(details: RegistrationRequest?, context: Context): String {
        return connection.requestForReg(details, context)
    }

    override fun getBillDetails(memberId: Int): BillDetailsResponse {
        return connection.getBillDetails(memberId)
    }

    override fun getUserDetails(phoneNo: String, pin: String): UserDetailsResponse {
        return connection.addTapResponse(phoneNo, pin)
    }

    override fun requestPin(phoneNo: String, memberId: String): String {
        return connection.requestPin(phoneNo, memberId)
    }

    override fun changePinCode(
        phoneNo: String?,
        memberId: String?,
        newPin: String
    ): String? {
        return connection.changePin(phoneNo, memberId, newPin)
    }

    override fun addComplaint(
        message: String,
        memberID: String?,
        phoneNo: String?
    ): String {
        return connection.addComplaint(message, memberID, phoneNo)
    }

    override fun getComplaintList(
        memberID: String?,
        phoneNo: String?
    ): MutableList<ComplaintResponse>? {
        return connection.getComplaintList(memberID, phoneNo)
    }


    override fun getActivities(): ActivitiesListResponse {
        return connection.getActivities(context)
    }

    override fun getSliderImages(): SliderListResponse {
        return connection.getSliderImages(context)
    }

    override suspend fun getServerDetailsFromServer(appID: String): UiState<ServerDetailsResponse> {
        return withContext(dispatcher) {
            doTryCatch {
                authApi.getServerDetails(appID = appID).handleResponse()
            }
        }
    }

    override fun getLedgerDetails(memberId: Int): LedgerDetailsResponse {
        return connection.getLedgerDetails(memberId)
    }

    override suspend fun getWalletAuth(
        appID: String,
        walletName: String
    ): UiState<WalletAuthResponse>? {
        return withContext(dispatcher) {
            doTryCatch {
                authApi.getWalletAuth(appID = appID, walletName).handleResponse()
            }
        }
    }

    override suspend fun getWalletToken(
        vName: String,
        vPassword: String,
        counterCode: String
    ): UiState<WalletTokenResponse>? {
        return withContext(dispatcher) {
            doTryCatch {
                val basic = Credentials.basic(vName, vPassword)
                authApi.getWalletToken(basic, counterCode).handleResponse()
            }
        }
    }

    override suspend fun getBillInquiry(
        vName: String,
        vPassword: String,
        counterCode: String,
        vToken: String,
        memberID: String?,
        requestId: String
    ): UiState<BillInquiryResponse>? {
        return withContext(dispatcher) {
            doTryCatch {
                val basic = Credentials.basic(vName, vPassword)
                authApi.getBillInquiry(basic, counterCode, vToken, memberID, requestId)
                    .handleResponse()
            }
        }
    }

    override suspend fun postPayment(
        vName: String,
        vPassword: String,
        counterCode: String,
        vToken: String,
        memberID: String?,
        requestId: String,
        payAmount: Float,
        walletToken: String
    ): UiState<ConfirmPaymentResponse>? {
        return withContext(dispatcher) {
            doTryCatch {
                val basic = Credentials.basic(vName, vPassword)

                val requestBody: RequestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("counter_code", counterCode)
                    .addFormDataPart("token", vToken)
                    .addFormDataPart("sid", memberID.toString())
                    .addFormDataPart("request_id", requestId)
                    .addFormDataPart("transaction_code", walletToken)
                    .addFormDataPart("amount", payAmount.toString())
                    .build()

                authApi.postPayment(
                    basic,
                    requestBody
                ).handleResponse()
            }
        }
    }

    override suspend fun confirmSuccess(
        vName: String,
        vPassword: String,
        counterCode: String,
        referenceCode: String?,
        memberID: String?,
    amount:Float
    ): UiState<ConfirmPaymentSuccessResponse>? {
        return withContext(dispatcher) {
            doTryCatch {
                val basic = Credentials.basic(vName, vPassword)
                authApi.confirmSuccessPayment(basic, counterCode, referenceCode, memberID,referenceCode,amount)
                    .handleResponse()
            }
        }
    }

    override fun getWalletList(): WalletListResponse {
        return connection.getWalletList()
    }

    override fun getAllMembers(): AllMembersListResponse {
        return connection.getAllMembers()
    }
}