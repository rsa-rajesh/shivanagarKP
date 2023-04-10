package com.heartsun.shivanagarkp.ui.meroKhaniPani.complaint

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidcommon.RDrawable
import androidcommon.base.BaseActivity
import androidcommon.extension.showErrorDialog
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.heartsun.shivanagarkp.databinding.ActivityComplaintBinding
import com.heartsun.shivanagarkp.ui.meroKhaniPani.MyTapViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.concurrent.thread

class ComplaintActivity : BaseActivity() {

    private val myTapViewModel by viewModel<MyTapViewModel>()

    private val binding by lazy {
        ActivityComplaintBinding.inflate(layoutInflater)
    }

    private lateinit var complaintListAdapter: ComplaintListAdapter

    private val memberID1 by lazy {
        intent.getStringExtra(memberId)
    }
    private val phoneNo1 by lazy {
        intent.getStringExtra(phoneNO)
    }

    private var lastMessage: String = ""

    companion object {
        private const val memberId = "MemberID"
        private const val phoneNO = "PhoneNO"
        fun newIntent(
            context: Context, memberID: String, phoneNo: String,
        ): Intent {
            return Intent(context, ComplaintActivity::class.java).apply {
                putExtra(memberId, memberID)
                putExtra(phoneNO, phoneNo)
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
            toolbar.tvToolbarTitle.text = "गुनासो"
            toolbar.ivBack.setOnClickListener {
                onBackPressed()
                this@ComplaintActivity.finish()
            }
            showProgress()
            thread {
                myTapViewModel.getComplaintListServer(memberID1, phoneNo1)
            }
            ivSend.setOnClickListener {
                complaintAddObserver()
                if (!tvMessage.text.isNullOrBlank()) {
                    showProgress()
                    lastMessage = tvMessage.text.toString()
                    myTapViewModel.postComplaint(
                        tvMessage.text.toString(),
                        memberID1,
                        phoneNo1
                    )
                }
            }
        }
        complaintListObserver()
    }

    @SuppressLint("SetTextI18n")
    private fun complaintAddObserver() {
        myTapViewModel.addComplaint.observe(this) {

            it ?: return@observe
            if (it.equals("success", true)) {
                showProgress()
                myTapViewModel.getComplaintListServer(memberID1, phoneNo1)
                binding.tvMessage.setText("")
            } else {
                hideProgress()
                showErrorDialog(
                    message = "पठाउन अक्षम \n कृपया पछि फेरि प्रयास गर्नुहोस्",
                    "पुन: प्रयास गर्नुहोस्",
                    "त्रुटि",
                    RDrawable.ic_error_for_dilog,
                    color = Color.RED
                )
            }
            hideProgress()
        }
    }


    @SuppressLint("SetTextI18n")
    private fun complaintListObserver() {
        myTapViewModel.complaintList.observe(this) {
            it ?: return@observe
            if (!it.isNullOrEmpty()) {
                binding.noComplaints.isGone = true
                complaintListAdapter = ComplaintListAdapter()
                complaintListAdapter.items = it
                binding.rvMessages.layoutManager = LinearLayoutManager(this)
                binding.rvMessages.adapter = complaintListAdapter
                binding.rvMessages.scrollToPosition(it.size - 1)
                hideProgress()
            } else {
                binding.noComplaints.isVisible = true
            }
            hideProgress()
        }
    }
}