package com.heartsun.shivanagarkp.ui

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidcommon.base.BaseActivity
import androidcommon.extension.logger
import androidcommon.extension.showErrorDialog
import androidcommon.utils.UiState
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.heartsun.shivanagarkp.R
import com.heartsun.shivanagarkp.data.Prefs
import com.heartsun.shivanagarkp.databinding.ActivitySplashBinding
import com.heartsun.shivanagarkp.utils.AppSignatureHelper

import kotlinx.coroutines.delay
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    private val prefs by inject<Prefs>()


    private val binding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    var  flag = "default"
    lateinit var appID: String
    private val homeViewModel by viewModel<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lifecycleScope.launchWhenCreated {
            delay(3000)
            navigateNext()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW
                )
            )
        }

        Firebase.messaging.subscribeToTopic("shivanagarkp")
            .addOnCompleteListener { task ->
                var msg = getString(R.string.msg_subscribed)
                if (!task.isSuccessful) {
                    msg = getString(R.string.msg_subscribe_failed)
                }
                logger(msg)
//                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            }

        // Get token
        // [START log_reg_token]
        Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                logger("Fetching FCM registration token failed",task.exception.toString())
//                Log.Timber.tag(TAG).w(task.exception, "Fetching FCM registration token failed")
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            prefs.fcmToken = token
            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            logger(msg)
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
        // [END log_reg_token]

        intent.extras?.let {
            for (key in it.keySet()) {
                val value = intent.extras?.get(key)
                logger("Key: $key Value: $value", "FCM")
            }
        }
        logger(prefs.fcmToken,"fcmToken")

    }

    @SuppressLint("SetTextI18n")
    private fun navigateNext() {
        binding.tvLoading.text = "connecting to server…"
        val appSignatureHelper = AppSignatureHelper(this)
//        appID = appSignatureHelper.appSignatures[0].toString()
        appID ="Manish12345"
//        appID ="Manish2"

        prefs.appId= appID

        if (prefs.isFirstTime) {
            serverDetailsObserver()
            homeViewModel.getServerDetailsFromAPI(appID)
//                        homeViewModel.getServerDetailsFromAPI("0e4bvyVX8JO")
        } else {
            if(intent.hasExtra("flag")){
                flag = intent.extras?.getString("flag")!!
            }
            startActivity(HomeActivity.newIntent(context = this,flag))
            this.finish()
//            startActivity(HomeActivity.newIntent(context = this))
//            this.finish()
        }
    }

    private fun serverDetailsObserver() {
        homeViewModel.serverDetails.observe(this) {
            it ?: return@observe
            binding.apply {
                when (it) {
                    is UiState.Loading -> {
                        showProgress()
                    }
                    is UiState.Success -> {
                        hideProgress()
                        if (it.data?.responseCode.equals("0")) {
                            prefs.databaseUser = it.data?.dbUserName
                            prefs.databasePassword = it.data?.dbPassword
                            prefs.databaseName = it.data?.databaseName
                            prefs.serverIp = it.data?.dbServerName?.split(",")?.get(0)
                            prefs.serverPort = it.data?.dbServerName?.split(",")?.get(1)
                            startActivity(HomeActivity.newIntent(context = this@SplashActivity,flag))
                        }
                    }
                    is UiState.Error -> {
                        hideProgress()
                        showErrorDialog(
                            isCancellable = false,
                            message = "सर्भरमा जडान गर्न सकेन!!! \n कृपया फेरि प्रयास गर्नुहोस्",
                            title = "सर्भरमा जडान गर्न सकेन!!!",
                            label = "फेरि प्रयास गर्नुहोस्",
                            onBtnClick = { retryClicked() })
                    }
                    else -> {
                        showErrorDialog(
                            isCancellable = false,
                            message = "सर्भरमा जडान गर्न सकेन!!! \n कृपया फेरि प्रयास गर्नुहोस्",
                            title = "सर्भरमा जडान गर्न सकेन!!!",
                            label = "फेरि प्रयास गर्नुहोस्",
                            onBtnClick = { retryClicked() })
                    }
                }
            }
        }
    }

    private fun retryClicked() {
        homeViewModel.getServerDetailsFromAPI(appID)
//        homeViewModel.getServerDetailsFromAPI("0e4bvyVX8JO")

    }
}