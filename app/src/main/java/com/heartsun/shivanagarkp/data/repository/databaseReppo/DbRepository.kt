package com.heartsun.shivanagarkp.data.repository.databaseReppo

import com.heartsun.shivanagarkp.data.dao.*
import com.heartsun.shivanagarkp.data.database.KanipaniDatabase
import com.heartsun.shivanagarkp.domain.dbmodel.*
import kotlinx.coroutines.flow.Flow

class DbRepository(database: KanipaniDatabase) {


    //for tap types and rates -- starts

    private var tblReadingSetupDtlDao: TBLReadingSetupDtlDao = database.tBLReadingSetupDtlDao()
    private var tblReadingSetupDao: TBLReadingSetupDao = database.tBLReadingSetupDao()
    private var tblTapTypeMasterDao: TblTapTypeMasterDao = database.tblTapTypeMasterDao()

    val tBLReadingSetupDtls: Flow<List<TBLReadingSetupDtl>> =
        tblReadingSetupDtlDao.getAllData()

    suspend fun insert(tBLReadingSetupDtl: TBLReadingSetupDtl) {
        tblReadingSetupDtlDao.insert(table = tBLReadingSetupDtl)
    }

    suspend fun deleteAll(tBLReadingSetupDtl: TBLReadingSetupDtl) {
        tblReadingSetupDtlDao.deleteAll()
    }


    val tBLReadingSetup: Flow<List<TBLReadingSetup>> =
        tblReadingSetupDao.getAllData()

    suspend fun insert(tblReadingSetup: TBLReadingSetup) {
        tblReadingSetupDao.insert(table = tblReadingSetup)
    }

    suspend fun deleteAll(tblReadingSetupDao: TBLReadingSetup) {
        tblReadingSetupDtlDao.deleteAll()
    }


    val tblTapTypeMaster: Flow<List<TblTapTypeMaster>> =
        tblTapTypeMasterDao.getAllData()

    suspend fun insert(tblTapTypeMaster: TblTapTypeMaster) {
        tblTapTypeMasterDao.insert(table = tblTapTypeMaster)
    }

    suspend fun deleteAll(tblTapTypeMaster: TblTapTypeMaster) {
        tblTapTypeMasterDao.deleteAll()
    }



    private var tblBoardMemberType: TblBoardMemberTypeDao = database.tblBoardMemberTypeDao()
    private var tblContact: TblContactDao = database.tblContactDao()


    val tblContacts: Flow<List<TblContact>> =
        tblContact.getAllData()


    suspend fun getContactList(memberTypeId: Int): Flow<List<TblContact>> {
        return tblContact.getFilteredContacts(memberTypeId)
    }

    suspend fun getOldContactList(memberTypeId: Int): Flow<List<TblContact>> {
        return tblContact.getFilteredOldContacts(memberTypeId)
    }

    suspend fun insert(tblContacts: TblContact) {
        tblContact.insert(table = tblContacts)
    }

    suspend fun deleteAllMembers() {
        tblContact.deleteAll()
    }


    val tblBoardMemberTypes: Flow<List<TblBoardMemberType>> =
        tblBoardMemberType.getAllData()

    suspend fun insert(tblBoardMemberTypes: TblBoardMemberType) {
        tblBoardMemberType.insert(table = tblBoardMemberTypes)
    }

    suspend fun deleteAllMemberTypes() {
        tblBoardMemberType.deleteAll()
    }

    //for member types and members -- ends
    suspend fun deleteAll() {
        tblReadingSetupDtlDao.deleteAll()
        tblReadingSetupDao.deleteAll()
        tblTapTypeMasterDao.deleteAll()

    }

    //for notices list -- starts

    private var tblNotice: TblNoticeDao = database.tblNoticeDao()

    suspend fun insert(notice: TblNotice) {
        tblNotice.insert(table = notice)

    }

    suspend fun deleteAll(tblNotices: TblNotice) {
        tblNotice.deleteAll()
    }

    val tblNotices: Flow<List<TblNotice>> =
        tblNotice.getAllData()


    //for about organization  -- starts

    private var tblAboutOrg: TblAboutOrgDao = database.tblAboutOrgDao()

    suspend fun insert(about: TblAboutOrg) {
        tblAboutOrg.insert(table = about)

    }

    suspend fun deleteAll(about: TblAboutOrg) {
        tblAboutOrg.deleteAll()
    }

    val about: Flow<List<TblAboutOrg>> =
        tblAboutOrg.getAllData()


    //for contact  -- starts

    private var tblDepartmentContact: TblDepartmentContactDao = database.tblDepartmentContactDao()

    suspend fun insert(contacts: TblDepartmentContact) {
        tblDepartmentContact.insert(table = contacts)

    }

    suspend fun deleteAll(contact: TblDepartmentContact) {
        tblDepartmentContact.deleteAll()
    }

    val contact: Flow<List<TblDepartmentContact>> =
        tblDepartmentContact.getAllData()


    //for document type  -- starts

    private var tblDocumentType: TblDocumentTypeDao = database.tblDocumentTypeDao()

    suspend fun insert(contacts: TblDocumentType) {
        tblDocumentType.insert(table = contacts)
    }

    suspend fun deleteAll(contact: TblDocumentType) {
        tblDocumentType.deleteAll()
    }

    val files: Flow<List<TblDocumentType>> =
        tblDocumentType.getAllData()


//    add Tap --start

    private var tblMemberDao: TblMemberDao = database.tblMemberDao()


    suspend fun insert(members: TblMember) {
        tblMemberDao.insert(table = members)
    }

    suspend fun deleteAll(members: TblMember) {
        tblMemberDao.deleteAll()
    }

    suspend fun delete(members: Int) {
        tblMemberDao.delete(members)
    }

    suspend fun updatePin(memberID: Int, changedPin: Int) {
        tblMemberDao.updatePin(memberID,changedPin)
    }

    val getAllTaps: Flow<List<TblMember>> =
        tblMemberDao.getAllData()




    //for activity list -- starts

    private var tblActivity: TblActivityDao = database.tblActivityDao()

    suspend fun insert(activity: TblActivity) {
        tblActivity.insert(table = activity)

    }

    suspend fun deleteAll(tblActivity1: TblActivity) {
        tblActivity.deleteAll()
    }

    val tblActivities: Flow<List<TblActivity>> =
        tblActivity.getAllData()




    //for activity list -- starts

    private var tblSliderImages: TblSliderImagesDao = database.tblSliderImagesDao()

    suspend fun insert(activity: TblSliderImages) {
        tblSliderImages.insert(table = activity)

    }

    suspend fun deleteAll(tblSliderImages1: TblSliderImages) {
        tblSliderImages.deleteAll()
    }

    private var tblCustomersDao: TblCustomersDao = database.tblCustomersDao()

    val getMembersList: Flow<List<TblCustomers>> = tblCustomersDao.getAllData()


     fun getFilteredMembersList(search_query: String?): Flow<List<TblCustomers>> {
        return tblCustomersDao.getFilteredData(search_query)
    }

    val tblSliderImagess: Flow<List<TblSliderImages>> =
        tblSliderImages.getAllData()

    suspend fun insert(customers: TblCustomers) {
        tblCustomersDao.insert(table = customers)
    }

}