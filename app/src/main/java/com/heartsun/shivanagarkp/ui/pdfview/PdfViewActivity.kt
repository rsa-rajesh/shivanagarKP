package com.heartsun.shivanagarkp.ui.pdfview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.print.PDFPrint
import android.print.PrintAttributes
import androidcommon.RDrawable
import androidcommon.base.BaseActivity
import androidcommon.extension.showErrorDialog
import androidcommon.extension.toastS
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.heartsun.shivanagarkp.databinding.ActivityPdfViewBinding
import com.heartsun.shivanagarkp.domain.dbmodel.TblCustomers
import com.heartsun.shivanagarkp.ui.HomeViewModel
import com.heartsun.shivanagarkp.utils.pdfUtils.FileManager
import com.heartsun.shivanagarkp.utils.pdfUtils.PDFUtil
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*

class PdfViewActivity : BaseActivity() {

    private val binding by lazy {
        ActivityPdfViewBinding.inflate(layoutInflater)
    }

    private lateinit var memberListAdapter: AllMemberListAdapter

    private val homeViewModel by viewModel<HomeViewModel>()

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                savePDF(data?.data!!)
            }
        }

    private fun savePDF(data: Uri) {

        showProgress("Generating PDF ....")
        val mType = "ग्राहकहरु"

        var htmlString: String =
            "<html><head><style>table {font-family: arial, sans-serif;border-collapse: collapse;width: 100%;}td, th {border: 1px solid #dddddd;text-align: left;padding: 8px;}tr:nth-child(even) {background-color: #dddddd;}</style>" +
                    "</head><body><h1 style=text-align:center>शिबनगर खानेपानी तथा सरसफाइ उपभोक्ता समिति</h1><h4 style=text-align:center>कावासोती- १६, पिप्रेनी, नवलपुर</h4><h2 style=text-align:center>$mType</h2><br><br><table><tr><th>नाम</th><th>Name</th><th>ठेगाना</th><th>सम्पर्क</th></tr>"

        for (datas in listItems) {
            htmlString =
                "$htmlString<tr><td>${datas.MemNameNepali}</td><td>${datas.MemName}</td><td>${datas.MemAddNepali}</td><td>${datas.ContactNo}</td></tr>"
        }
        htmlString = "$htmlString</table></body></html>"


        val savedPDFFile: File =
            FileManager.getInstance().createTempFile(applicationContext, "pdf", false)

        PDFUtil.generatePDFFromHTML(
            applicationContext,
            savedPDFFile,
            htmlString, PrintAttributes.MediaSize.ISO_A4,
            object : PDFPrint.OnPDFPrintListener {
                override fun onSuccess(file: File) {
                    copy(file, data)
                }

                override fun onError(exception: Exception) {
                    toastS("pdf error")
                    exception.printStackTrace()
                }
            }
        )

    }

    lateinit var listItems: List<TblCustomers>

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, PdfViewActivity::class.java)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {

        showProgress()
        homeViewModel.getAllMembers()
        binding.toolbar.tvToolbarTitle.text = "ग्राहकहरु"
        binding.toolbar.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.tvSearch.addTextChangedListener{

            if (binding.tvSearch.text.isEmpty()) {
                homeViewModel.getAllMembers()
            } else {
                homeViewModel.getFilteredMembers(binding.tvSearch.text.toString())
            }
            listObserver()
        }

        binding.btMakePDF.setOnClickListener {
            val date = Date()
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/pdf"
                putExtra(Intent.EXTRA_TITLE, "CK-Members_list$date")
            }
            resultLauncher.launch(intent)

        }
        listObserver()
        binding.swipeLayout.setOnRefreshListener {
            binding.swipeLayout.isRefreshing = true
            homeViewModel.deleteAllMemberType()
            homeViewModel.deleteAllMembers()
            allMembersFromServerObserver()
            homeViewModel.getMembersFromServer()
        }
    }


    private fun listObserver() {
        homeViewModel.allMembersFromLocal?.observe(this) { it ->
            it ?: return@observe

            binding.swipeLayout.isRefreshing = false
            if (!it.isNullOrEmpty()) {
                binding.btMakePDF.isVisible = true
                binding.clEmptyList.isVisible = false
                binding.rvMembers.isVisible = true
                listItems = it
                memberListAdapter = AllMemberListAdapter()
                memberListAdapter.items = listItems
                binding.rvMembers.layoutManager = LinearLayoutManager(this)
                binding.rvMembers.adapter = memberListAdapter
                hideProgress()
            } else {
                homeViewModel.getAllMembersFromServer()
                allMembersFromServerObserver()

            }
        }
    }

    private fun allMembersFromServerObserver() {
        homeViewModel.allMembersFromServer.observe(this) {
            it ?: return@observe
            binding.swipeLayout.isRefreshing = false

            if (it.status.equals("success", true)) {
                if (it.tblContact?.isEmpty() == true) {
                    binding.clEmptyList.isVisible = true
                } else {
                    binding.clEmptyList.isGone = true
                }
                for (members in it.tblContact!!) {
                    homeViewModel.insert(members)
                }
                homeViewModel.getAllMembers()

            } else {
                hideProgress()

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

    @Throws(IOException::class)
    fun copy(src: File?, dst: Uri) {
        FileInputStream(src).use { `in` ->
            this.contentResolver.openOutputStream(dst).use { out ->
                // Transfer bytes from in to out
                val buf = ByteArray(1024)
                var len: Int
                while (`in`.read(buf).also { len = it } > 0) {
                    out?.write(buf, 0, len)
                }
            }
        }
        hideProgress()
        toastS("pdf Saved")
    }
}