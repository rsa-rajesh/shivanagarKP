package com.heartsun.shivanagarkp.ui.about

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidcommon.RString
import androidcommon.base.BaseActivity
import com.heartsun.shivanagarkp.databinding.ActivityAboutAppBinding

class AboutAppActivity : BaseActivity() {

    private val binding by lazy {
        ActivityAboutAppBinding.inflate(layoutInflater)
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, AboutAppActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()

    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        with(binding) {
            ivBack.setOnClickListener {
                onBackPressed()
                this@AboutAppActivity.finish()
            }
            val version = getString(RString.app_version)
            tvAppVersion.text = "version $version"
        }
    }
}