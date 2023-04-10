package com.heartsun.shivanagarkp.ui.contact

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidcommon.base.ImmutableRecyclerAdapter
import androidcommon.base.VBViewHolder
import androidx.core.view.isVisible
import com.heartsun.shivanagarkp.databinding.ItemContactListBinding
import com.heartsun.shivanagarkp.domain.dbmodel.TblDepartmentContact
import java.util.*
import kotlin.properties.Delegates

class ContactListAdapter(
    private val onCallClick: (item: TblDepartmentContact) -> Unit = {},
    private val onMailClick: (item: TblDepartmentContact) -> Unit = {}
) :
    ImmutableRecyclerAdapter<TblDepartmentContact, ItemContactListBinding>() {
    override var items: List<TblDepartmentContact> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    override fun getViewBinding(parent: ViewGroup) = ItemContactListBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: VBViewHolder<ItemContactListBinding>,
        position: Int
    ) {
        val item = items[position]
        with(holder.binding) {
            tvTitle.text = item.Dept_name.toString().trim()
            tvPhone.text = "फोन नम्बर :- "+item.Dept_contact.toString().trim()

            val properNoun = arrayOf("#FE0000", "#0090B5", "#4E9D67", "#DB41E1", "#F83B00", "#515B3A","#7AC74F")
            val random = Random()
            val index: Int = random.nextInt(properNoun.size)
            cvMeroKahiPani.setStrokeColor(ColorStateList.valueOf(Color.parseColor(properNoun[index])))
            if (item.Dept_mail.toString().isEmpty()){
                tvMail.isVisible=false
                btMail.isVisible=false

            }
            if (item.Dept_contact.toString().isEmpty()){
                tvPhone.isVisible=false
                btCall.isVisible=false
            }

            tvMail.text = "इमेल :- "+item.Dept_mail.toString().trim()

            btCall.setOnClickListener {
                onCallClick(items[position])
            }
            btMail.setOnClickListener {
                onMailClick(items[position])
            }
        }



    }
}