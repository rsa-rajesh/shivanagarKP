package androidcommon.handler

import androidcommon.App
import androidcommon.base.BaseErrorEntity
import androidcommon.extension.logException
import androidcommon.extension.loggerE
import androidcommon.utils.UiState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Response

suspend fun <T> Response<T>.handleResponse(doActionOnSuccess: suspend (body: T) -> Unit = {}): UiState<T> {
    val genericErrorMessage = "Error encountered. Please try again later."
    return if (isSuccessful||code() in listOf(201)) {
        if (body() != null) {
            doActionOnSuccess.invoke(body()!!)
            UiState.Success(body()!!)
        } else {
            UiState.Error(message())
        }
    }
    else if (code() in listOf(401)) {
//        App.instance.forceLogout()
        UiState.Error(message(), code())
    }
    /*else if (code() == 400) {
        loggerE("This is Error ${errorBody().toJson().orEmpty()}")
        UiState.Error(message(), code())
    } */ else {
        val errorBody = errorBody()?.string()
        loggerE(errorBody?.trim())
        return try {
            //Parse: {"error":"Invalid credentials"}
            //Parse: {"message":"Incorrect otp"}
            //Parse: {"detail":{"Incorrect otp"}}
            //Parse: {"errors":["Incorrect otp","Error 2"]}
            //Parse: {"non_field_errors":["Incorrect otp","Error 2"]}
            val errorModel = Gson().fromJson(errorBody, BaseErrorEntity::class.java)
            UiState.Error(
                errorModel.error ?: errorModel.message ?: errorModel.detail
                ?: errorModel.errors?.get(0)
                ?: errorModel.nonFieldErrors?.first()
                ?: errorModel.fail
                ?: throw Exception("Thrown to try another approach!")
            )
        } catch (e: Exception) {
            loggerE("On First Exception")
            e.logException()
            //Parse: {"password":["This field may not be blank."]}
            val type = object : TypeToken<Map<String, List<String>>>() {}.type
            val data: Map<String, List<String>> = Gson().fromJson(errorBody, type)
            return if (!data.isNullOrEmpty()) {
                val firstEntry = data.entries.iterator().next()
                val errorMessage = firstEntry.value
                if (errorMessage.isNullOrEmpty()) {
                    UiState.Error(message())
                } else {
                    //Display first message from the message list in given key
                    UiState.Error(errorMessage.first())
                }
            } else {
                UiState.Error(message())
            }
        } catch (e: Exception) {
            loggerE("On Second Exception")
            e.logException()
            //Parse: ["User with this email already exists"]*/
            val stringArrayType = object : TypeToken<List<String>>() {}.type
            val arrayMessages: List<String> = Gson().fromJson(errorBody, stringArrayType)
            return if (!arrayMessages.isNullOrEmpty()) {
                UiState.Error(arrayMessages[0])
            } else {
                UiState.Error(message())
            }
        } catch (e: Exception) {
            e.logException()
            UiState.Error(genericErrorMessage)
        }
    }
}
