package androidcommon.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidcommon.widjets.ProgressDialogHelper
import androidcommon.widjets.dismissProgress
import androidcommon.widjets.showProgress
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    private var _binding: VB? = null
    val binding get() = _binding!!

    private val progressDialog: Dialog by lazy {
        ProgressDialogHelper.getProgressDialog(context = requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    open fun showProgress(message: String = "लोड गरिँदैछ...") {
        progressDialog.showProgress(message)
    }

    open fun hideProgress() {
        progressDialog.dismissProgress()
    }

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}