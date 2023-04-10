package com.heartsun.shivanagarkp.ui.sameetee

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
import com.heartsun.shivanagarkp.databinding.ActivitySameeteeSelectionBinding
import com.heartsun.shivanagarkp.ui.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.concurrent.thread

class SameeteeSelectionActivity : BaseActivity() {

    private val binding by lazy {
        ActivitySameeteeSelectionBinding.inflate(layoutInflater)
    }
    private val homeViewModel by viewModel<HomeViewModel>()
    private lateinit var memberTypeAdapter: MemberTypeAdapter

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, SameeteeSelectionActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        with(binding) {
            toolbar.tvToolbarTitle.text = "समिति"
            toolbar.ivBack.setOnClickListener {
                onBackPressed()
                this@SameeteeSelectionActivity.finish()
            }
            getMemberTypesFromDb()

            swipeLayout.setOnRefreshListener {
                swipeLayout.isRefreshing = true
                homeViewModel.deleteAllMemberType()
                homeViewModel.deleteAllMembers()
            }
        }
    }

    private fun getMemberTypesFromDb() {
        showProgress()

        homeViewModel.membersTypeFromLocalDb.observe(this) { it ->
            it ?: return@observe
            if (it.isNullOrEmpty()) {
                membersFromServerObserver()
                thread {
                    homeViewModel.getMembersFromServer()
                }

            } else {
                binding.clEmptyList.isVisible = false
                memberTypeAdapter = MemberTypeAdapter(
                    onItemClick = {
                        startActivity(
                            SameeteeListActivity.newIntent(
                                this,
                                it.MemTypeID,
                                it.MemberType.orEmpty(),
                                it.isOldMember
                            )
                        )
                    }
                )
                memberTypeAdapter.items = it
                binding.rvMemberList.layoutManager = LinearLayoutManager(this)
                binding.rvMemberList.adapter = memberTypeAdapter
                hideProgress()
            }
        }
    }

    private fun membersFromServerObserver() {
        homeViewModel.membersFromServer.observe(this) {
            it ?: return@observe
            hideProgress()
            binding.swipeLayout.isRefreshing=false

            if (it.status.equals("success", true)) {

                if (it.tblContact?.isEmpty() == true) {
                    binding.clEmptyList.isVisible = true
                } else {
                    binding.clEmptyList.isGone = true
                }
                for (memberType in it.tblBoardMemberType!!) {
//                    homeViewModel.deleteAllMemberType()
                    homeViewModel.insert(memberType)
                }
                for (members in it.tblContact!!) {
//                    homeViewModel.deleteAllMembers()
                    homeViewModel.insert(members)
                }




            } else {
                showErrorDialog(
                    message = "माफ गर्नुहोस्!!! सर्भरमा जडान गर्न सकेन",
                    "पुन: प्रयास गर्नुहोस्",
                    "त्रुटि",
                    RDrawable.ic_server_error,
                    color = Color.RED,
                    onBtnClick = {
                        homeViewModel.getMembersFromServer()
                    }
                )
            }
        }
    }
}