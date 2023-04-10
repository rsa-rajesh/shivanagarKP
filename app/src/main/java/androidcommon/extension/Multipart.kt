package androidcommon.extension

import android.content.Context
import android.net.Uri
import androidcommon.utils.FilePath
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

fun String.toRequestBody() =
    this.toRequestBody("multipart/form-data".toMediaTypeOrNull())

fun File.toMultiPart(partName: String/*, fileName: String*/): MultipartBody.Part {
    val requestFile = this.asRequestBody("image/*".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(partName, /*fileName*/this.path, requestFile)
}


fun Context.getFile(pathString: String?): File {
    val path = try {
        FilePath.getRealPath(this, Uri.parse(pathString)).orEmpty()
    } catch (ex: Exception) {
        FilePath.getRealPathFromURI(this, Uri.parse(pathString)).orEmpty()
    }
    return File(path)
}
