package com.heartsun.shivanagarkp.domain


import com.heartsun.shivanagarkp.domain.dbmodel.*

data class WaterRateListResponse(

    val readingSetupDetails: List<TBLReadingSetupDtl>,
    val readingSetup: List<TBLReadingSetup>,
    val tapType: List<TblTapTypeMaster>,
    val status: String,
    val message: String
)

data class MembersListResponse(
    val tblContact: List<TblContact>?,
    val tblBoardMemberType: List<TblBoardMemberType>?,
    val status: String,
    val message: String
)


data class AllMembersListResponse(
    val tblContact: List<TblCustomers>?,
    val status: String,
    val message: String
)


data class NoticesListResponse(
    val tblNotice: List<TblNotice>,
    val status: String,
    val message: String
)

data class ActivitiesListResponse(
    val tblActivity: List<TblActivity>,
    val status: String,
    val message: String
)

data class SliderListResponse(
    val tblSliderImages: List<TblSliderImages>,
    val status: String,
    val message: String
)

data class WalletListResponse(
    val walletList: List<WalletList>,
    val status: String,
    val message: String
) {
    class WalletList (
        val TokID: String,
        val Vendor: String,
        val PublicToken: String
    )
}


data class AboutOrgResponse(
    val tblAbout: TblAboutOrg?,
    val status: String,
    val message: String
)

data class ContactsListResponse(
    val tblDepartmentContact: List<TblDepartmentContact>,
    val status: String,
    val message: String
)

data class UserDetailsResponse(
    val tblMember: List<TblMember>?,
    val status: String,
    val message: String
)