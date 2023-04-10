package androidcommon.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore


class FilePath {
    companion object {
        fun getRealPathFromURI(context: Context, contentUri: Uri?): String? {
            var cursor: Cursor? = null
            return try {
                val proj = arrayOf(MediaStore.Images.Media.DATA)
                cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
                cursor ?: return null
                cursor.moveToFirst()
                val columnIndex = cursor.getColumnIndex(proj[0])
                cursor.getString(columnIndex)
            } finally {
                cursor?.close()
            }
        }

        fun getRealPath(context: Context, uri: Uri): String? {
            var cursor: Cursor? = null
            try {
                val wholeID = DocumentsContract.getDocumentId(uri)

                val id = wholeID.split(":").toTypedArray()[1]

                val column = arrayOf(MediaStore.Images.Media.DATA)

                val sel = MediaStore.Images.Media._ID + "=?"

                cursor = context.contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, arrayOf(id), null
                )
                cursor ?: return null

                var filePath: String? = ""

                val columnIndex = cursor.getColumnIndex(column[0])

                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex)
                }
                return filePath;
            } finally {
                cursor?.close()
            }
        }
    }
}