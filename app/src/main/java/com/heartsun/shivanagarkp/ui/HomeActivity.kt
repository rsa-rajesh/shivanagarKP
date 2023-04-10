package com.heartsun.shivanagarkp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidcommon.RDrawable
import androidcommon.base.BaseActivity
import androidcommon.extension.*
import com.heartsun.shivanagarkp.databinding.ActivityHomeBinding
import com.ouattararomuald.slider.ImageSlider
import com.ouattararomuald.slider.SliderAdapter
import com.ouattararomuald.slider.loaders.glide.GlideImageLoaderFactory
import androidx.core.text.parseAsHtml
import androidx.core.view.isVisible
import com.bumptech.glide.request.RequestOptions
import com.heartsun.shivanagarkp.R
import com.heartsun.shivanagarkp.data.Prefs
import com.heartsun.shivanagarkp.domain.dbmodel.TblMember
import com.heartsun.shivanagarkp.ui.about.AboutAppActivity
import com.heartsun.shivanagarkp.ui.about.AboutOrganizationActivity
import com.heartsun.shivanagarkp.ui.activityes.ActivitiesActivity
import com.heartsun.shivanagarkp.ui.billDetails.BillDetailsActivity
import com.heartsun.shivanagarkp.ui.contact.ContactActivity
import com.heartsun.shivanagarkp.ui.memberRegisterRequest.MemberRegisterActivity
import com.heartsun.shivanagarkp.ui.meroKhaniPani.MeroKhaniPaniActivity
import com.heartsun.shivanagarkp.ui.meroKhaniPani.MyTapViewModel
import com.heartsun.shivanagarkp.ui.meroKhaniPani.personalMenu.PersonalMenu
import com.heartsun.shivanagarkp.ui.noticeBoard.NoticeBoardActivity
import com.heartsun.shivanagarkp.ui.pdfview.PdfViewActivity
import com.heartsun.shivanagarkp.ui.sameetee.SameeteeSelectionActivity
import com.heartsun.shivanagarkp.ui.waterRate.WaterRateActivity
import com.heartsun.shivanagarkp.utils.AppSignatureHelper
import com.ouattararomuald.slider.ImageLoader
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.concurrent.thread


class HomeActivity : BaseActivity() {

    private val prefs by inject<Prefs>()
    private val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    private var doubleBackToExitPressedOnce = false

    private var count = 0
    private val homeViewModel by viewModel<HomeViewModel>()
    private val myTapViewModel by viewModel<MyTapViewModel>()
    private lateinit var imageSliderNew: ImageSlider

    private val flag by lazy {
        intent.getStringExtra(flagss)
    }

    lateinit var member: TblMember

