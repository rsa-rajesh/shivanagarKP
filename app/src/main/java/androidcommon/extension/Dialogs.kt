package androidcommon.extension

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.Window
import androidcommon.RStyle
import androidx.annotation.DrawableRes
import androidx.core.text.parseAsHtml
import com.heartsun.shivanagarkp.R
import com.heartsun.shivanagarkp.databinding.*

fun Context.getBaseDialog(): Dialog {
    return Dialog(this, R.style.WideDialog).apply {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
        }
    }
}

fun Context.showErrorDialog(
    message: String,
    label: String = "ठीक छ",
    title: String = "पानीको आपूर्ति",
    @DrawableRes icon: Int = R.drawable.ic_server_error,
    color: Int = Color.RED,
    isCancellable: Boolean = true,
    onBtnClick: () -> Unit = {},
) {
    val dialog = getBaseDialog()
    val dialogView = DialogErrorBinding.inflate(
        LayoutInflater.from(this),
        null,
        false
    )
    dialog.apply {
        setContentView(dialogView.root)
        setCancelable(isCancellable)
        show()
        with(dialogView) {
            tvTitle.text = title
            tvTitle.setTextColor(color)
            btnRetry.text = label
            tvMessage.text = message
            ivErrorImage.setImageResource(icon)

            btnRetry.setOnClickListener {
                dialog.dismiss()
                onBtnClick()
            }
        }
    }
}

fun Context.showCustomDialog(
    message: String,
    hideNegOption: Boolean = false,
    negLabel: String = "रद्द गर्नुहोस्",
    posLabel: String = "ठीक छ",
    onPosClick: () -> Unit = {},
    onNegClick: () -> Unit = {},
    icon: Int = R.drawable.logo) {
    val dialog = getBaseDialog()
    val dialogView = DialogPosNegOptionBinding.inflate(LayoutInflater.from(this), null, false)
    dialog.apply {
        setContentView(dialogView.root)
        setCancelable(false)
        show()
        with(dialogView) {
            tvPosAction.text = posLabel
            tvNegAction.text = negLabel
            tvMessage.text = message.parseAsHtml()
            ivLogo.setImageResource(icon)
            if (hideNegOption) {
                tvNegAction.gone()
                viewTwo.invisible()
            }

            tvNegAction.setOnClickListener {
                onNegClick()
                dismiss()
            }
            tvPosAction.setOnClickListener {
                onPosClick()
                dismiss()
            }
        }
    }
}

fun Context.showAddTapDialog(
    onAddClick: (phoneNo: String, pin: String) -> Unit,
    onRequestClick: () -> Unit,
    icon: Int = R.drawable.ic_notification
) {
    val dialog = getBaseDialog()
    dialog.window?.attributes?.windowAnimations=RStyle.DialogAnimation

    val dialogView = DilogAddNewBinding.inflate(LayoutInflater.from(this), null, false)
    dialog.apply {
        setContentView(dialogView.root)
        setCancelable(false)
        show()
        with(dialogView) {

            btAddNew.setOnClickListener {
                when {
                    phoneNumber.text.isNullOrBlank() -> {
                        tiPhoneNumber.error = "फोन नम्बर आवश्यक छ"
                    }
                    pin.text.isNullOrBlank() -> {
                        tiPin.error = "वैधता पिन आवश्यक छ"
                        tiPhoneNumber.error = null
                    }
                    else -> {
                        tiPin.error = null
                        tiPhoneNumber.error = null
                        onAddClick(phoneNumber.text.toString(), pin.text.toString())
                        dismiss()
                    }
                }
            }
            btRequestPin.setOnClickListener {
                onRequestClick()
                dismiss()
            }
            btClose.setOnClickListener {
                dismiss()
            }
        }
    }
}

fun Context.showRequestPinDialog(
    onAddClick: () -> Unit = {},
    onRequestClick: (phoneNo: String, memberId: String) -> Unit = { s: String, s1: String -> }
) {
    val dialog = getBaseDialog()
    dialog.window?.attributes?.windowAnimations=RStyle.DialogAnimation
    val dialogView = DilogRequestPinBinding.inflate(LayoutInflater.from(this), null, false)
    dialog.apply {
        setContentView(dialogView.root)
        setCancelable(false)
        show()
        with(dialogView) {
            btAddNew.setOnClickListener {
                onAddClick()
                dismiss()
            }
            btRequestPin.setOnClickListener {
                when {
                    phoneNumber.text.isNullOrBlank() -> {
                        tiPhoneNumber.error = "फोन नम्बर आवश्यक छ"
                    }
                    memberId.text.isNullOrBlank() -> {
                        tiMemberId.error = "सदस्य आईडी आवश्यक छ"
                        tiPhoneNumber.error = null
                    }
                    else -> {
                        tiMemberId.error = null
                        tiPhoneNumber.error = null
                        onRequestClick(phoneNumber.text.toString(), memberId.text.toString())
                        dismiss()
                    }
                }
            }
            btClose.setOnClickListener {
                dismiss()
            }
        }
    }
}

fun Context.showChangePinDialog(
    onChangeClick: (newPin: String) -> Unit = {},
    oldPinCode: Int = 0
) {
    val dialog = getBaseDialog()
    dialog.window?.attributes?.windowAnimations=RStyle.DialogAnimation
    val dialogView = DilogChangePinBinding.inflate(LayoutInflater.from(this), null, false)
    dialog.apply {
        setContentView(dialogView.root)
        setCancelable(false)
        show()
        with(dialogView) {


            btChange.setOnClickListener {

                when {
                    oldPin.text.isNullOrBlank() -> {
                        tiOldPin.error = "पुरानो पिन आवश्यक छ"
                    }

                    oldPin.text.toString().toInt() != oldPinCode -> {
                        tiOldPin.error = "पुरानो पिन मिलेन"
                    }

                    newPin.text.isNullOrBlank() -> {
                        tiNewPin.error = "नयाँ पिन आईडी आवश्यक छ"
                        tiOldPin.error = null
                    }
                    confirmPin.text.isNullOrBlank() -> {
                        tiConfirmPin.error = "यकिन पिन आईडी आवश्यक छ"
                        tiNewPin.error = null
                        tiOldPin.error = null

                    }

                    !confirmPin.text.toString().equals(newPin.text.toString(), false) -> {
                        tiConfirmPin.error = "यकीन पिन र नयाँ पिन मेल खाएन"
                        tiNewPin.error = null
                        tiOldPin.error = null

                    }

                    else -> {
                        tiConfirmPin.error = null
                        tiNewPin.error = null
                        tiOldPin.error = null

                        onChangeClick(newPin.text.toString())
                        dismiss()

                    }
                }
            }

            btClose.setOnClickListener {
                dismiss()
            }
        }
    }
}


