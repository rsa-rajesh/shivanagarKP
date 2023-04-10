package androidcommon.base

import android.Manifest
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import androidcommon.extension.dialogOnForeverDenied
import androidcommon.extension.dialogShowRationale
import androidcommon.extension.hasPermission
import androidcommon.extension.openSettings
import androidcommon.widjets.ProgressDialogHelper
import androidcommon.widjets.dismissProgress
import androidcommon.widjets.showProgress
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

abstract class BaseActivity : AppCompatActivity() {
    private var whichPermission = Manifest.permission.INTERNET
    private var onPermissionGranted: () -> Unit = {}

    private val progressDialog: Dialog by lazy {
        ProgressDialogHelper.getProgressDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    open fun showProgress(message: String = "Loading…") {
        progressDialog.showProgress(message)
    }

    open fun hideProgress() {
        progressDialog.dismissProgress()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> {
                    onPermissionGranted.invoke()
                }
                shouldShowRequestPermissionRationale(whichPermission) -> {
                    dialogShowRationale { askPermissionAgain() }
                }
                else -> {
                    dialogOnForeverDenied { openSettings() }
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.M)
    open fun askPermission(whichPermission: String, onPermissionGranted: () -> Unit) {
        this.whichPermission = whichPermission
        this.onPermissionGranted = onPermissionGranted
        if (hasPermission(whichPermission)) {
            onPermissionGranted.invoke()
        } else {
            requestPermissionLauncher.launch(whichPermission)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun askPermissionAgain() {
        requestPermissionLauncher.launch(whichPermission)
    }

}