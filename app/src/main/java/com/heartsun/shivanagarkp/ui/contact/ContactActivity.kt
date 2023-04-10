package com.heartsun.shivanagarkp.ui.contact

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidcommon.base.BaseActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.heartsun.shivanagarkp.databinding.ActivityContactBinding
import com.heartsun.shivanagarkp.ui.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.net.Uri
import androidcommon.RDrawable
import androidcommon.extension.logger
import androidcommon.extension.loggerE
import androidcommon.extension.showErrorDialog
import androidx.core.view.isGone
import androidx.core.view.isVisible
import kotlin.concurrent.thread

class ContactActivity : BaseActivity() {

    private val binding by lazy {
        ActivityContactBinding.inflate(layoutInflater)
    }

    private lateinit var contactListAdapter: ContactListAdapter

    private val homeViewModel by viewModel<HomeViewModel>()

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ContactActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        with(binding) {
            toolbar.tvToolbarTitle.text = "सम्पर्क"
            toolbar.ivBack.setOnClickListener {
                onBackPressed()
                this@ContactActivity.finish()
            }
        }
        getContactsFromDb()
    }

    private fun getContactsFromDb() {
        showProgress()
        homeViewModel.contactsListFromLocalDb.observe(this) { it ->
            it ?: return@observe
            if (it.isNullOrEmpty()) {
                contactsFromServerObserver()
                thread {
                    homeViewModel.getContactsFromServer()
                }
            } else {
                binding.clEmptyList.isVisible = false
                contactListAdapter = ContactListAdapter(
                    onCallClick = {
                        try {
                            val intent = Intent(Intent.ACTION_DIAL)
                            intent.data = Uri.parse("tel:" + it.Dept_contact.toString())
                            startActivity(intent)
                        } catch (e: Exception) {
                            loggerE("Failed to invoke call")
                        }
                    },
                    onMailClick = {
                        val intent = Intent(Intent.ACTION_SENDTO)
                        intent.data = Uri.parse("mailto:") // only email apps should handle this
                        intent.putExtra(Intent.EXTRA_EMAIL, it.Dept_mail.toString())
                        logger(it.Dept_mail.toString(), "test mail")
                        intent.putExtra(Intent.EXTRA_SUBJECT, "")
                        if (intent.resolveActivity(packageManager) != null) {
                            startActivity(intent)
                        }
                    }
                )
                contactListAdapter.items = it
                binding.rvList.layoutManager = LinearLayoutManager(this)
                binding.rvList.adapter = contactListAdapter
                hideProgress()
            }
        }
    }

    private fun contactsFromServerObserver() {
        homeViewModel.contactsFromServer.observe(this) {
            it ?: return@observe
            hideProgress()
            if (it.status.equals("success", true)) {
                if (it.tblDepartmentContact.isNullOrEmpty()) {
                    binding.clEmptyList.isVisible = true
                } else {
                    binding.clEmptyList.isGone = true
                    for (memberType in it.tblDepartmentContact) {
                        homeViewModel.insert(contacts = memberType)
                    }
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
        }
    }
}