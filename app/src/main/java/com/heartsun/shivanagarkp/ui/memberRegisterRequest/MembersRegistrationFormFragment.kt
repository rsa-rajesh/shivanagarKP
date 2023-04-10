package com.heartsun.shivanagarkp.ui.memberRegisterRequest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidcommon.base.BaseFragment
import androidx.navigation.Navigation
import com.heartsun.shivanagarkp.R
import com.heartsun.shivanagarkp.databinding.FragmentMembersRegisterationFormBinding
import com.heartsun.shivanagarkp.domain.RegistrationRequest

class MembersRegistrationFormFragment : BaseFragment<FragmentMembersRegisterationFormBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMembersRegisterationFormBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {

        binding.apply {
            btNext.setOnClickListener { view ->
                val details = RegistrationRequest(
                    Address = address.text.toString(),
                    MemName = memberName.text.toString(),
                    CitNo = citizenshipNumber.text.toString(),
                    ContactNo = contactNumber.text.toString(),
                    FHName = fatherOrHusbandName.text.toString(),
                    GFILName = grandFatherOrFatherInLawName.text.toString(),
                    MaleCount = maleCount.text.toString(),
                    FemaleCount = femaleCount.text.toString(),
                    Gender = getGender(rgGender.checkedRadioButtonId),
                    files = null
                )
                validateData(details, view)
            }
        }
    }

    private fun getGender(checkedRadioButtonId: Int): String {
        return if (checkedRadioButtonId == binding.male.id) {
            "mail"
        } else {
            "female"
        }
    }

    private fun validateData(request: RegistrationRequest, view: View) {
        binding.apply {
            when {
                request.MemName.isNullOrEmpty() -> {
                    clearError()
                    tiMemberName.requestFocus()
                    tiMemberName.error = "Name is required!"
                }
                request.Address.isNullOrEmpty() -> {
                    clearError()
                    tiAddress.error = "Email is required!"
                    tiAddress.requestFocus()
                }
                request.CitNo.isNullOrEmpty() -> {
                    clearError()
                    tiCitizenshipNumber.error = "Mobile number is required!"
                    tiCitizenshipNumber.requestFocus()
                }
                request.ContactNo.isNullOrEmpty() -> {
                    clearError()
                    tiContactNumber.error = "Mobile number is required!"
                    tiContactNumber.requestFocus()
                }
                request.FHName.isNullOrEmpty() -> {
                    clearError()
                    tiFatherOrHusbandName.error = "Mobile number is required!"
                    tiFatherOrHusbandName.requestFocus()
                }
                request.GFILName.isNullOrEmpty() -> {
                    clearError()
                    tiGrandFatherOrFatherInLawName.error = "Mobile number is required!"
                    tiGrandFatherOrFatherInLawName.requestFocus()
                }
                request.MaleCount.isNullOrEmpty() -> {
                    clearError()
                    tiMaleCount.error = "Mobile number is required!"
                    tiMaleCount.requestFocus()
                }
                request.FemaleCount.isNullOrEmpty() -> {
                    clearError()
                    tiFemaleCount.error = "Mobile number is required!"
                    tiFemaleCount.requestFocus()
                }
                else -> {

                    MemberRegisterActivity.registerRequest?.MemName = request.MemName
                    MemberRegisterActivity.registerRequest?.Address = request.Address
                    MemberRegisterActivity.registerRequest?.FemaleCount = request.FemaleCount
                    MemberRegisterActivity.registerRequest?.MaleCount = request.MaleCount
                    MemberRegisterActivity.registerRequest?.GFILName = request.GFILName
                    MemberRegisterActivity.registerRequest?.FHName = request.FHName
                    MemberRegisterActivity.registerRequest?.ContactNo = request.ContactNo
                    MemberRegisterActivity.registerRequest?.CitNo = request.CitNo
                    MemberRegisterActivity.registerRequest?.Gender = request.Gender

                    clearError()
                    if (request.Gender.isNullOrEmpty()) {
                        if (rgGender.checkedRadioButtonId == male.id) {
                            request.Gender = "Male"
                        } else {
                            request.Gender = "female"
                        }
                    }
                    Navigation.findNavController(view)
                        .navigate(R.id.action_membersRegistrationFormFragment_to_membersRegistrationFilesFragment)
                }
            }
        }
    }


    private fun clearError() {
        with(binding) {
            tiCitizenshipNumber.error = null
            tiFemaleCount.error = null
            tiMaleCount.error = null
            tiGrandFatherOrFatherInLawName.error = null
            tiFatherOrHusbandName.error = null
            tiContactNumber.error = null
            tiAddress.error = null
            tiMemberName.error = null
        }
    }
}