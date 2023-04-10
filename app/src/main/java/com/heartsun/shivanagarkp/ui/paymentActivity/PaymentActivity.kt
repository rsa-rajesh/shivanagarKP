package com.heartsun.shivanagarkp.ui.paymentActivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidcommon.base.BaseActivity
import androidcommon.extension.*
import androidcommon.utils.UiState
import androidx.core.view.isVisible
import com.heartsun.shivanagarkp.R
import com.heartsun.shivanagarkp.data.Prefs
import com.heartsun.shivanagarkp.databinding.ActivityPaymentBinding
import com.heartsun.shivanagarkp.ui.HomeViewModel
import com.heartsun.shivanagarkp.ui.billDetails.BillDetailsActivity
import com.khalti.checkout.helper.Config
import com.khalti.checkout.helper.KhaltiCheckOut
import com.khalti.checkout.helper.OnCheckOutListener
import com.khalti.checkout.helper.PaymentPreference
import org.koin.android.ext.android.inject
//import com.swifttechnology.imepaysdk.ENVIRONMENT
//import com.swifttechnology.imepaysdk.IMEPayment
//import com.swifttechnology.imepaysdk.IMEPaymentCallback
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.concurrent.thread


class PaymentActivity : BaseActivity() {

    private val binding by lazy {
        ActivityPaymentBinding.inflate(layoutInflater)
    }

    private val homeViewModel by viewModel<HomeViewModel>()

    private val prefs by inject<Prefs>()


    private var vName: String = ""
    private var vPassword: String = ""
    private var vToken: String = ""

        private var counterCode: String = "2020081704360002"
//    private var counterCode: String = "2508202002410003"

    private var request_id: String = ""
    var walletToken: String = ""
    private var paymentPreference: PaymentPreference = PaymentPreference.KHALTI
    private var payAmount = 0f
    var config: Config? = null
    private val memberID by lazy {
        intent.getStringExtra(memberId)
    }
    private val amount by lazy {
        intent.getStringExtra(amountE)
    }
    private var walletID: Int = 0;

    private var khaltiPublicKey: String = ""
    private var imePublicKey: String = ""

