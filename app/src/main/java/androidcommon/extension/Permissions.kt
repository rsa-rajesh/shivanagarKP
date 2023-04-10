package androidcommon.extension

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat


fun Context.hasPermission(permission: String): Boolean {
    if (permission == Manifest.permission.ACCESS_BACKGROUND_LOCATION && Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
        return true
    return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Context.dialogShowRationale(
    title: String = "Permission Required",
    message: String = "Some permissions are denied. Those permissions are required to operate the application properly.",
    posLabel: String = "Ask Again",
    onPositiveClick: () -> Unit = {}
): AlertDialog {
    return AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(posLabel) { dialog, _ ->
            dialog.dismiss()
            onPositiveClick.invoke()
        }
        .setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        .show()
}

fun Context.dialogOnForeverDenied(
    title: String = "Permission Required",
    message: String = "Some permissions are denied permanently. Those permissions are required to operate the application properly. You can allow a permission from app settings.",
    onPositiveClick: () -> Unit
): AlertDialog {
    return AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("Open Settings") { _, _ ->
            onPositiveClick.invoke()
        }
        .setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        .show()
}

fun Context.openSettings() {
    startActivity(
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
    )
}