package com.heartsun.shivanagarkp.ui.sameetee

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.print.PDFPrint
import android.print.PrintAttributes
import android.view.View
import android.widget.AdapterView
import androidcommon.base.BaseActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.heartsun.shivanagarkp.databinding.ActivitySameeteeListBinding
import com.heartsun.shivanagarkp.ui.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.widget.ArrayAdapter
import androidcommon.RDrawable
import androidcommon.RLayout
import androidcommon.extension.showErrorDialog
import androidcommon.extension.toastS
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.heartsun.shivanagarkp.domain.dbmodel.TblContact
import com.heartsun.shivanagarkp.utils.pdfUtils.FileManager
import com.heartsun.shivanagarkp.utils.pdfUtils.PDFUtil
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*

class SameeteeListActivity : BaseActivity() {

    private val binding by lazy {
        ActivitySameeteeListBinding.inflate(layoutInflater)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                savePDF(data?.data!!)
            }
        }

    private fun savePDF(data: Uri) {

        showProgress("Generating PDF ....")
        val mType=memberType

        var htmlString: String =
            "<html><head><style>table {font-family: arial, sans-serif;border-collapse: collapse;width: 100%;}td, th {border: 1px solid #dddddd;text-align: left;padding: 8px;}tr:nth-child(even) {background-color: #dddddd;}</style>" +
                    "</head><body><h1 style=text-align:center>शिबनगर खानेपानी तथा सरसफाइ उपभोक्ता समिति</h1><h4 style=text-align:center>कावासोती- १६, पिप्रेनी, नवलपुर</h4><h2 style=text-align:center>$mType</h2><br><br><table><tr><th>नाम</th><th>पद</th><th>ठेगाना</th><th>सम्पर्क</th></tr>"

        for (datas in listItems) {
            htmlString =
                "$htmlString<tr><td>${datas.ContactName}</td><td>${datas.Post}</td><td>${datas.Address}</td><td>${datas.ContactNumber}</td></tr>"
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
                    copy(file, data) }
                override fun onError(exception: Exception) {
                    toastS("pdf error")
                    exception.printStackTrace()
                }
            }
        )

    }

    private lateinit var memberListAdapter: MemberListAdapter

    private val memberTypeId by lazy { intent.getIntExtra(memberId, -1) }
    private val memberType by lazy { intent.getStringExtra(memberTypee) }
    private val isOldMember by lazy { intent.getIntExtra(isOldMembers, 0) }
    var maxYear: Int = 2000
    var minYear: Int = 3000
    lateinit var originalItems: List<TblContact>
    lateinit var listItems: List<TblContact>

    companion object {
        private const val memberId = "memberTypeId"
        private const val memberTypee = "memberType"
        private const val isOldMembers = "isOldMember"

        fun newIntent(
            context: Context,
            memberTypeID: Int,
            memberType: String,
            isOldMember: Int
        ): Intent {
            return Intent(context, SameeteeListActivity::class.java).apply {
                putExtra(isOldMembers, isOldMember)
                putExtra(memberId, memberTypeID)
                putExtra(memberTypee, memberType)
            }
        }
    }

    private val homeViewModel by viewModel<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        if (isOldMember == 0) {
            homeViewModel.getMembers(memberTypeId)
        } else {
            binding.tvSelectYear.isVisible = true
            homeViewModel.getOldMembers(memberTypeId)
        }
        binding.toolbar.tvToolbarTitle.text = memberType
        binding.toolbar.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.btMakePDF.setOnClickListener {
            val date = Date()
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/pdf"
                putExtra(Intent.EXTRA_TITLE, "CK-Members_list$date")
            }
            resultLauncher.launch(intent)
//            CreatePdf(this)
//                .setPdfName("PK-Member_list$date")
//                .openPrintDialog(false)
//                .setContentBaseUrl(null)
//                .setPageSize(PrintAttributes.MediaSize.ISO_A4)
//                .setContent(htmlString)
//                .setFilePath(getExternalFilesDir(null)!!.absolutePath + "/MyPdf")
//                .setCallbackListener(object : CreatePdf.PdfCallbackListener {
//                    override fun onFailure(s: String) {
//                        hideProgress()
//                        Toast.makeText(
//                            this@SameeteeListActivity,
//                            "Error while generating PDF",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                    override fun onSuccess(s: String) {
//                        hideProgress()
//                            Toast.makeText(this@SameeteeListActivity, "Pdf Saved", Toast.LENGTH_SHORT)
//                                .show()
//                    }
//                })
//                .create()
        }

        binding.spYear.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {

                val year: String = binding.spYear.selectedItem.toString()
                if (year.equals("all", true)) {
                    listItems = originalItems
                    memberListAdapter.items = listItems

                } else {
                    if (year.isNotEmpty()) {
                        val tempItems: MutableList<TblContact> = ArrayList()

                        for (data in originalItems) {
                            if (year.toInt() >= data.Tenure.orEmpty()
                                    .split("-")[0].toInt() && year.toInt() <= data.Tenure.orEmpty()
                                    .split("-")[1].toInt()
                            ) {
                                tempItems.add(data)
                            }
                        }
                        listItems = tempItems
                        memberListAdapter.items = listItems
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
        listObserver()

        binding.swipeLayout.setOnRefreshListener {
            binding.swipeLayout.isRefreshing=true
            homeViewModel.deleteAllMemberType()
            homeViewModel.deleteAllMembers()
            membersFromServerObserver()
            homeViewModel.getMembersFromServer()
        }
    }

    private fun listObserver() {
        homeViewModel.membersFromLocal?.observe(this) { it ->
            it ?: return@observe

            binding.swipeLayout.isRefreshing=false
            if (!it.isNullOrEmpty()) {
                binding.btMakePDF.isVisible = true
                binding.clEmptyList.isVisible = false
                binding.rvMembers.isVisible = true
                memberListAdapter = MemberListAdapter()
                originalItems = it
                listItems = originalItems
                memberListAdapter.items = listItems
                binding.rvMembers.layoutManager = LinearLayoutManager(this)
                binding.rvMembers.adapter = memberListAdapter
                if (isOldMember == 1) {
                    binding.llSelectYear.isVisible = true
                    for (member in it) {
                        if (member.Tenure.orEmpty().split("-")[0].toInt() < minYear) {
                            minYear = member.Tenure.orEmpty().split("-")[0].toInt()
                        }
                        if (member.Tenure.orEmpty().split("-")[1].toInt() > maxYear) {
                            maxYear = member.Tenure.orEmpty().split("-")[1].toInt()
                        } }
                    val listData = getNumbersInRange()
                    val adapter = ArrayAdapter(
                        this,
                        RLayout.item_years_dropdown,
                        listData)
                    binding.spYear.adapter = adapter
                    binding.spYear.dropDownVerticalOffset = binding.spYear.height
                }
                hideProgress()
            }
        }
    }

    private fun getNumbersInRange(): List<String> {
        val array2: ArrayList<String> = ArrayList()
        array2.add("all")
        for (i in minYear until maxYear + 1 step 1) {
            array2.add(i.toString())
        }
        return array2.toList()
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
                if (isOldMember == 0) {
                    homeViewModel.getMembers(memberTypeId)
                } else {
                    binding.tvSelectYear.isVisible = true
                    homeViewModel.getOldMembers(memberTypeId)
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