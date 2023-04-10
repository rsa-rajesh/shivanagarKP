package com.heartsun.shivanagarkp.ui.memberRegisterRequest

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidcommon.extension.loggerE
import androidx.lifecycle.*
import com.heartsun.shivanagarkp.data.repository.AuthRepository
import com.heartsun.shivanagarkp.data.repository.databaseReppo.DbRepository
import com.heartsun.shivanagarkp.domain.*
import com.heartsun.shivanagarkp.domain.dbmodel.TblDocumentType
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val dbRepository: DbRepository,
    private val homeRepository: AuthRepository,

) : ViewModel() {
    private val _filesRequirements = MutableLiveData<DocumentTypesResponse>()
    val fileTypesFromServer: LiveData<DocumentTypesResponse> = _filesRequirements
    fun getFileRequirementFromServer() {

        val a: DocumentTypesResponse = homeRepository.getFilesRequirement()
        Handler(Looper.getMainLooper()).post {
            _filesRequirements.value = a
        }
    }

    private val _sendRegistrationRequest = MutableLiveData<String>()
    val registrationResponse: LiveData<String> = _sendRegistrationRequest
    fun sendRegistrationRequestToServer(details: RegistrationRequest?,context:Context) {
            loggerE("calling data", "specTest")
            val a: String? = homeRepository.sendRegistrationRequest(details,context)
            if (a!= null) {
                Handler(Looper.getMainLooper()).post {
                    _sendRegistrationRequest.value = a.toString()
                }
            }
    }


    val fileTypeFromLocalDb: LiveData<List<TblDocumentType>> =
        dbRepository.files.asLiveData()

    fun insert(fileTypes: TblDocumentType) = viewModelScope.launch {
        dbRepository.insert(contacts = fileTypes)
    }


    private val _billDetails = MutableLiveData<BillDetailsResponse>()
    val billDetailsFromServer: LiveData<BillDetailsResponse> = _billDetails
    fun getBillingDetails(memberId:Int) {
        val a: BillDetailsResponse = homeRepository.getBillDetails(memberId)
        Handler(Looper.getMainLooper()).post {
            _billDetails.value = a
        }
    }

    private val _ledgerDetails = MutableLiveData<LedgerDetailsResponse>()
    val ledgerDetailsFromServer: LiveData<LedgerDetailsResponse> = _ledgerDetails
    fun getLedgerDetails(memberId:Int) {
        val a: LedgerDetailsResponse = homeRepository.getLedgerDetails(memberId)
        Handler(Looper.getMainLooper()).post {
            _ledgerDetails.value = a
        }
    }
}