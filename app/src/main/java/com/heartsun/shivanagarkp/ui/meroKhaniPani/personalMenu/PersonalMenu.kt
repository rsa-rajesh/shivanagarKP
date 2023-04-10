package com.heartsun.shivanagarkp.ui.meroKhaniPani.personalMenu

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidcommon.RDrawable
import androidcommon.base.BaseActivity
import androidcommon.extension.showChangePinDialog
import androidcommon.extension.showCustomDialog
import androidcommon.extension.showErrorDialog
import com.heartsun.shivanagarkp.databinding.ActivityPersionalMenuBinding
import com.heartsun.shivanagarkp.ui.HomeActivity
import com.heartsun.shivanagarkp.ui.meroKhaniPani.MyTapViewModel
import com.heartsun.shivanagarkp.ui.meroKhaniPani.complaint.ComplaintActivity
import com.heartsun.shivanagarkp.ui.meroKhaniPani.ledger.LedgerActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class PersonalMenu : BaseActivity() {

    private val binding by lazy {
        ActivityPersionalMenuBinding.inflate(layoutInflater)
    }

    private val myTapViewModel by viewModel<MyTapViewModel>()

    private val name by lazy {
        intent.getStringExtra(name1)
    }

    private val address by lazy {
        intent.getStringExtra(address1)
    }

    private val memberId by lazy {
        intent.getStringExtra(memberId1)
    }

    private val registrationDate by lazy {
        intent.getStringExtra(registrationDate1)
    }

    private val phoneNo by lazy {
        intent.getStringExtra(phoneNo1)
    }

    private val pinCode by lazy {
        intent.getIntExtra(pinCode1, 0)
    }

    private var pinCodee = 0
    private var changedPin = 0

    companion object {
        private const val name1 = "NoticePublishedDate"
        private const val address1 = "NoticeImageUrl"
        private const val memberId1 = "NoticeTitle"
        private const val registrationDate1 = "NoticeDetails"
        private const val phoneNo1 = "PhoneNumber"
        private const val pinCode1 = "pinCode"

        fun newIntent(
            context: Context,
            address: String,
            memberId: String,
            registrationDate: String,
            name: String,
            phoneNo: String,
            pinCode: Int,
            ): Intent {
            return Intent(context, PersonalMenu::class.java).apply {
                putExtra(name1, name)
                putExtra(address1, address)
                putExtra(memberId1, memberId)
                putExtra(registrationDate1, registrationDate)
                putExtra(phoneNo1, phoneNo)
                putExtra(pinCode1, pinCode)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {

        with(binding) {
            tvName.text = "ग्राहकको नाम :- " + name.orEmpty()
            tvAddress.text = "ठेगाना :- " + address.orEmpty()
            tvDharaNo.text = "दर्ता न. :- " + memberId.orEmpty()
            tvRegistrationDate.text = "दर्ता भएको मिति :- " + registrationDate.orEmpty()

            pinCodee = pinCode

            toolbar.ivBack.setOnClickListener {
                onBackPressed()
                this@PersonalMenu.finish()
            }
            toolbar.tvToolbarTitle.text = "मेरो खानेपानी"

            listOf(cvLedger, cvComplain, cvChangePin,cvLogOut).forEach {
                it.setOnClickListener { view ->
                    when (view) {
                        cvComplain -> {
                            startActivity(
                                ComplaintActivity.newIntent(
                                    this@PersonalMenu,
                                    memberId.toString(), phoneNo.toString(),
                                )
                            )
                        }

                        cvLedger -> {
                            startActivity(
                                LedgerActivity.newIntent(
                                    this@PersonalMenu,
                                    memberId.toString(),name.toString(), address.toString(), memberId.toString()
                                )
                            )
                        }
                        cvChangePin -> {
                            changePinObserver()
                            showChangePinDialog(onChangeClick = { newPin ->
                                changePin(newPin = newPin)
                            }, oldPinCode = pinCodee.toString().toInt())
                        }
                        cvLogOut -> {

                            showCustomDialog(
                                message = "तपाईँ वास्तवमै लगआउट गर्न चाहनुहुन्छ ?",
                                negLabel = "रद्द गर्नुहोस्",
                                posLabel = "लगआउट",
                                onPosClick = {
                                    if (!memberId.isNullOrEmpty()){
                                        myTapViewModel.delete(members = memberId.toString().toInt())
                                        startActivity(
                                            HomeActivity.newIntent(
                                                this@PersonalMenu,"default"
                                            )
                                        )
                                    }
                                })

                        }
                    }
                }
            }
        }
    }

    private fun changePin(newPin: String) {
        changedPin = newPin.toInt()
        myTapViewModel.changePin(newPin, memberId, phoneNo)
    }

    private fun changePinObserver() {
        myTapViewModel.changePin.observe(this) {
            it ?: return@observe
            if (it.equals("success", true)) {
                myTapViewModel.update(memberId.toString().toInt(), changedPin)

                showErrorDialog(
                    message = "पिन कोड सफलतापूर्वक परिवर्तन भयो ।",
                    "बन्द गर्नुहोस्",
                    "सफलता",
                    RDrawable.ic_success_for_dilog,
                    color = Color.GREEN
                )
                pinCodee = changedPin

            } else {
                hideProgress()
                showErrorDialog(
                    message = "माफ गर्नुहोस्, अहिले पिन परिवर्तन गर्न सकिँदैन",
                    "बन्द गर्नुहोस्",
                    "त्रुटि",
                    RDrawable.ic_error_for_dilog,
                    color = Color.RED
                )
            }
        }
    }
}