    companion object {
        private const val flagss = "flag"
        fun newIntent(context: Context,flag :String): Intent {
            return Intent(context, HomeActivity::class.java).apply {
                putExtra(flagss, flag)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        prefs.isFirstTime = false

        if(flag.equals("notice",true)){
            startActivity(NoticeBoardActivity.newIntent(this@HomeActivity))
        }
        initView()
    }

    private fun initView() {
        with(binding) {
            slidersFromServerObserver()
            addTapFromServerObserver()
            requestPinFromServerObserver()
            thread {
                homeViewModel.getSlidersFromServer()
            }

            getTapsFromDb()
            ivLogo.setOnClickListener {
                val appSignatureHelper = AppSignatureHelper(this@HomeActivity)
                count += 1
                if (count == 5) {
                    toastL(appSignatureHelper.appSignatures[0].toString())
                    count = 0
                }
            }

            val powered =
                "<font color=#79B473>powered by:- </font><font color=#3B0D11>Heartsun Technology</font>"
            tvPoweredBy.text = powered.parseAsHtml()

            val version = getString(R.string.app_version)
            val versions =
                "<font color=#79B473>version: </font><font color=#3B0D11>$version</font>"
            tvVersion.text = versions.parseAsHtml()
            listOf(
                cvMeroKahiPani,
                cvNayaDhara,
                cvPanikoDar,
                cvSamitee,
                cvSamparka,
                cvSastakoBarama,
                cvSuchanaPati,
                tvPoweredBy,
                tvVersion,
                cvBillDetails,
                cvActivity, cvLogin, cvUser,cvMembers
            ).forEach {
                it.setOnClickListener { view ->
                    when (view) {
                        cvPanikoDar -> {
                            activateViews(false)
                            startActivity(WaterRateActivity.newIntent(this@HomeActivity))
                        }
                        cvMeroKahiPani -> {
                            activateViews(false)
                            startActivity(MeroKhaniPaniActivity.newIntent(this@HomeActivity))
                        }
                        cvNayaDhara -> {
                            activateViews(false)
                            startActivity(MemberRegisterActivity.newIntent(this@HomeActivity))
                        }
                        cvSamitee -> {
                            activateViews(false)
                            startActivity(SameeteeSelectionActivity.newIntent(this@HomeActivity))
                        }
                        cvMembers -> {
                            activateViews(false)
                            startActivity(PdfViewActivity.newIntent(this@HomeActivity))
                        }
                        cvSamparka -> {
                            activateViews(false)
                            startActivity(ContactActivity.newIntent(this@HomeActivity))
                        }
                        cvSastakoBarama -> {
                            activateViews(false)
                            startActivity(AboutOrganizationActivity.newIntent(this@HomeActivity))
                        }
                        cvSuchanaPati -> {
                            activateViews(false)
                            startActivity(NoticeBoardActivity.newIntent(this@HomeActivity))
                        }
                        cvBillDetails -> {
                            activateViews(false)
                            startActivity(BillDetailsActivity.newIntent(this@HomeActivity))
                        }
                        cvActivity -> {
                            activateViews(false)
                            startActivity(ActivitiesActivity.newIntent(this@HomeActivity))
                        }
                        tvVersion -> {
                            activateViews(false)
                            startActivity(AboutAppActivity.newIntent(this@HomeActivity))
                        }
                        cvLogin -> {
                            openAddDialog()
                        }
                        cvUser -> {
                            startActivity(
                                PersonalMenu.newIntent(
                                    this@HomeActivity,
                                    address = member.Address.toString(),
                                    memberId = member.MemberID.toString(),
                                    registrationDate = member.RegDateTime.toString().split(" ")[0],
                                    name = member.MemName.toString(),
                                    phoneNo = member.ContactNo.toString(),
                                    member.PinCode.toString().toInt()
                                )
                            )
                        }
                        tvPoweredBy -> {
                            val url = "https://www.heartsun.com.np/"
                            val i = Intent(Intent.ACTION_VIEW)
                            i.data = Uri.parse(url)
                            startActivity(i)
                        }
                    }
                }
            }

        }
    }

    private fun getSliderFromDb() {
        homeViewModel.sliderImagesFromLocalDb.observe(this) {
            it ?: return@observe
            if (!it.isNullOrEmpty()) {
                val imageUrls = arrayListOf<String>()
                val descriptions = arrayListOf<String>()
                for (a in it) {
                    descriptions.add(a.SliderTitle.orEmpty())
                    if (a.SliderImageUrl.isNullOrEmpty()) {
                        imageUrls.add(a.SliderImageFile.toString())
//                        imageUrls.add("https://play-lh.googleusercontent.com/1-hPxafOxdYpYZEOKzNIkSP43HXCNftVJVttoo4ucl7rsMASXW3Xr6GlXURCubE1tA=w3840-h2160-rw")

                    } else {
                        imageUrls.add(a.SliderImageUrl.toString())
//                        imageUrls.add("https://play-lh.googleusercontent.com/1-hPxafOxdYpYZEOKzNIkSP43HXCNftVJVttoo4ucl7rsMASXW3Xr6GlXURCubE1tA=w3840-h2160-rw")
                    }
                }
                imageSliderNew = binding.imageSlider
                binding.imageSlider.setIndicatorsBottomMargin(8)
                binding.imageSlider.adapter = SliderAdapter(
                    this@HomeActivity,
                    getImageLoader(),
                    imageUrls = imageUrls,
                    descriptions = descriptions,
                )
                hideProgress()
            }
        }
    }

    private fun slidersFromServerObserver() {
        homeViewModel.slidersFromServer.observe(this) {
            it ?: return@observe

            if (it.status.equals("success", true)) {
                if (!homeViewModel.sliderImagesFromLocalDb.value.isNullOrEmpty()) {
                    homeViewModel.deleteAllSlider(it.tblSliderImages[0])
                }
                for (slider in it.tblSliderImages) {
                    homeViewModel.insert(slider)
                }
                getSliderFromDb()
            } else {
                hideProgress()
                getSliderFromDb()
            }


        }
    }

    private fun activateViews(status: Boolean) {
        with(binding) {
            cvSamitee.isClickable = status
            cvImageSlider.isClickable = status
            cvSuchanaPati.isClickable = status
            cvSastakoBarama.isClickable = status
            cvSamparka.isClickable = status
            cvPanikoDar.isClickable = status
            cvNayaDhara.isClickable = status
            cvMeroKahiPani.isClickable = status
            cvActivity.isCheckable = status
            tvPoweredBy.isClickable = status
            cvBillDetails.isCheckable = status
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        binding.tvNoOfTaps.text = "धारा स्ख्या:- " + prefs.noOfTaps.orEmpty()
        activateViews(true)
    }

    private fun getImageLoader(): ImageLoader.Factory<out ImageLoader> {
        val requestOptions = RequestOptions.errorOf(R.drawable.error_placeholder)
            .placeholder(R.drawable.loading_anim)
        return GlideImageLoaderFactory(requestOptions = requestOptions)
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

    private fun openRequestDialog() {
        showRequestPinDialog(onAddClick = {
            openAddDialog()
        }, onRequestClick = { phoneNo, memberId ->
            requestNewPin(phoneNo, memberId)
        })
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

    @SuppressLint("SetTextI18n")
    private fun getTapsFromDb() {
        showProgress()
        myTapViewModel.tapsListFromLocalDb.observe(this) {
            it ?: return@observe
            if (it.isNullOrEmpty()) {
                binding.cvLogin.isVisible = true
                binding.cvUser.isVisible = false
                hideProgress()
            } else {
                binding.cvLogin.isVisible = false
                binding.cvUser.isVisible = true
                binding.tvName.text = "नाम :- " + it[0].MemName.orEmpty()
                binding.tvMemberId.text = "दर्ता न. :- " + it[0].MemberID.toString()
                member = it[0]
                hideProgress()
            }
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        toastS("Please click BACK again to exit")
        Handler(Looper.getMainLooper()).postDelayed( {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }
}