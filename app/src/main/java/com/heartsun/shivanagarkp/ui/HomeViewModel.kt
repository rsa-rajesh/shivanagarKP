package com.heartsun.shivanagarkp.ui

import android.os.Handler
import android.os.Looper
import androidcommon.utils.UiState
import androidx.lifecycle.*
import com.heartsun.shivanagarkp.data.repository.AuthRepository
import com.heartsun.shivanagarkp.data.repository.databaseReppo.DbRepository
import com.heartsun.shivanagarkp.domain.*
import com.heartsun.shivanagarkp.domain.apiResponse.*
import com.heartsun.shivanagarkp.domain.dbmodel.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val dbRepository: DbRepository,
    private val homeRepository: AuthRepository
) : ViewModel() {

    private val _rates = MutableLiveData<WaterRateListResponse>()
    val ratesFromServer: LiveData<WaterRateListResponse> = _rates
    fun getRatesFromServer() {
        val a: WaterRateListResponse = homeRepository.getRates()
        Handler(Looper.getMainLooper()).post {
            _rates.value = a
        }
    }

    fun insert(shipment: TblTapTypeMaster) = viewModelScope.launch {
        dbRepository.insert(tblTapTypeMaster = shipment)
    }

    fun insert(shipment: TBLReadingSetupDtl) = viewModelScope.launch {
        dbRepository.insert(tBLReadingSetupDtl = shipment)
    }

    fun insert(shipment: TBLReadingSetup) = viewModelScope.launch {
        dbRepository.insert(tblReadingSetup = shipment)
    }

    val ratesFromLocalDb: LiveData<List<TBLReadingSetupDtl>> =
        dbRepository.tBLReadingSetupDtls.asLiveData()

    val tapTypesObserver: LiveData<List<TblTapTypeMaster>> =
        dbRepository.tblTapTypeMaster.asLiveData()

    //for members activity
    private val _members = MutableLiveData<MembersListResponse>()
    val membersFromServer: LiveData<MembersListResponse> = _members
    fun getMembersFromServer() {
        val a: MembersListResponse = homeRepository.getMembers()
        Handler(Looper.getMainLooper()).post {
            _members.value = a
        }
    }

    var membersFromLocal: LiveData<List<TblContact>>? = null
    fun getMembers(memberTypeId: Int) = viewModelScope.launch {
        membersFromLocal = dbRepository.getContactList(memberTypeId).asLiveData()
    }

    //    var oldMembersFromLocal: LiveData<List<TblContact>>? = null
    fun getOldMembers(memberTypeId: Int) = viewModelScope.launch {
        membersFromLocal = dbRepository.getOldContactList(memberTypeId).asLiveData()
    }

    val membersTypeFromLocalDb: LiveData<List<TblBoardMemberType>> =
        dbRepository.tblBoardMemberTypes.asLiveData()

    fun insert(memberType: TblBoardMemberType) = viewModelScope.launch {
        dbRepository.insert(tblBoardMemberTypes = memberType)
    }

    fun insert(members: TblContact) = viewModelScope.launch {
        dbRepository.insert(tblContacts = members)
    }

    private val _notices = MutableLiveData<NoticesListResponse>()
    val noticesFromServer: LiveData<NoticesListResponse> = _notices
    fun getNoticesFromServer() {
        val a: NoticesListResponse = homeRepository.getNotices()
        Handler(Looper.getMainLooper()).post {
            _notices.value = a
        }
    }

    fun insert(notice: TblNotice) = viewModelScope.launch {
        dbRepository.insert(notice)
    }

    fun insert(sliderImages: TblSliderImages) = viewModelScope.launch {
        dbRepository.insert(sliderImages)
    }

    fun insert(activity: TblActivity) = viewModelScope.launch {
        dbRepository.insert(activity)
    }

    val noticesFromLocalDb: LiveData<List<TblNotice>> =
        dbRepository.tblNotices.asLiveData()

    val activityFromLocalDb: LiveData<List<TblActivity>> =
        dbRepository.tblActivities.asLiveData()


    val sliderImagesFromLocalDb: LiveData<List<TblSliderImages>> =
        dbRepository.tblSliderImagess.asLiveData()

    private val _aboutOrg = MutableLiveData<AboutOrgResponse>()
    val aboutOrgFromServer: LiveData<AboutOrgResponse> = _aboutOrg
    fun getAboutOrgFromServer() {
        val a: AboutOrgResponse = homeRepository.getAboutOrg()
        Handler(Looper.getMainLooper()).post {
            _aboutOrg.value = a
        }
    }

    fun insert(about: TblAboutOrg) = viewModelScope.launch {
        dbRepository.insert(about)
    }

    val aboutOrgFromLocalDb: LiveData<List<TblAboutOrg>> =
        dbRepository.about.asLiveData()


    val contactsListFromLocalDb: LiveData<List<TblDepartmentContact>> =
        dbRepository.contact.asLiveData()

    fun insert(contacts: TblDepartmentContact) = viewModelScope.launch {
        dbRepository.insert(contacts = contacts)
    }

    private val _contacts = MutableLiveData<ContactsListResponse>()
    val contactsFromServer: LiveData<ContactsListResponse> = _contacts
    fun getContactsFromServer() {
        val a: ContactsListResponse = homeRepository.getContacts()
        Handler(Looper.getMainLooper()).post {
            _contacts.value = a
        }
    }

    private val _activities = MutableLiveData<ActivitiesListResponse>()
    val activitiesFromServer: LiveData<ActivitiesListResponse> = _activities
    fun getActivitiesFromServer() {
        val a: ActivitiesListResponse = homeRepository.getActivities()
        Handler(Looper.getMainLooper()).post {
            _activities.value = a
        }
    }

    private val _sliders = MutableLiveData<SliderListResponse>()
    val slidersFromServer: LiveData<SliderListResponse> = _sliders
    fun getSlidersFromServer() {
        val a: SliderListResponse = homeRepository.getSliderImages()
        Handler(Looper.getMainLooper()).post {
            _sliders.value = a
        }
    }

    fun deleteAllSlider(slider: TblSliderImages) = viewModelScope.launch {
        dbRepository.deleteAll(tblSliderImages1 = slider)
    }

    private val _serverDetails = MutableLiveData<UiState<ServerDetailsResponse>>()
    val serverDetails: LiveData<UiState<ServerDetailsResponse>> = _serverDetails
    fun getServerDetailsFromAPI(appID: String) {
        viewModelScope.launch {
            _serverDetails.value = UiState.Loading()
            _serverDetails.value = homeRepository.getServerDetailsFromServer(appID)
        }
    }

    private val _walletAuths = MutableLiveData<UiState<WalletAuthResponse>>()
    val walletAuths: LiveData<UiState<WalletAuthResponse>> = _walletAuths
    fun getWalletAuth(walletName: String, appID: String) {
        viewModelScope.launch {
        _walletAuths.value = UiState.Loading()
        _walletAuths.value = homeRepository.getWalletAuth(appID,walletName)
        }
    }

    private val _walletToken = MutableLiveData<UiState<WalletTokenResponse>>()
    val walletToken: LiveData<UiState<WalletTokenResponse>> = _walletToken
    fun getWalletToken(vName: String, vPassword: String, counterCode: String) {
        viewModelScope.launch {
            _walletToken.value = UiState.Loading()
            _walletToken.value = homeRepository.getWalletToken(vName,vPassword,counterCode)
        }
    }

    private val _billInquiry = MutableLiveData<UiState<BillInquiryResponse>>()
    val billInquiry: LiveData<UiState<BillInquiryResponse>> = _billInquiry
    fun getBillInquiry(vName: String, vPassword: String, counterCode: String, vToken: String, memberID: String?, requestId: String) {
        viewModelScope.launch {
            _billInquiry.value = UiState.Loading()
            _billInquiry.value = homeRepository.getBillInquiry(vName,vPassword,counterCode,vToken,memberID,requestId)
        }
    }

    private val _confirmPayment = MutableLiveData<UiState<ConfirmPaymentResponse>>()
    val confirmPayment: LiveData<UiState<ConfirmPaymentResponse>> = _confirmPayment
    fun confirmPayment(vName: String, vPassword: String, vToken: String, payAmount: Float, memberID: String?, walletToken: String, requestId: String, counterCode: String) {
        viewModelScope.launch {
            _confirmPayment.value = UiState.Loading()
            _confirmPayment.value = homeRepository.postPayment(vName,vPassword,counterCode,vToken,memberID,requestId,payAmount,walletToken)
        }
    }

    private val _confirmSuccess = MutableLiveData<UiState<ConfirmPaymentSuccessResponse>>()
    val confirmSuccess: LiveData<UiState<ConfirmPaymentSuccessResponse>> = _confirmSuccess
    fun paymentSuccess(vName: String, vPassword: String, referenceCode: String?, counterCode: String, memberID:String?,amount:Float) {
        viewModelScope.launch {
            _confirmSuccess.value = UiState.Loading()
            _confirmSuccess.value = homeRepository.confirmSuccess(vName,vPassword,counterCode,referenceCode,memberID,amount)
        }
    }

    private val _wallets = MutableLiveData<WalletListResponse>()
    val walletListFromServer: LiveData<WalletListResponse> = _wallets
    fun getActiveWallet() {
        val a: WalletListResponse = homeRepository.getWalletList()
        Handler(Looper.getMainLooper()).post {
            _wallets.value = a
        }
    }

    fun deleteAllNotices(slider: TblNotice) = viewModelScope.launch {
        dbRepository.deleteAll(tblNotices = slider)
    }

    fun deleteAllMemberType() = viewModelScope.launch {
        dbRepository.deleteAllMemberTypes()
    }

    fun deleteAllMembers() =  viewModelScope.launch  {
        dbRepository.deleteAllMembers()
    }

    private val _allMembers = MutableLiveData<AllMembersListResponse>()
    val allMembersFromServer: LiveData<AllMembersListResponse> = _allMembers
    fun getAllMembersFromServer() {
        val a: AllMembersListResponse = homeRepository.getAllMembers()
        Handler(Looper.getMainLooper()).post {
            _allMembers.value = a
        }
    }

    var allMembersFromLocal: LiveData<List<TblCustomers>>? = null
    fun getAllMembers()=viewModelScope.launch  {
        allMembersFromLocal = dbRepository.getMembersList.asLiveData()
    }

    fun getFilteredMembers(search_query:String)=viewModelScope.launch  {
        allMembersFromLocal = dbRepository.getFilteredMembersList(search_query).asLiveData()
    }

    fun insert(memberType: TblCustomers)= viewModelScope.launch {
        dbRepository.insert(customers = memberType)
    }
}