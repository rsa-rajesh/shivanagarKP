package com.heartsun.shivanagarkp.ui.memberRegisterRequest

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidcommon.base.BaseFragment
import androidcommon.extension.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.heartsun.shivanagarkp.R
import com.heartsun.shivanagarkp.databinding.BottomSheetImageSelectionBinding
import com.heartsun.shivanagarkp.databinding.FragmentMembersRegisterationFilesBinding
import com.heartsun.shivanagarkp.domain.RegistrationRequest


open class MembersRegistrationFilesFragment : BaseFragment<FragmentMembersRegisterationFilesBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMembersRegisterationFilesBinding.inflate(inflater, container, false)

    private lateinit var filesListAdapter: FilesListAdapter
    private var whichPermission = Manifest.permission.INTERNET
    private var onPermissionGranted: () -> Unit = {}

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private var indexOfImage: Int = -1

    var details: RegistrationRequest? = MemberRegisterActivity.registerRequest

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initViews() {
        filesListAdapter = FilesListAdapter(onAddFileClicked = {
            indexOfImage = it
            openPhotoActions()
        },
            onRemoveFileClicked = {
                details?.files?.get(it)?.DocImage = null
                filesListAdapter.items = details?.files.orEmpty()
                filesListAdapter.notifyItemChanged(it)
            })
        filesListAdapter.items = details?.files.orEmpty()
        binding.rvFiles.layoutManager = GridLayoutManager(context, 2)
        binding.rvFiles.adapter = filesListAdapter
        binding.btSubmit.setOnClickListener {
            validateFiles()
        }
    }

    private fun validateFiles() {
        var boolean = true

        for (data in details?.files.orEmpty()) {
            if (data.DocImage.isNullOrEmpty()) {
                boolean = false
            }
        }
        if (boolean){
            (activity as MemberRegisterActivity?)!!.requestRegistrationToServer()
        }else{
            toastS("कृपया सबै आवश्यक फाइलहरू छनोट गर्नुहोस् ।")
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> {
                    onPermissionGranted.invoke()
                }
                shouldShowRequestPermissionRationale(whichPermission) -> {
                    context?.dialogShowRationale { askPermissionAgain() }
                }
                else -> {
                    context?.dialogOnForeverDenied { context?.openSettings() }
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.M)
    open fun askPermission(whichPermission: String, onPermissionGranted: () -> Unit) {
        this.whichPermission = whichPermission
        this.onPermissionGranted = onPermissionGranted
        if (context?.hasPermission(whichPermission) == true) {
            onPermissionGranted.invoke()
        } else {
            requestPermissionLauncher.launch(whichPermission)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun askPermissionAgain() {
        requestPermissionLauncher.launch(whichPermission)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun openPhotoActions() {
        val bottomSheet = context?.let { BottomSheetDialog(it, R.style.BottomSheetDialog) }
        val bottomSheetView = BottomSheetImageSelectionBinding.inflate(layoutInflater, null, false)
        bottomSheet?.setContentView(bottomSheetView.root)
        with(bottomSheetView) {
            clCamera.setOnClickListener {
                askPermission(Manifest.permission.CAMERA) {
                    // takePicture.launch(updateViewModel.getUriToSaveImage())
                    pickFromCamera()
                }
                bottomSheet?.dismiss()
            }
            clGallery.setOnClickListener {
                askPermission(Manifest.permission.READ_EXTERNAL_STORAGE) {
                    // pickImage.launch("image/*")
                    pickFromGallery()
                }
                bottomSheet?.dismiss()
            }
        }
        bottomSheet?.show()
    }

    private fun pickFromCamera() {
        showProgress()
        ImagePicker.with(this)
            .compress(512)
            .cameraOnly()
            .crop()
            .maxResultSize(1080, 1080)
            .createIntent {
                startImagePicker.launch(it)
            }
    }

    private fun pickFromGallery() {
        showProgress()
        ImagePicker.with(this)
            .compress(512)
            .crop()
            .maxResultSize(1080, 1080)
            .galleryOnly()
            .createIntent {
                startImagePicker.launch(it)
            }
    }

    private val startImagePicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            val result = activityResult.data
            when (activityResult.resultCode) {
                AppCompatActivity.RESULT_OK -> {
                    val displayOnUi = result?.data
                    hideProgress()
                    if (indexOfImage != -1) {
                        details?.files?.get(indexOfImage)?.DocImage = displayOnUi.toString()
                        MemberRegisterActivity.registerRequest?.files?.get(indexOfImage)?.DocImage = displayOnUi.toString()
                        filesListAdapter.items = details?.files.orEmpty()
                        filesListAdapter.notifyItemChanged(indexOfImage)
                    }
                }
                ImagePicker.RESULT_ERROR -> {
                    toastS(ImagePicker.getError(result))
                }
                else -> {
                    hideProgress()
                }
            }
        }
}


