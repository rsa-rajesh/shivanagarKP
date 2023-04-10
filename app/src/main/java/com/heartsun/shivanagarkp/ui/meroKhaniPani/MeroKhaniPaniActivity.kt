package com.heartsun.shivanagarkp.ui.meroKhaniPani

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidcommon.RDrawable
import androidcommon.base.BaseActivity
import androidcommon.extension.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.heartsun.shivanagarkp.data.Prefs
import com.heartsun.shivanagarkp.databinding.ActivityMeroKhaniPaniBinding
import com.heartsun.shivanagarkp.ui.meroKhaniPani.personalMenu.PersonalMenu
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MeroKhaniPaniActivity : BaseActivity() {

    private val binding by lazy {
        ActivityMeroKhaniPaniBinding.inflate(layoutInflater)
    }

    private val prefs by inject<Prefs>()

    private lateinit var tapListAdapter: TapListAdapter

    private val myTapViewModel by viewModel<MyTapViewModel>()

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MeroKhaniPaniActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        requestPinFromServerObserver()
        addTapFromServerObserver()

        getTapsFromDb()
        with(binding) {
            toolbar.tvToolbarTitle.text = "मेरो खानेपानी"
            toolbar.ivBack.setOnClickListener {
                onBackPressed()
                this@MeroKhaniPaniActivity.finish()
            }
            listOf(btAdd, btAddNew).forEach {
                it.setOnClickListener {
                    openAddDialog()
                }
            }
        }

    }


    private fun getTapsFromDb() {
        showProgress()
        myTapViewModel.tapsListFromLocalDb.observe(this) { it ->
            it ?: return@observe
            if (it.isNullOrEmpty()) {
                binding.clEmptyList.isVisible = true
                prefs.noOfTaps = "0"
                hideProgress()

            } else {
                prefs.noOfTaps = it.size.toString()
                binding.clEmptyList.isVisible = false
                tapListAdapter = TapListAdapter(
                    onItemClick = {
                        startActivity(
                            PersonalMenu.newIntent(
                                this,
                                address = it.Address.toString(),
                                memberId = it.MemberID.toString(),
                                registrationDate = it.RegDateTime.toString().split(" ")[0],
                                name = it.MemName.toString(),
                                phoneNo = it.ContactNo.toString(),
                                it.PinCode.toString().toInt()
                            )
                        )
                    }, onDeleteClick = {
                        showCustomDialog(
                            message = "तपाईँ साँच्चिकै सूचीबाट यो धारा हटाउन चाहनुहुन्छ ?",
                            negLabel = "रद्द गर्नुहोस्",
                            posLabel = "हटाउनुहोस्",
                            onPosClick = {
                                myTapViewModel.delete(members = it.MemberID)
                            })
                    }
                )
                tapListAdapter.items = it
                binding.rvList.layoutManager = LinearLayoutManager(this)
                binding.rvList.adapter = tapListAdapter
                hideProgress()
            }
        }
    }


    private fun openRequestDialog() {
        showRequestPinDialog(onAddClick = {
            openAddDialog()
        }, onRequestClick = { phoneNo, memberId ->
            requestNewPin(phoneNo, memberId)
        })
    }

    private fun openAddDialog() {

        showAddTapDialog(onAddClick = { phoneNo, pin ->
            addTap(phoneNo, pin)
        }, onRequestClick = {
            openRequestDialog()
        })
    }

    private fun addTap(phoneNo: String, pin: String) {
        showProgress()
        myTapViewModel.addTap(phoneNo, pin)
    }

    private fun requestNewPin(phoneNo: String, memberId: String) {
        showProgress()
        myTapViewModel.requestPin(phoneNo, memberId)
    }

    private fun addTapFromServerObserver() {
        myTapViewModel.addTap.observe(this) {
            it ?: return@observe
            hideProgress()

            if (it.status.equals("error", true)) {
                showErrorDialog(
                    message = it.message,
                    "बन्द गर्नुहोस्",
                    "त्रुटि",
                    RDrawable.ic_error_for_dilog,
                    color = Color.RED
                )
            } else {
                for (member in it.tblMember!!) {
                    myTapViewModel.insert(members = member)
                }
            }


        }
    }

    private fun requestPinFromServerObserver() {
        myTapViewModel.pinRequest.observe(this) {
            it ?: return@observe
            hideProgress()

            if (it.equals("Access Code is sent to your mobile", true)) {
                showErrorDialog(
                    message = it,
                    "बन्द गर्नुहोस्",
                    "सफलता",
                    RDrawable.ic_success_for_dilog,
                    color = Color.GREEN
                )
            } else {
                showErrorDialog(
                    message = it,
                    "बन्द गर्नुहोस्",
                    "त्रुटि",
                    RDrawable.ic_error_for_dilog,
                    color = Color.RED
                )
            }
        }

    }

}