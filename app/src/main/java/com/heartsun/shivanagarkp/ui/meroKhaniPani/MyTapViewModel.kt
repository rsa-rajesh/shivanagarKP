package com.heartsun.shivanagarkp.ui.meroKhaniPani

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.*
import com.heartsun.shivanagarkp.data.repository.AuthRepository
import com.heartsun.shivanagarkp.data.repository.databaseReppo.DbRepository
import com.heartsun.shivanagarkp.domain.*
import com.heartsun.shivanagarkp.domain.dbmodel.*
import kotlinx.coroutines.launch

class MyTapViewModel(
    private val dbRepository: DbRepository,
    private val homeRepository: AuthRepository
) : ViewModel() {

    private val _addTap = MutableLiveData<UserDetailsResponse>()
    val addTap: LiveData<UserDetailsResponse> = _addTap
    fun addTap(phoneNo: String, pin: String) {
        viewModelScope.launch {
            _addTap.value = homeRepository.getUserDetails(phoneNo, pin)
        }
    }

    private val _pinRequest = MutableLiveData<String>()
    val pinRequest: LiveData<String> = _pinRequest
    fun requestPin(phoneNo: String, memberId: String) {
        viewModelScope.launch {
            _pinRequest.value = homeRepository.requestPin(phoneNo, memberId)
        }
    }

    fun insert(members: TblMember) = viewModelScope.launch {
        dbRepository.insert(members)
    }

    fun delete(members: Int) = viewModelScope.launch {

        dbRepository.delete(members)
    }

    private val _changePin = MutableLiveData<String>()
    val changePin: LiveData<String> = _changePin
    fun changePin(newPin: String, memberId: String?, phoneNo: String?) {
        viewModelScope.launch {
            _changePin.value = homeRepository.changePinCode(phoneNo, memberId,newPin)
        }
    }

    fun update(memberID: Int, changedPin: Int)= viewModelScope.launch {
        dbRepository.updatePin(memberID,changedPin)
    }


    private val _addComplaint = MutableLiveData<String>()
    val addComplaint: LiveData<String> = _addComplaint
    fun postComplaint(message: String, memberID: String?, phoneNo: String?) {
        viewModelScope.launch {
            _addComplaint.value = homeRepository.addComplaint(message,memberID,phoneNo)
        }
    }


    private val _complaintList = MutableLiveData<MutableList<ComplaintResponse>>()
    val complaintList: LiveData<MutableList<ComplaintResponse>> = _complaintList
    fun getComplaintListServer(memberID: String?, phoneNo: String?) {
        val a: MutableList<ComplaintResponse>? = homeRepository.getComplaintList(memberID,phoneNo)
        if(a!=null){
            Handler(Looper.getMainLooper()).post {
                _complaintList.value = a!!
            }
        }
    }


    val tapsListFromLocalDb: LiveData<List<TblMember>> = dbRepository.getAllTaps.asLiveData()


//    val noticesFromLocalDb: LiveData<List<TblNotice>> =
//        dbRepository.tblNotices.asLiveData()
}