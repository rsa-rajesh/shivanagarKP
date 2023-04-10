package com.heartsun.shivanagarkp.data

import android.content.SharedPreferences
import androidx.core.content.edit

class Prefs(private val sharedPreferences: SharedPreferences) {

    companion object {
        const val PREF_FIRST_TIME = "prefs.FIRST_TIME"
        const val PREF_UNIQUE_ID = "prefs.UNIQUE_ID"
        const val PREF_LOGGED_IN = "prefs.LOGGED_IN"
        const val PREF_TOKEN = "prefs.TOKEN"
        const val UserID = "prefs.UserID"
        const val PHONE_NUMBER = "prefs.phone_number"
        const val EMAIL = "prefs.email"
        const val IMAGE_DB = "prefs.image_db"
        const val RECORD_DB = "prefs.record_db"
        const val LAST_SEARCHED_MEMBER_ID = "prefs.last_searched_member_id"
        const val NO_OF_TAPS = "prefs.number_of_taps"
        const val DATABASE_NAME = "prefs.DATABASE_NAME"
        const val SERVER_IP = "prefs.SERVER_IP"
        const val SERVER_PORT = "prefs.SERVER_PORT"
        const val DATABASE_USER = "prefs.DATABASE_USER"
        const val DATABASE_PASSWORD = "prefs.DATABASE_PASSWORD"
        const val FCM_TOKEN = "prefs.FCM_TOKEN"
        const val APP_ID = "prefs.APP_ID"
    }



    var memberIds: String?
        get() = sharedPreferences.getString(LAST_SEARCHED_MEMBER_ID, "")
        set(value) {
            sharedPreferences.edit { putString(LAST_SEARCHED_MEMBER_ID, value) }
        }

    var imageDb: String?
        get() = sharedPreferences.getString(IMAGE_DB, "")
        set(value) {
            sharedPreferences.edit { putString(IMAGE_DB, value) }
        }

    var recordDb: String?
        get() = sharedPreferences.getString(RECORD_DB, "")
        set(value) {
            sharedPreferences.edit { putString(RECORD_DB, value) }
        }


    var isFirstTime: Boolean
        get() = sharedPreferences.getBoolean(PREF_FIRST_TIME, true)
        set(value) {
            sharedPreferences.edit { putBoolean(PREF_FIRST_TIME, value) }
        }


    var isLoggedIn: Boolean
        get() = sharedPreferences.getBoolean(PREF_LOGGED_IN, false)
        set(value) {
            sharedPreferences.edit { putBoolean(PREF_LOGGED_IN, value) }
        }




    var authToken: String?
        get() {
            val token = sharedPreferences.getString(PREF_TOKEN, null)
            return "Token ${token ?: return null}"
        }
        set(value) {
            sharedPreferences.edit { putString(PREF_TOKEN, value) }
        }


    var appId: String?
        get() {
            return sharedPreferences.getString(APP_ID, null)
        }
        set(value) {
            sharedPreferences.edit { putString(APP_ID, value) }
        }


    var userId: Int?
        get() {
            return sharedPreferences.getInt(PREF_UNIQUE_ID, 0)
        }
        set(value) {
            sharedPreferences.edit {
                if (value != null) {
                    putInt(PREF_UNIQUE_ID, value)
                }
            }
        }




    var phoneNumber: String?
        get() {
            val phoneNumber = sharedPreferences.getString(PHONE_NUMBER, null)
            return phoneNumber ?: return null
        }
        set(value) {
            sharedPreferences.edit { putString(PHONE_NUMBER, value) }
        }

    var email: String?
        get() {
            val email = sharedPreferences.getString(EMAIL, null)
            return email ?: return null
        }
        set(value) {
            sharedPreferences.edit { putString(EMAIL, value) }
        }

    fun logout() {
        sharedPreferences.edit(true) {
            clear()
        }
        isFirstTime = false
    }

    var noOfTaps: String?
        get() {
            val noOfTaps = sharedPreferences.getString(NO_OF_TAPS, null)
            return noOfTaps ?: return null
        }
        set(value) {
            sharedPreferences.edit { putString(NO_OF_TAPS, value) }
        }

//    const val SERVER_IP = "prefs.SERVER_IP"
//    const val SERVER_PORT = "prefs.SERVER_PORT"
//    const val DATABASE_USER = "prefs.DATABASE_USER"
//    const val DATABASE_PASSWORD = "prefs.DATABASE_PASSWORD"


    var databaseName: String?
        get() {
            val databaseName = sharedPreferences.getString(DATABASE_NAME, null)
            return databaseName ?: return null
        }
        set(value) {
            sharedPreferences.edit { putString(DATABASE_NAME, value) }
        }

    var databaseUser: String?
        get() {
            val databaseUser = sharedPreferences.getString(DATABASE_USER, null)
            return databaseUser ?: return null
        }
        set(value) {
            sharedPreferences.edit { putString(DATABASE_USER, value) }
        }

    var databasePassword: String?
        get() {
            val databasePassword = sharedPreferences.getString(DATABASE_PASSWORD, null)
            return databasePassword ?: return null
        }
        set(value) {
            sharedPreferences.edit { putString(DATABASE_PASSWORD, value) }
        }

    var serverIp: String?
        get() {
            val serverIp = sharedPreferences.getString(SERVER_IP, null)
            return serverIp ?: return null
        }
        set(value) {
            sharedPreferences.edit { putString(SERVER_IP, value) }
        }

    var serverPort: String?
        get() {
            val serverIp = sharedPreferences.getString(SERVER_PORT, null)
            return serverIp ?: return null
        }
        set(value) {
            sharedPreferences.edit { putString(SERVER_PORT, value) }
        }


    var fcmToken: String?
        get() {
            val serverIp = sharedPreferences.getString(FCM_TOKEN, null)
            return serverIp ?: return null
        }
        set(value) {
            sharedPreferences.edit { putString(FCM_TOKEN, value) }
        }
}