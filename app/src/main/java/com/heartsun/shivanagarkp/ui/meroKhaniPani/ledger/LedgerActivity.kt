package com.heartsun.shivanagarkp.ui.meroKhaniPani.ledger

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidcommon.RDrawable
import androidcommon.base.BaseActivity
import androidcommon.extension.showErrorDialog
import androidcommon.extension.toastS
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.heartsun.shivanagarkp.databinding.ActivityLedgerBinding
import com.heartsun.shivanagarkp.domain.TBLMemberReading
import com.heartsun.shivanagarkp.ui.memberRegisterRequest.RegisterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.concurrent.thread

class LedgerActivity : BaseActivity() {

    private val binding by lazy {
        ActivityLedgerBinding.inflate(layoutInflater)
    }

    private val memberID by lazy {
        intent.getStringExtra(memberId)
    }
    private val memberName by lazy {
        intent.getStringExtra(member_Name)
    }
    private val memberAddress by lazy {
        intent.getStringExtra(member_Address)
    }

    private val memberRegNo by lazy {
        intent.getStringExtra(member_RegNo)
    }

    private val registerViewModel by viewModel<RegisterViewModel>()

    private lateinit var billDetailsAdapter: LedgerDetailsAdapter


    companion object {
        private const val memberId = "MemberID"
        private const val member_Name = "member_Name"
        private const val member_Address = "member_Address"
        private const val member_RegNo = "member_RegNo"

        fun newIntent(
            context: Context, memberID: String,memberName: String,memberAddress: String,memberRegNo: String,
        ): Intent {
            return Intent(context, LedgerActivity::class.java).apply {
                putExtra(memberId, memberID)
                putExtra(member_Name, memberName)
                putExtra(member_Address, memberAddress)
                putExtra(member_RegNo, memberRegNo)

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()

    }

    private fun initView() {
        with(binding) {
            rateFromServerObserver()
            showProgress()
            getReport()

            toolbar.tvToolbarTitle.text = "लेजर"

            toolbar.ivBack.setOnClickListener {
                super.onBackPressed()
                this@LedgerActivity.finish()
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun rateFromServerObserver() {
        registerViewModel.ledgerDetailsFromServer.observe(this) {

            it ?: return@observe
            hideProgress()
            if (it.status.equals("success", true)) {
                if (it.billDetails.isEmpty()) {
                    binding.cvCommunityRate.isVisible = false
                    toastS("सदस्यता नम्बर फेला परेन")
                } else {
                    val bills: MutableList<TBLMemberReading> = arrayListOf()
                    for (billDetails in it.billDetails) {
                        if (billDetails.PaidStatus != 1) {
                            bills.add(billDetails)
                        }
                    }

                    binding.tvName.text = "ग्राहकको नाम :- $memberName"
                    binding.tvAddress.text = "ठेगाना :- $memberAddress"
                    binding.tvDharaNo.text = "दर्ता न. :- $memberRegNo"
                    binding.tvDharaType.text = "धाराको प्रकार :-"

                    binding.cvCommunityRate.isVisible = true
                    billDetailsAdapter = LedgerDetailsAdapter()
                    billDetailsAdapter.items = it.billDetails

                    binding.rvCommunityRate.layoutManager = LinearLayoutManager(this)
                    binding.rvCommunityRate.adapter = billDetailsAdapter
                }
            } else {
                showErrorDialog(
                    message = "माफ गर्नुहोस्!!! सर्भरमा जडान गर्न सकेन \n" +
                            " कृपया पछि फेरि प्रयास गर्नुहोस्",
                    "पुन: प्रयास गर्नुहोस्",
                    "त्रुटि",
                    RDrawable.ic_error_for_dilog,
                    color = Color.RED
                )
            }
            hideProgress()
        }
    }

    private fun getReport() {
        showProgress()
        thread {
            Thread.sleep(100)
            registerViewModel.getLedgerDetails(memberId = memberID!!.toInt())
        }
    }
}