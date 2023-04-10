package com.heartsun.shivanagarkp.data.repository

import android.annotation.SuppressLint
import com.heartsun.shivanagarkp.data.Prefs
import com.heartsun.shivanagarkp.domain.dbmodel.*
import com.heartsun.shivanagarkp.utils.connectionUtils.SqlServerFunctions
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement
import android.content.Context

import android.graphics.BitmapFactory

import android.graphics.Bitmap
import android.net.Uri
import androidcommon.extension.loggerE
import androidcommon.utils.FilePath
import com.heartsun.shivanagarkp.domain.*
import timber.log.Timber
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class ConnectionToServer(var prefs: Prefs) {

    fun getRates(): WaterRateListResponse {
        val stmt: Statement?
        val resultSet: ResultSet?
        val resultSet2: ResultSet?
        val resultSet3: ResultSet?

        val query = "select * from TBLReadingSetupDtl"
        val query2 = "select * from TBLReadingSetup"
        val query3 = "select * from tblTapTypeMaster"

        val tapTypeList: MutableList<TblTapTypeMaster> = arrayListOf()
        val readingSetupList: MutableList<TBLReadingSetup> = arrayListOf()
        val readingSetupDetailsList: MutableList<TBLReadingSetupDtl> = arrayListOf()

        try {
            val ss = SqlServerFunctions()
            val conn: Connection = ss.ConnectToSQLServer(prefs)
            stmt = conn.createStatement()

            resultSet = stmt.executeQuery(query)
            while (resultSet.next()) {
                val readingSetupDetails = TBLReadingSetupDtl(
                    VNO = resultSet.getInt("VNO"),
                    SrNo = resultSet.getInt("SrNo"),
                    MnNo = resultSet.getInt("MnNo"),
                    MxNo = resultSet.getInt("MxNo"),
                    Amt = resultSet.getFloat("Amt"),
                    Rate = resultSet.getFloat("Rate")
                )
                readingSetupDetailsList.add(readingSetupDetails)
            }
            resultSet2 = stmt.executeQuery(query2)
            while (resultSet2.next()) {
                val readingSetup = TBLReadingSetup(
                    VNO = resultSet2.getInt("VNO"),
                    FixCharges = resultSet2.getInt("FixCharges"),
                    TapTypeID = resultSet2.getInt("TapTypeID"),
                    TapSizeID = resultSet2.getInt("TapSizeID"),
                    Remarks = resultSet2.getString("Remarks")
                )
                readingSetupList.add(readingSetup)
            }

            resultSet3 = stmt.executeQuery(query3)
            while (resultSet3.next()) {
                val tapType = TblTapTypeMaster(
                    TapTypeID = resultSet3.getInt("TapTypeID"),
                    TapTypeName = resultSet3.getString("TapTypeName")
                )
                tapTypeList.add(tapType)
            }

            conn.close()

            return WaterRateListResponse(
                readingSetup = readingSetupList,
                readingSetupDetails = readingSetupDetailsList,
                tapType = tapTypeList,
                status = "success",
                message = "success"
            )

        } catch (e: Exception) {
            return WaterRateListResponse(
                readingSetup = readingSetupList,
                readingSetupDetails = readingSetupDetailsList,
                tapType = tapTypeList,
                status = "error",
                message = "माफ गर्नुहोस् सर्भरमा जडान गर्न सकिएन"
            )

        }


    }

    fun getMembers(): MembersListResponse {
        val stmt: Statement?
        val resultSet: ResultSet?
        val resultSet2: ResultSet?
        val query = "select * from tblBoardMemberType"
        val query2 = "select * from tblContact WHERE membertype IS NOT NULL"

        val memberTypeList: MutableList<TblBoardMemberType> = arrayListOf()
        val membersList: MutableList<TblContact> = arrayListOf()

        try {
            val ss = SqlServerFunctions()
            val conn: Connection = ss.ConnectToSQLServer(prefs)
            stmt = conn.createStatement()
            resultSet = stmt.executeQuery(query2)
            while (resultSet.next()) {

                    val tblContact = TblContact(
                        ContID = resultSet.getInt("ContID"),
                        ContactName = resultSet.getString("ContactName"),
                        ContactNumber = resultSet.getString("ContactNumber"),
                        IsActive = resultSet.getInt("IsActive"),
                        Post = resultSet.getString("Post"),
                        MemberType = resultSet.getInt("MemberType"),
                        Tenure = resultSet.getString("Tenure"),
                        Address = resultSet.getString("Address"),
                        Image = resultSet.getString("ImgUrl")
                    )
                    membersList.add(tblContact)
            }

            resultSet2 = stmt.executeQuery(query)
            while (resultSet2.next()) {
                val tblBoardMemberType = TblBoardMemberType(
                    MemTypeID = resultSet2.getInt("MemTypeID"),
                    MemberType = resultSet2.getString("MemberType"),
                    isOldMember = resultSet2.getInt("isOldMember"),
                )
                memberTypeList.add(tblBoardMemberType)
            }
            conn.close()

            return MembersListResponse(
                tblContact = membersList,
                tblBoardMemberType = memberTypeList,
                status = "success",
                message = "success"
            )
        } catch (e: Exception) {
            return MembersListResponse(
                tblContact = null,
                tblBoardMemberType = null,
                status = "error",
                message = "माफ गर्नुहोस् सर्भरमा जडान गर्न सकिएन"
            )
        }

    }

    fun getNotices(context: Context): NoticesListResponse {

        val stmt: Statement?
        val resultSet: ResultSet?

        val query = "select * from tblNotice where IsActive=1"

        val noticesList: MutableList<TblNotice> = arrayListOf()


        try {
            val ss = SqlServerFunctions()
            val conn: Connection = ss.ConnectToSQLServer(prefs)
            stmt = conn.createStatement()
            resultSet = stmt.executeQuery(query)
            while (resultSet.next()) {
                if (resultSet.getBytes("NoticeFile") != null) {
                    val data: ByteArray = resultSet.getBytes("NoticeFile")
                    val imageStream = ByteArrayInputStream(data)
                    val theImage = BitmapFactory.decodeStream(imageStream)
                    val image = File(
                        context.getExternalFilesDir(null),
                        "noticeImage" + resultSet.getInt("NoticeID").toString() + ".png"
                    )
                    val fos = FileOutputStream(image)
                    fos.use { theImage.compress(Bitmap.CompressFormat.PNG, 100, it) }

                    val notices = TblNotice(
                        NoticeID = resultSet.getInt("NoticeID"),
                        NoticeHeadline = resultSet.getString("NoticeHeadline"),
                        NoticeDesc = resultSet.getString("NoticeDesc"),
                        DateNep = resultSet.getString("DateNep"),
                        DateTimeEng = resultSet.getString("DateTimeEng"),
                        NoticeFile = Uri.parse(image.path).toString()
                    )
                    noticesList.add(notices)
                } else {

                    val notices = TblNotice(
                        NoticeID = resultSet.getInt("NoticeID"),
                        NoticeHeadline = resultSet.getString("NoticeHeadline"),
                        NoticeDesc = resultSet.getString("NoticeDesc"),
                        DateNep = resultSet.getString("DateNep"),
                        DateTimeEng = resultSet.getString("DateTimeEng"),
                        NoticeFile = null
                    )
                    noticesList.add(notices)

                }
            }
            conn.close()

            return NoticesListResponse(
                tblNotice = noticesList,
                status = "success",
                message = "success"
            )
        } catch (e: Exception) {
            return NoticesListResponse(
                tblNotice = noticesList,
                status = "error",
                message = "माफ गर्नुहोस् सर्भरमा जडान गर्न सकिएन"
            )
        }

    }

    fun getAboutOrg(): AboutOrgResponse {
        val stmt: Statement?
        val resultSet: ResultSet?
        val query = "select * from TblAboutOrg where Cont_id=1"
        var aboutOrg: TblAboutOrg? = null
        try {
            val ss = SqlServerFunctions()
            val conn: Connection = ss.ConnectToSQLServer(prefs)
            stmt = conn.createStatement()

            resultSet = stmt.executeQuery(query)
            while (resultSet.next()) {
//                if (resultSet.getBytes("Cont_image") != null) {
//                    val data: ByteArray = resultSet.getBytes("Cont_image")
//                    val imageStream = ByteArrayInputStream(data)
//                    val theImage = BitmapFactory.decodeStream(imageStream)
//                    val image = File(
//                        context.getExternalFilesDir(null),
//                        "aboutOrgImage.png"
//                    )
//                    val fos = FileOutputStream(image)
//                    fos.use { theImage.compress(Bitmap.CompressFormat.PNG, 100, it) }
//
//                    val about: TblAboutOrg = TblAboutOrg(
//                        Cont_id = resultSet.getInt("Cont_id"),
//                        Cont_details = resultSet.getString("Cont_details"),
//                        Cont_image = Uri.parse(image.path).toString()
//                    )
//                    aboutOrg = about
//                } else {
                    val about = TblAboutOrg(
                        Cont_id = resultSet.getInt("Cont_id"),
                        Cont_details = resultSet.getString("Cont_details"),
                        Cont_image = resultSet.getString("Cont_image")
                    )
                    aboutOrg = about
            }
            conn.close()

            return if (aboutOrg == null) {
                AboutOrgResponse(
                    tblAbout = null,
                    status = "success",
                    message = "माफ गर्नुहोस् !! समितिको बारेमा पोस्ट गरिएको छैन"
                )
            } else {
                AboutOrgResponse(
                    tblAbout = aboutOrg,
                    status = "success",
                    message = "success"
                )
            }

        } catch (e: Exception) {
            return AboutOrgResponse(
                tblAbout = null,
                status = "error",
                message = "माफ गर्नुहोस् सर्भरमा जडान गर्न सकिएन"
            )

        }
    }

    fun getContactList(): ContactsListResponse {
        val stmt: Statement?
        val resultSet: ResultSet?
        val query = "select * from TblDepartmentContact"
        val contactsList: MutableList<TblDepartmentContact> = arrayListOf()
        try {
            val ss = SqlServerFunctions()
            val conn: Connection = ss.ConnectToSQLServer(prefs)
            stmt = conn.createStatement()
            resultSet = stmt.executeQuery(query)
            while (resultSet.next()) {
                val contacts = TblDepartmentContact(
                    Dept_id = resultSet.getInt("Dept_id"),
                    Dept_name = resultSet.getString("Dept_name").orEmpty(),
                    Dept_contact = resultSet.getString("Dept_contact").orEmpty(),
                    Dept_mail = resultSet.getString("Dept_mail").orEmpty(),
                )
                contactsList.add(contacts)
            }
            conn.close()
            return ContactsListResponse(
                tblDepartmentContact = contactsList,
                status = "success",
                message = "success"
            )
        } catch (e: Exception) {
            return ContactsListResponse(
                tblDepartmentContact = contactsList,
                status = "error",
                message = "माफ गर्नुहोस् सर्भरमा जडान गर्न सकिएन"
            )
        }
    }

    fun getRequiredFiles(): DocumentTypesResponse {
        val stmt: Statement?
        val resultSet: ResultSet?
        val query = "select * from tblDocumentType"
        val contactsList: MutableList<RegistrationRequest.RequiredDocuments> = arrayListOf()
        try {
            val ss = SqlServerFunctions()
            val conn: Connection = ss.ConnectToSQLServer(prefs)
            stmt = conn.createStatement()
            resultSet = stmt.executeQuery(query)
            while (resultSet.next()) {
                val contacts: RegistrationRequest.RequiredDocuments =
                    RegistrationRequest.RequiredDocuments(
                        DocumentName = resultSet.getString("DocTypeName"),
                        DocImage = null,
                    )
                contactsList.add(contacts)
            }
            conn.close()
            return DocumentTypesResponse(
                documentTypes = contactsList,
                status = "success",
                message = "success"
            )
        } catch (e: java.lang.Exception) {
            return DocumentTypesResponse(
                documentTypes = contactsList,
                status = "error",
                message = "माफ गर्नुहोस् सर्भरमा जडान गर्न सकिएन"
            )
        }
    }

    fun requestForReg(data: RegistrationRequest?, context: Context): String {
        val stmt: Statement?
        var maxId = 0
        val ss = SqlServerFunctions()
        val conn: Connection = ss.ConnectToSQLServer(prefs)
        stmt = conn.createStatement()
        try {
            val rs: ResultSet =
                stmt.executeQuery("Select isnull(Max(ReqID),0) as ReqID from tblOnlineTapRequest")
            while (rs.next()) {
                maxId = Integer.parseInt(rs.getString("ReqID")) + 1
            }
        } catch (e: Exception) {
            loggerE("Unable Connect to Server: $e","DBTest")
//            Log.e("DBTest", "Unable Connect to Server", e)
        }
        val query1 =
            "INSERT INTO tblOnlineTapRequest (ReqID,MemName,Address,Gender,CitNo,ContactNo,FHName,GFILName,MaleCount,FemaleCount) " +
                    "VALUES ($maxId,'${data?.MemName.toString()}', '${
                        data?.Address.toString()
                    }', '${data?.Gender.toString()}', '${
                        data?.CitNo.toString()
                    }', '${data?.ContactNo.toString()}'," +
                    "'${data?.FHName.toString()}', '${
                        data?.GFILName.toString()
                    }', ${data?.MaleCount}, ${data?.FemaleCount} );"
        for (files in data?.files.orEmpty()) {
            var path: String
            try {
                path = FilePath.getRealPath(context, Uri.parse(files.DocImage)).orEmpty()
            } catch (ex: Exception) {
                val myUri = Uri.parse(files.DocImage)
                path = myUri.encodedPath.toString()
                FilePath.getRealPathFromURI(context, Uri.parse(files.DocImage)).orEmpty()
            }
            val pic = BitmapFactory.decodeFile(path)
            val docImg: ByteArray = getBytes(pic)
            val picSql =
                "Insert into tblOnlineTapReqDocImg(ReqID,DocumentName,DocImage) Values(?,?,?)"
            val pics = conn.prepareStatement(picSql)
            pics.setInt(1, maxId)
            pics.setString(2, files.DocumentName.toString())
            pics.setBytes(3, docImg)
            pics.execute()
        }
        stmt.execute(query1)
        conn.close()
        return "Success"
    }

    private fun getBytes(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        return stream.toByteArray()
    }

    fun getBillDetails(memberId: Int): BillDetailsResponse {
        val stmt: Statement?
        val resultSet: ResultSet?
        val query = "SELECT MemberID,MemName,TapNo,Address,TapType,RID" +
                ",TotReading,Amt,Inv_Date,Sam_Date,PaidStatus,Dis,Fine" +
                ",NetAmt FROM tblTempBillDetail Where Amt>0 and MemberID=" + memberId
            .toString()
        val billDetailsList: MutableList<BillDetails> = arrayListOf()

        try {
            val ss = SqlServerFunctions()
            val conn: Connection = ss.ConnectToSQLServer(prefs)
            stmt = conn.createStatement()
            val sPsql = "EXEC GenerateMemberBillDetail ?"
            val ps = conn.prepareStatement(sPsql)
            ps.setEscapeProcessing(true)
            ps.queryTimeout = 30
            ps.setString(1, memberId.toString())
            ps.executeUpdate()
            resultSet = stmt.executeQuery(query)
            val totalBillDetails = BillDetails(
                999999, null, 0, null, null, null, 0, 0f, null, null, 1, 0f, 0f, 0f
            )
            while (resultSet.next()) {
                totalBillDetails.TotReading =
                    totalBillDetails.TotReading?.plus(resultSet.getInt("TotReading"))
                totalBillDetails.Amt = totalBillDetails.Amt?.plus(resultSet.getInt("Amt"))
                totalBillDetails.Dis = totalBillDetails.Dis?.plus(resultSet.getInt("Dis"))
                totalBillDetails.Fine = totalBillDetails.Fine?.plus(resultSet.getInt("Fine"))
                totalBillDetails.NetAmt = totalBillDetails.NetAmt?.plus(resultSet.getInt("NetAmt"))
                totalBillDetails.MemberID = resultSet.getInt("MemberID")
                totalBillDetails.MemName = resultSet.getString("MemName")
                totalBillDetails.TapNo = resultSet.getInt("TapNo")
                totalBillDetails.Address = resultSet.getString("Address").orEmpty()
                totalBillDetails.TapType = resultSet.getString("TapType").orEmpty()
                totalBillDetails.RID = resultSet.getInt("RID")
                totalBillDetails.Inv_Date = resultSet.getString("Inv_Date").orEmpty()
                totalBillDetails.Sam_Date = resultSet.getString("Sam_Date").orEmpty()

                val billDetails = BillDetails(
                    MemberID = resultSet.getInt("MemberID"),
                    MemName = resultSet.getString("MemName").orEmpty(),
                    TapNo = resultSet.getInt("TapNo"),
                    Address = resultSet.getString("Address").orEmpty(),
                    TapType = resultSet.getString("TapType").orEmpty(),
                    RID = resultSet.getInt("RID"),
                    TotReading = resultSet.getInt("TotReading"),
                    Amt = resultSet.getFloat("Amt"),
                    Inv_Date = resultSet.getString("Inv_Date").orEmpty(),
                    Sam_Date = resultSet.getString("Sam_Date").orEmpty(),
                    Dis = resultSet.getFloat("Dis"),
                    Fine = resultSet.getFloat("Fine"),
                    NetAmt = resultSet.getFloat("NetAmt"),
                    PaidStatus = resultSet.getInt("PaidStatus")
                )
                billDetailsList.add(billDetails)
            }
            conn.close()
            if (billDetailsList.isNotEmpty()) {
                billDetailsList.add(totalBillDetails)
            }
            return BillDetailsResponse(
                billDetails = billDetailsList,
                message = "success",
                status = "success"
            )
        } catch (e: Exception) {
            return BillDetailsResponse(
                billDetails = billDetailsList,
                message = "सर्भरमा जडान गर्न सकिएन कृपया पछि फेरि प्रयास गर्नुहोस्",
                status = "error"
            )
        }
    }

    fun addTapResponse(phoneNo: String, pin: String): UserDetailsResponse {
        val stmt: Statement?
        val uID: String = phoneNo
        val pWD: String = pin
        var tapCount = 0
        val qry =
            "Select * from tblMember where ContactNo='$uID' and PinCode=$pWD"
        val tblMember: MutableList<TblMember> = arrayListOf()
        try {
            val ss = SqlServerFunctions()
            val conn: Connection = ss.ConnectToSQLServer(prefs)
            stmt = conn.createStatement()
            val resultSet: ResultSet? = stmt.executeQuery(qry)
            while (resultSet!!.next()) {
                tapCount += 1
                val member = TblMember(
                    MemberID = resultSet.getInt("MemberID"),
                    ContactNo = resultSet.getString("ContactNo"),
                    MemName = resultSet.getString("MemName"),
                    PinCode = resultSet.getString("PinCode"),
                    Address = resultSet.getString("Address"),
                    RegDateTime = resultSet.getString("RegDateTime")
                )
                tblMember.add(member)
            }
            if(tapCount>0){
                val fcmToken = prefs.fcmToken

                val qry2 =
                    "UPDATE tblMember SET FCMtoken = '${prefs.fcmToken}' WHERE MemberID=${tblMember[0].MemberID}"
                stmt.execute(qry2)
            }
            conn.close()
        } catch (e: Exception) {
            return UserDetailsResponse(
                tblMember = null,
                message = "सर्भरमा जडान गर्न सकिएन कृपया पछि फेरि प्रयास गर्नुहोस्",
                status = "error"
            )
        }
        if (tapCount == 0) {
            return UserDetailsResponse(
                tblMember = null,
                message = "सम्पर्क वा पिन सङ्केत मिल्दो छैन",
                status = "error"
            )
        }
        return UserDetailsResponse(
            tblMember = tblMember,
            message = "success",
            status = "success"
        )
    }

    @SuppressLint("SimpleDateFormat")
    fun requestPin(phoneNo: String, memberId: String): String {
        var rC = 0
        var sFRC = 0
        val code: Int
        val stmt: Statement?
        var resultSet: ResultSet?
        val tokenStr: String?
        val shortStr: String?
        val secCode: String
        val ss = SqlServerFunctions()
        val conn: Connection?
        val smsFeatQuery =
            "Select * from tblHospitalSetting Where SettingName='OTPSMSEnabled' and SettingValue='True'"
        try {
            conn = ss.ConnectToSQLServer(prefs)
            stmt = conn.createStatement()

            resultSet = stmt.executeQuery(smsFeatQuery)
            while (resultSet.next()) {
                sFRC += 1
            }

            if (sFRC == 0) {
                conn.close()
                return "SMS features is not activated yet"
            }

            val qry = "select * from tblMember where MemberID=" + memberId +
                    " and ContactNo='" + phoneNo + "'"

            resultSet = stmt.executeQuery(qry)
            while (resultSet.next()) {
                rC += 1
            }

            tokenStr = getFieldData(
                "SettingValue",
                "Select * from tblHospitalSetting Where SettingName='SMSTokenValue'", stmt
            )
            shortStr = getFieldData(
                "SettingValue",
                "Select * from tblHospitalSetting Where SettingName='SMSShortName'", stmt
            )

        } catch (e: Exception) {
            return "Couldn't connect to server please try again later"
        }

        if (rC > 0) {
            try {
                val d = Calendar.getInstance().time
                val df1 = SimpleDateFormat("smd") //second minute date
                secCode = df1.format(d)
                val url = URL("https://api.sparrowsms.com/v2/sms/")
                val connn = url.openConnection() as HttpURLConnection
                connn.doOutput = true
                connn.requestMethod = "POST"
                connn.setRequestProperty("Content-Type", "application/json")
                val input =
                    "{\"token\":\"" + tokenStr + "\",\"from\":\"" + shortStr + "\",\"to\":\"" + phoneNo +
                            "\",\"text\":\"Your Access Code for Dropcare is: " + secCode + "\"}"
                val os = connn.outputStream
                os.write(input.toByteArray())
                os.flush()
                code = connn.responseCode
                connn.disconnect()
            } catch (e: MalformedURLException) {
                loggerE(e.toString(),"connError")
                e.printStackTrace()
                return "Couldn't connect to SMS server"
            } catch (e: IOException) {
                loggerE(e.toString(),"connError")
                e.printStackTrace()

                return "Couldn't connect to SMS server"
            }
            if (code == 200) {
                try {
                    val mN: String = phoneNo
                    val mMID: Int = memberId.toInt()
                    val sC = secCode.toInt()
                    updateSecurityCode(mN, mMID, sC, stmt)
                    conn.commit()
                } catch (ex: Exception) {
                    Timber.i("SQL Exception Occurred")
                }
                conn.close()
                return "Access Code is sent to your mobile"
            } else {
                conn.close()
                return "SMS Connection Server Error"
            }
        } else {
            conn.close()
            return "Either Mobile or MemberID is not registered"
        }
    }

    private fun getFieldData(fieldName: String, qry: String, stmt: Statement?): String {
        val rs = stmt!!.executeQuery(qry)
        var retVal = "0"
        while (rs.next()) {
            retVal = rs.getString(fieldName)
        }
        return retVal
    }

    private fun updateSecurityCode(mn: String, mmId: Int, sc: Int, stmt: Statement) {
        val qry =
            "Update tblMember Set PinCode=$sc where MemberID=$mmId and ContactNo='$mn'"
        stmt.executeUpdate(qry)
    }

    fun changePin(phoneNo: String?, memberId: String?, newPin: String): String? {
        val stmt: Statement?
        val ss = SqlServerFunctions()
        val conn: Connection = ss.ConnectToSQLServer(prefs)
        stmt = conn.createStatement()
        return try {
            updateSecurityCode(
                phoneNo.toString(),
                memberId.toString().toInt(),
                newPin.toInt(),
                stmt
            )
            conn.close()
            "Success"
        } catch (a: Exception) {
            conn.close()
            "Error"
        }
    }

    fun addComplaint(message: String, memberID: String?, phoneNo: String?): String {
        val stmt: Statement?
        val ss = SqlServerFunctions()
        val conn: Connection = ss.ConnectToSQLServer(prefs)
        stmt = conn.createStatement()
        return try {
            val qry =
                "Insert into [tblComplaint]([MemberID],[ComplaintMsg],[ContactNumber]) Values(" + memberID?.trim() + ",N'" +
                        message.trim() + "','" + phoneNo?.trim() + "')"
            stmt.execute(qry)
            conn.close()
            "Success"
        } catch (a: Exception) {
            conn.close()
            "Error"
        }
    }

    fun getComplaintList(memberID: String?, phoneNo: String?): MutableList<ComplaintResponse>? {
        val stmt: Statement?
        val ss = SqlServerFunctions()
        val conn: Connection = ss.ConnectToSQLServer(prefs)
        stmt = conn.createStatement()
        val complaintList: MutableList<ComplaintResponse> = arrayListOf()
        return try {
            val resultSet: ResultSet?
            val qry =
                "SELECT TOP 100 * " +
                        "FROM tblComplaint " +
                        "where MemberID=${memberID?.toInt()} and ContactNumber='$phoneNo' "
//                        "ORDER BY ComptID DESC "
            resultSet = stmt.executeQuery(qry)

            while (resultSet.next()) {
                val list = ComplaintResponse(
                    ComptID = resultSet.getInt("ComptID"),
                    MemberID = resultSet.getInt("MemberID"),
                    ComplaintMsg = resultSet.getString("ComplaintMsg"),
                    ComptDate = resultSet.getString("ComptDate"),
                    IsRectified = resultSet.getInt("IsRectified")
                )
                complaintList.add(list)
            }
            conn.close()
            complaintList
        } catch (a: Exception) {
            conn.close()
            complaintList
        }
    }

    fun getActivities(context: Context): ActivitiesListResponse {
        val stmt: Statement?
        val resultSet: ResultSet?
        val query = "select * from TblActivity where IsActive=1"
        val activitiesList: MutableList<TblActivity> = arrayListOf()
        try {
            val ss = SqlServerFunctions()
            val conn: Connection = ss.ConnectToSQLServer(prefs)
            stmt = conn.createStatement()
            resultSet = stmt.executeQuery(query)
            while (resultSet.next()) {
                if (resultSet.getBytes("ActivityFile") != null) {
                    val data: ByteArray = resultSet.getBytes("ActivityFile")
                    val imageStream = ByteArrayInputStream(data)
                    val theImage = BitmapFactory.decodeStream(imageStream)
                    val image = File(
                        context.getExternalFilesDir(null),
                        "activityImage" + resultSet.getInt("ActivityID").toString() + ".png"
                    )
                    val fos = FileOutputStream(image)
                    fos.use { theImage.compress(Bitmap.CompressFormat.PNG, 100, it) }
                    val notices = TblActivity(
                        ActivityID = resultSet.getInt("ActivityID"),
                        ActivityHeadline = resultSet.getString("ActivityHeadline"),
                        ActivityDesc = resultSet.getString("ActivityDesc"),
                        DateNep = resultSet.getString("DateNep"),
                        DateTimeEng = resultSet.getString("DateTimeEng"),
                        ActivityFile = Uri.parse(image.path).toString()

                    )
                    activitiesList.add(notices)
                } else {

                    val notices = TblActivity(
                        ActivityID = resultSet.getInt("ActivityID"),
                        ActivityHeadline = resultSet.getString("ActivityHeadline"),
                        ActivityDesc = resultSet.getString("ActivityDesc"),
                        DateNep = resultSet.getString("DateNep"),
                        DateTimeEng = resultSet.getString("DateTimeEng"),
                        ActivityFile = null
                    )
                    activitiesList.add(notices)
                }
            }
            conn.close()
            return ActivitiesListResponse(
                tblActivity = activitiesList,
                status = "success",
                message = "success"
            )
        } catch (e: Exception) {
            return ActivitiesListResponse(
                tblActivity = activitiesList,
                status = "error",
                message = "माफ गर्नुहोस् सर्भरमा जडान गर्न सकिएन"
            )
        }
    }

    fun getSliderImages(context: Context): SliderListResponse {
        val stmt: Statement?
        val resultSet: ResultSet?
        val query = "SELECT TOP 4 * FROM TblSliderImages"
        val sliderList: MutableList<TblSliderImages> = arrayListOf()
        try {
            val ss = SqlServerFunctions()
            val conn: Connection = ss.ConnectToSQLServer(prefs)
            stmt = conn.createStatement()
            resultSet = stmt.executeQuery(query)
            while (resultSet.next()) {
                if (resultSet.getBytes("SliderImageFile") != null) {
                    val data: ByteArray = resultSet.getBytes("SliderImageFile")
                    val imageStream = ByteArrayInputStream(data)
                    val theImage = BitmapFactory.decodeStream(imageStream)
                    val image = File(
                        context.getExternalFilesDir(null),
                        "SliderImage" + resultSet.getInt("SliderID").toString() + ".png"
                    )
                    val fos = FileOutputStream(image)
                    fos.use { theImage.compress(Bitmap.CompressFormat.PNG, 100, it) }
                    val notices = TblSliderImages(
                        SliderID = resultSet.getInt("SliderID"),
                        SliderTitle = resultSet.getString("SliderTitle"),
                        SliderImageUrl = resultSet.getString("SliderImageUrl"),
                        Url = resultSet.getString("DateNep"),
                        SliderImageFile = Uri.parse(image.path).toString()
                    )
                    sliderList.add(notices)
                } else {
                    val notices = TblSliderImages(
                        SliderID = resultSet.getInt("SliderID"),
                        SliderTitle = resultSet.getString("SliderTitle"),
                        SliderImageUrl = resultSet.getString("SliderImageUrl"),
                        Url = resultSet.getString("Url"),
                        SliderImageFile = null
                    )
                    sliderList.add(notices)
                }
            }
            conn.close()

            return SliderListResponse(
                tblSliderImages = sliderList,
                status = "success",
                message = "success"
            )
        } catch (e: Exception) {
            return SliderListResponse(
                tblSliderImages = sliderList,
                status = "error",
                message = "माफ गर्नुहोस् सर्भरमा जडान गर्न सकिएन"
            )
        }
    }

    fun getLedgerDetails(memberId: Int): LedgerDetailsResponse {

        val stmt: Statement?
        val resultSet: ResultSet?

        val query = "SELECT RID" +
                " ,MemberID" +
                " ,Inv_Date" +
                " ,Sam_Date" +
                " ,TotReading" +
                " ,Amt" +
                " ,DisAmt" +
                " ,FineAmt" +
                " ,NetAmt" +
                " ,PayDateNep" +
                " ,PayDateEng" +
                " ,TapNo" +
                " ,PaidAmt" +
                " ,PaidStatus" +
                "  FROM TBLMemberReading where MemberID=" + memberId
            .toString()

        val billDetailsList: MutableList<TBLMemberReading> = arrayListOf()

        try {
            val ss = SqlServerFunctions()
            val conn: Connection = ss.ConnectToSQLServer(prefs)
            stmt = conn.createStatement()

            resultSet = stmt.executeQuery(query)

            val totalBillDetails = TBLMemberReading(
                999999, null, 0, 0, 0f, null, null, 0, 0f, 0f, 0f, null, null, 0f
            )

            while (resultSet.next()) {
                totalBillDetails.RID = resultSet.getInt("RID")
                totalBillDetails.MemberID = resultSet.getInt("MemberID")
                totalBillDetails.Inv_Date = resultSet.getString("Inv_Date").orEmpty()
                totalBillDetails.Sam_Date = resultSet.getString("Sam_Date").orEmpty()
                totalBillDetails.TotReading =
                    totalBillDetails.TotReading?.plus(resultSet.getInt("TotReading"))
                totalBillDetails.Amt = totalBillDetails.Amt?.plus(resultSet.getFloat("Amt"))
                totalBillDetails.Dis = totalBillDetails.Dis?.plus(resultSet.getFloat("DisAmt"))
                totalBillDetails.Fine = totalBillDetails.Fine?.plus(resultSet.getFloat("FineAmt"))
                totalBillDetails.NetAmt = totalBillDetails.NetAmt?.plus(resultSet.getInt("NetAmt"))
                totalBillDetails.PayDateNep = resultSet.getString("NetAmt").orEmpty()
                totalBillDetails.PayDateEng = resultSet.getString("NetAmt").orEmpty()
                totalBillDetails.TapNo = resultSet.getInt("TapNo")
                totalBillDetails.PaidAmt =
                    totalBillDetails.NetAmt?.plus(resultSet.getFloat("PaidAmt"))

                val billDetails = TBLMemberReading(
                    RID = resultSet.getInt("RID"),
                    MemberID = resultSet.getInt("MemberID"),
                    TapNo = resultSet.getInt("TapNo"),
                    TotReading = resultSet.getInt("TotReading"),
                    Amt = resultSet.getFloat("Amt"),
                    Inv_Date = resultSet.getString("Inv_Date").orEmpty(),
                    Sam_Date = resultSet.getString("Sam_Date").orEmpty(),
                    Dis = resultSet.getFloat("DisAmt"),
                    Fine = resultSet.getFloat("FineAmt"),
                    NetAmt = resultSet.getFloat("NetAmt"),
                    PaidStatus = resultSet.getInt("PaidStatus"),
                    PaidAmt = resultSet.getFloat("PaidAmt"),
                    PayDateNep = resultSet.getString("PayDateNep"),
                    PayDateEng = resultSet.getString("PayDateEng")
                )
                billDetailsList.add(billDetails)
            }

            conn.close()

            if (billDetailsList.isNotEmpty()) {
                billDetailsList.add(totalBillDetails)
            }

            return LedgerDetailsResponse(
                billDetails = billDetailsList,
                message = "success",
                status = "success"
            )

        } catch (e: Exception) {
            return LedgerDetailsResponse(
                billDetails = billDetailsList,
                message = "सर्भरमा जडान गर्न सकिएन कृपया पछि फेरि प्रयास गर्नुहोस्",
                status = "error"
            )
        }
    }

    fun getWalletList(): WalletListResponse {
        val stmt: Statement?
        val resultSet: ResultSet?

        val query = "SELECT TokID,Vendor,PublicToken FROM tblAPITokens  where PublicToken!='' AND PublicToken IS NOT NULL AND IsActive=1"

        val walletList: MutableList<WalletListResponse.WalletList> = arrayListOf()

        try {
            val ss = SqlServerFunctions()
            val conn: Connection = ss.ConnectToSQLServer(prefs)
            stmt = conn.createStatement()

            resultSet = stmt.executeQuery(query)


            while (resultSet.next()) {
                val wallet = WalletListResponse.WalletList(
                    TokID=resultSet.getString("TokID"),
                Vendor=resultSet.getString("Vendor"),
                PublicToken=resultSet.getString("PublicToken")
                )
                walletList.add(wallet)
            }

            conn.close()

            return WalletListResponse(
                walletList = walletList,
                message = "success",
                status = "success"
            )

        } catch (e: Exception) {
            return WalletListResponse(
                walletList = walletList,
                message = "सर्भरमा जडान गर्न सकिएन कृपया पछि फेरि प्रयास गर्नुहोस्",
                status = "error"
            )
        }
    }



    fun getAllMembers(): AllMembersListResponse {
        val stmt: Statement?
        val resultSet: ResultSet?
        val query = "SELECT * FROM tblMember where ActiveStatus =1 and IsVerified =1"

        val membersList: MutableList<TblCustomers> = arrayListOf()

        try {
            val ss = SqlServerFunctions()
            val conn: Connection = ss.ConnectToSQLServer(prefs)
            stmt = conn.createStatement()
            resultSet = stmt.executeQuery(query)
            while (resultSet.next()) {

                val tblContact = TblCustomers(
                    MemberID = resultSet.getInt("MemberID"),
                    MemName = resultSet.getString("MemName"),
                    MemAddNepali = resultSet.getString("MemAddNepali"),
                    MemNameNepali = resultSet.getString("MemNameNepali"),
                    Address = resultSet.getString("Address"),
                    ContactNo = resultSet.getString("ContactNo"),

                )
                membersList.add(tblContact)
            }
            conn.close()

            return AllMembersListResponse(
                tblContact = membersList,
                status = "success",
                message = "success"
            )
        } catch (e: Exception) {
            return AllMembersListResponse(
                tblContact = null,
                status = "error",
                message = "माफ गर्नुहोस् सर्भरमा जडान गर्न सकिएन"
            )
        }

    }
}