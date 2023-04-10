package com.heartsun.shivanagarkp.entity
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.heartsun.shivanagarkp.R

enum class OnBoardingPage(
    @StringRes val titleResource: Int,
    @StringRes val descriptionResource: Int,
    @DrawableRes val logoResource: Int
) {

    ONE(R.string.onboarding_slide1_title, R.string.onboarding_slide1_desc, R.drawable.ic_onboarding1),
    TWO(R.string.onboarding_slide2_title, R.string.onboarding_slide2_desc, R.drawable.ic_onboarding2),
    THREE(R.string.onboarding_slide2_title, R.string.onboarding_slide1_desc, R.drawable.ic_onboarding3)

}