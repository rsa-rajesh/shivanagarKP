package com.heartsun.shivanagarkp.customView

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2
import com.heartsun.shivanagarkp.R
import com.heartsun.shivanagarkp.data.Prefs
import com.heartsun.shivanagarkp.databinding.OnboardingViewBinding
import com.heartsun.shivanagarkp.entity.OnBoardingPage
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import android.graphics.Color
import com.google.android.material.internal.ContextUtils.getActivity
import com.heartsun.shivanagarkp.adapters.OnBoardingPagerAdapter
import com.heartsun.shivanagarkp.ui.OnBoardingActivity


class OnBoardingView @JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val numberOfPages by lazy { OnBoardingPage.values().size }
    private val prefManager: Prefs
    private lateinit var viewPager2: ViewPager2
    var positions = 0;
    private val binding: OnboardingViewBinding =
        OnboardingViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        setUpSlider(binding.root)
        Log.d("onboard", "aayoi")
        addingButtonsClickListeners()
        prefManager = Prefs(context.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE))
    }

    private fun setUpSlider(view: View) {
        with(binding.slider1) {
            adapter = OnBoardingPagerAdapter()
            setPageTransformer { page, position ->
                setParallaxTransformation(page, position)
            }
            viewPager2 = binding.slider1
            addSlideChangeListener()
            Log.d("onboard", "aayoi 2")
            val wormDotsIndicator = view.findViewById<WormDotsIndicator>(R.id.page_indicator)
            wormDotsIndicator.setViewPager2(this)
        }
    }


    private fun addSlideChangeListener() {
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            @SuppressLint("ResourceAsColor")
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (numberOfPages > 1) {
                    val newProgress = (position + positionOffset) / (numberOfPages - 1)
                    binding.onboardingRoot.progress = newProgress
                }
                when (position) {
                    1 -> {
                        binding.startBtn.text = "Next"
                        binding.startBtn.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#EF4D4D"))

                        binding.titleTv.text = context.getString(R.string.onboard_second_title)
                        binding.descTV.text = context.getString(R.string.onboard_second_details)
                    }
                    2 -> {
                        binding.startBtn.text = "Start"
                        binding.startBtn.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#27AE60"))
//                        binding.startBtn.backgroundTintList= ColorStateList.valueOf(Color.GREEN)

                        binding.titleTv.text = context.getString(R.string.onboard_third_title)
                        binding.descTV.text = context.getString(R.string.onboard_third_details)
                    }
                    else -> {
                        binding.startBtn.text = "Next"
                        binding.startBtn.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#EF4D4D"))

                        binding.titleTv.text = context.getString(R.string.onboard_first_title)
                        binding.descTV.text = context.getString(R.string.onboard_first_details)
                    }
                }
                positions = position;
            }
        })
    }

    private fun addingButtonsClickListeners() {
        binding.startBtn.setOnClickListener {
            if (positions == 2) {
                navigateToMainActivity()
            } else {
                navigateToNextSlide()
            }
        }
    }



    private fun navigateToNextSlide() {
        val nextSlidePos: Int = binding.slider1.currentItem.plus(1) ?: 0
        binding.slider1.setCurrentItem(nextSlidePos, true)
    }

    @SuppressLint("RestrictedApi")
    private fun navigateToMainActivity() {


        var activity:OnBoardingActivity = getActivity(this.context) as OnBoardingActivity
        activity.startActivity()


    }
}

fun setParallaxTransformation(page: View, position: Float) {
    page.apply {
        val parallaxView = page.findViewById<ImageView>(R.id.img)
        when {
            position < -1 -> // [-Infinity,-1)
                // This page is way off-screen to the left.
                alpha = 1f
            position <= 1 -> { // [-1,1]
                parallaxView.translationX = -position * (width / 2) //Half the normal speed
            }
            else -> // (1,+Infinity]
                // This page is way off-screen to the right.
                alpha = 1f
        }
    }

}