    companion object {
        private const val memberId = "MemberID"
        private const val amountE = "Amount"
        fun newIntent(
            context: Context, memberID: String, amount: String,
        ): Intent {
            return Intent(context, PaymentActivity::class.java).apply {
                putExtra(memberId, memberID)
                putExtra(amountE, amount)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        activeObservers()

        showProgress()
        thread {
            homeViewModel.getActiveWallet()
        }
        setKhaltiSDK()

        with(binding) {
            request_id = Date().time.toString()
            toolbar.tvToolbarTitle.text = "अनलाइन बिल भुक्तानी"
            toolbar.ivBack.setOnClickListener {
                onBackPressed()
            }
        }

        binding.btRetry.setOnClickListener {
            showProgress()
            homeViewModel.getActiveWallet()

        }
    }

    private fun setKhaltiSDK() {
        binding.btKhalti.setOnClickListener {
            walletID = 0
            paymentPreference = PaymentPreference.KHALTI
            homeViewModel.getWalletAuth("khalti", prefs.appId.toString())
        }
        binding.btEbanking.setOnClickListener {
            walletID = 0
            paymentPreference = PaymentPreference.EBANKING
            homeViewModel.getWalletAuth("khalti", prefs.appId.toString())
        }
        binding.btMbanking.setOnClickListener {
            walletID = 0
            paymentPreference = PaymentPreference.MOBILE_BANKING
            homeViewModel.getWalletAuth("khalti", prefs.appId.toString())
        }
        binding.btSCT.setOnClickListener {
            walletID = 0
            paymentPreference = PaymentPreference.SCT
            homeViewModel.getWalletAuth("khalti", prefs.appId.toString())
        }
        binding.btConnectIps.setOnClickListener {
            walletID = 0
            paymentPreference = PaymentPreference.CONNECT_IPS
            homeViewModel.getWalletAuth("khalti", prefs.appId.toString())
        }
        binding.btIMEPay.setOnClickListener {
            walletID = 0
            homeViewModel.getWalletAuth("imepay", prefs.appId.toString())
        }

    }

    private fun activeObservers() {
        homeViewModel.walletAuths.observe(this) {
            it ?: return@observe
            binding.apply {
                when (it) {
                    is UiState.Loading -> {
                        showProgress()
                    }
                    is UiState.Success -> {
                        hideProgress()
                        if (it.data?.responseCode.equals("0")) {
                            vName = it.data?.v_username.toString()
                            vPassword = it.data?.v_password.toString()
                            homeViewModel.getWalletToken(vName, vPassword, counterCode)
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
        homeViewModel.walletToken.observe(this) {
            it ?: return@observe
            binding.apply {
                when (it) {
                    is UiState.Loading -> {
                        showProgress()
                    }
                    is UiState.Success -> {
                        hideProgress()
                        vToken = it.data?.token.toString()
                        homeViewModel.getBillInquiry(
                            vName,
                            vPassword,
                            counterCode,
                            vToken,
                            memberID,
                            request_id
                        )
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
        homeViewModel.billInquiry.observe(this) {
            it ?: return@observe
            binding.apply {
                when (it) {
                    is UiState.Loading -> {
                        showProgress()
                    }
                    is UiState.Success -> {
                        hideProgress()

                        payAmount = it.data?.amount!!
                        when {
                            it.data.response_code == "0" -> {
                                showCustomDialog(
                                    message = getString(
                                        R.string.payment_message,
                                        it.data.properties.MemID,
                                        it.data.properties.Name,
                                        it.data.properties.Address,
                                        it.data.amount.toString()
                                    ),
                                    onPosClick = { proceedToPay() },
                                    onNegClick = { paymentCanceled() }
                                )
                            }
                            it.data.amount < 1 -> {

                                if (it.data.response_message.equals(
                                        "You have other amount pending bills...",
                                        true
                                    )
                                ) {
                                    showCustomDialog(
                                        message = getString(R.string.other_amount_remaining),
                                        hideNegOption = true, onPosClick = {
                                            onBackPressed()
                                        }
                                    )
                                } else {
                                    showCustomDialog(
                                        message = getString(R.string.no_due_amount_to_pay),
                                        hideNegOption = true, onPosClick = {
                                            onBackPressed()
                                        }
                                    )
                                }

//                                showCustomDialog(
//                                    message = getString(R.string.no_due_amount_to_pay),
//                                    hideNegOption = true
//                                )
                            }
                            else -> {
                                showErrorDialog(
                                    isCancellable = false,
                                    message = "माफ गर्नुहोस् केहि गलत भयो कृपया पुन: प्रयास गर्नुहोस्",
                                    title = "माफ गर्नुहोस्",
                                    label = "फेरि प्रयास गर्नुहोस्",
                                    onBtnClick = { retryClicked() })
                            }
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
        homeViewModel.confirmPayment.observe(this) {
            it ?: return@observe
            binding.apply {
                when (it) {
                    is UiState.Loading -> {
                        showProgress()
                    }
                    is UiState.Success -> {
                        hideProgress()
                        homeViewModel.paymentSuccess(
                            vName,
                            vPassword,
                            walletToken,
                            counterCode,
                            memberID,
                            payAmount
                        )
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

        homeViewModel.confirmSuccess.observe(this) {
            it ?: return@observe
            binding.apply {
                when (it) {
                    is UiState.Loading -> {
                        showProgress()
                    }
                    is UiState.Success -> {
                        hideProgress()
                        if (it.data?.response_code==null) {
                            showErrorDialog(
                                isCancellable = false,
                                message = "भुक्तानी सफल\n" +
                                        "अनलाइन भुक्तानी विधि प्रयोग गर्नुभएकोमा धन्यवाद ।",
                                title = "भुक्तानी पूरा भयो",
                                label = "ठिक छ ।",
                                icon = R.drawable.ic_success_for_dilog,
                                onBtnClick = { paymentSuccess() })
                        }else{
                            showErrorDialog(
                                isCancellable = false,
                                message = "सर्भरमा जडान गर्न सकेन!!! \n कृपया फेरि प्रयास गर्नुहोस्",
                                title = "सर्भरमा जडान गर्न सकेन!!!",
                                label = "फेरि प्रयास गर्नुहोस्",
                                onBtnClick = { retryClicked() })
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

        homeViewModel.walletListFromServer.observe(this) {
            it ?: return@observe
            binding.apply {
                hideProgress()
                if (it.status.equals("Success", true)) {
                    llNoPaymentPartner.isVisible = false

                    for (wallet in it.walletList) {
                        if (wallet.Vendor.equals("khalti", true)) {
                            khaltiPublicKey = wallet.PublicToken
                            btKhalti.isVisible = true
                            btConnectIps.isVisible = true
                            btEbanking.isVisible = true
                            btMbanking.isVisible = true
                            btSCT.isVisible = true
                        } else if (wallet.Vendor.equals("imepay", true)) {
                            imePublicKey = wallet.PublicToken
                            btIMEPay.isVisible = true
                        }
                    }
                } else {
                    llNoPaymentPartner.isVisible = true
                }

            }
        }

    }

    private fun paymentSuccess() {
        startActivity(BillDetailsActivity.newIntent(this))
    }

    private fun paymentCanceled() {
        toastS(getString(R.string.payment_process_cancelled))
    }

    private fun proceedToPay() {
        if (walletID == 0) {
            val khaltiRefId = khaltiPublicKey
            val khaltiBuilder = Config.Builder(khaltiRefId,
                "502",
                "Dudhrakshya-mobileapp-payment",
                (payAmount * 100).toLong(),
                object : OnCheckOutListener {
                    override fun onError(action: String, errorMap: Map<String, String>) {
                        loggerI("khalti_error", errorMap.toString())
                        loggerI("khalti_error2", action)
                        loggerI("success", "khalti")
                    }

                    override fun onSuccess(data: Map<String, Any>) {
                        Log.i("success_khalti", data.toString());
                        walletToken = data["token"].toString()
                        homeViewModel.confirmPayment(
                            vName,
                            vPassword,
                            vToken,
                            payAmount,
                            memberID,
                            walletToken,
                            request_id,
                            counterCode
                        )
                    }
                })
                .productUrl("https://heartsun.com.np/")

            khaltiBuilder.paymentPreferences(object :
                ArrayList<PaymentPreference?>() {
                init {
                    add(paymentPreference)
                }
            })
            config = khaltiBuilder.build()
            val khaltiCheckOut = KhaltiCheckOut(this, config)
            khaltiCheckOut.show()
        } else if (walletID == 1) {

//            val imePayment = IMEPayment(this, ENVIRONMENT.TEST)
//            imePayment.performPaymentV1(
//                "TEST",
//                "TEST",
//                "TEST",
//                "TEST",
//                "100.25",
//                "TEST",
//                "TEST",
//                "TEST",
//                "",
//                object : IMEPaymentCallback {
//
//                    override fun onSuccess(
//                        responseCode: Int,
//                        responseDescription: String?,
//                        transactionId: String?,
//                        msisdn: String?,
//                        amount: String?,
//                        refId: String?,
//                    ) {
////                        TODO("Not yet implemented")
////                        int responseCode, String responseDescription, String transactionId, Strings msidn, String amount, String refId
//                    }
//
//                    override fun onTransactionCancelled(p0: String?) {
////                        TODO("Not yet implemented")
//                    }
//
//                })

        }
    }

    private fun retryClicked() {
        homeViewModel.getWalletAuth("khalti", prefs.appId.toString())
    }
}