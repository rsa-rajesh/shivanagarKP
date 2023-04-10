package com.heartsun.shivanagarkp.domain.dbmodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tBLReadingSetupDtl", primaryKeys = ["VNO", "SrNo"])
data class TBLReadingSetupDtl(
    @ColumnInfo(name = "VNO")
    val VNO: Int,
    @ColumnInfo(name = "SrNo")
    val SrNo: Int,
    @ColumnInfo(name = "MnNo")
    val MnNo: Int?,
    @ColumnInfo(name = "MxNo")
    val MxNo: Int?,
    @ColumnInfo(name = "Amt")
    val Amt: Float?,
    @ColumnInfo(name = "Rate")
    val Rate: Float?,
)

@Entity(tableName = "tBLReadingSetup")
data class TBLReadingSetup(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "VNO")
    val VNO: Int,
    @ColumnInfo(name = "FixCharges")
    val FixCharges: Int?,
    @ColumnInfo(name = "TapTypeID")
    val TapTypeID: Int?,
    @ColumnInfo(name = "TapSizeID")
    val TapSizeID: Int?,
    @ColumnInfo(name = "Remarks")
    val Remarks: String?,
)

@Entity(tableName = "tblTapTypeMaster")
data class TblTapTypeMaster(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "TapTypeID")
    val TapTypeID: Int,
    @ColumnInfo(name = "TapTypeName")
    val TapTypeName: String,
)


@Entity(tableName = "tblContact")
data class TblContact(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "ContID")
    val ContID: Int,
    @ColumnInfo(name = "ContactName")
    val ContactName: String?,
    @ColumnInfo(name = "ContactNumber")
    val ContactNumber: String?,
    @ColumnInfo(name = "IsActive")
    val IsActive: Int,
    @ColumnInfo(name = "Post")
    val Post: String?,
    @ColumnInfo(name = "MemberType")
    val MemberType: Int,
    @ColumnInfo(name = "Tenure")
    val Tenure: String?,
    @ColumnInfo(name = "Address")
    val Address: String?,
    @ColumnInfo(name = "Image")
    val Image: String?,
)


@Entity(tableName = "tblBoardMemberType")
data class TblBoardMemberType(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "MemTypeID")
    val MemTypeID: Int,
    @ColumnInfo(name = "MemberType")
    val MemberType: String?,
    @ColumnInfo(name = "isOldMember")
    val isOldMember: Int,
)

@Entity(tableName = "tblNotice")
data class TblNotice(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "NoticeID")
    val NoticeID: Int,
    @ColumnInfo(name = "NoticeHeadline")
    val NoticeHeadline: String?,
    @ColumnInfo(name = "NoticeDesc")
    val NoticeDesc: String?,
    @ColumnInfo(name = "DateNep")
    val DateNep: String?,
    @ColumnInfo(name = "DateTimeEng")
    val DateTimeEng: String?,
    @ColumnInfo(name = "NoticeFile")
    val NoticeFile: String?,
)

@Entity(tableName = "tblActivity")
data class TblActivity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "ActivityID")
    val ActivityID: Int,
    @ColumnInfo(name = "ActivityHeadline")
    val ActivityHeadline: String?,
    @ColumnInfo(name = "ActivityDesc")
    val ActivityDesc: String?,
    @ColumnInfo(name = "DateNep")
    val DateNep: String?,
    @ColumnInfo(name = "DateTimeEng")
    val DateTimeEng: String?,
    @ColumnInfo(name = "ActivityFile")
    val ActivityFile: String?,
)


@Entity(tableName = "tblSliderImages")
data class TblSliderImages(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "SliderID")
    val SliderID: Int,
    @ColumnInfo(name = "SliderTitle")
    val SliderTitle: String?,
    @ColumnInfo(name = "SliderImageUrl")
    val SliderImageUrl: String?,
    @ColumnInfo(name = "Url")
    val Url: String?,
    @ColumnInfo(name = "SliderImageFile")
    val SliderImageFile: String?
)


@Entity(tableName = "tblAboutOrg")
data class TblAboutOrg(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "Cont_id")
    val Cont_id: Int,
    @ColumnInfo(name = "Cont_image")
    val Cont_image: String?,
    @ColumnInfo(name = "Cont_details")
    val Cont_details: String?,
)

@Entity(tableName = "tblDepartmentContact")
data class TblDepartmentContact(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "Dept_id")
    val Dept_id: Int,
    @ColumnInfo(name = "Dept_name")
    val Dept_name: String?,
    @ColumnInfo(name = "Dept_contact")
    val Dept_contact: String?,
    @ColumnInfo(name = "Dept_mail")
    val Dept_mail: String?,
)

@Entity(tableName = "tblDocumentType")
data class TblDocumentType(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "DTID")
    val DTID: Int,
    @ColumnInfo(name = "DocTypeName")
    val DocTypeName: String?,

    )


@Entity(tableName = "tblMember")
data class TblMember(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "MemberID")
    val MemberID: Int,
    @ColumnInfo(name = "ContactNo")
    val ContactNo: String?,
    @ColumnInfo(name = "MemName")
    val MemName: String?,
    @ColumnInfo(name = "PinCode")
    val PinCode: String?,
    @ColumnInfo(name = "Address")
    val Address: String?,
    @ColumnInfo(name = "RegDateTime")
    val RegDateTime: String?
//    @ColumnInfo(name = "IsCMAndMAAllowed")
//    val IsCMAndMAAllowed: Boolean?,
)

@Entity(tableName = "tblCustomers")
data class TblCustomers(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "MemberID")
    val MemberID: Int,
    @ColumnInfo(name = "MemName")
    val MemName: String?,
    @ColumnInfo(name = "Address")
    val Address: String?,
    @ColumnInfo(name = "MemNameNepali")
    val MemNameNepali: String,
    @ColumnInfo(name = "MemAddNepali")
    val MemAddNepali: String?,
    @ColumnInfo(name = "ContactNo")
    val ContactNo: String?,
)