package com.heartsun.shivanagarkp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidcommon.base.BaseActivity
import com.heartsun.shivanagarkp.R

class OnBoardingActivity : BaseActivity() {


    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, OnBoardingActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
    }

    fun startActivity() {

        startActivity(HomeActivity.newIntent(this,"default"))
        this.finish()


    }
}