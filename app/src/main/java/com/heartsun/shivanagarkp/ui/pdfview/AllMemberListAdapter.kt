package com.heartsun.shivanagarkp.ui.pdfview

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidcommon.base.ImmutableRecyclerAdapter
import androidcommon.base.VBViewHolder
import com.heartsun.shivanagarkp.databinding.ItemMembersListBinding
import com.heartsun.shivanagarkp.domain.dbmodel.TblCustomers
import java.util.*
import kotlin.properties.Delegates

class AllMemberListAdapter :
    ImmutableRecyclerAdapter<TblCustomers, ItemMembersListBinding>() {
    override var items: List<TblCustomers> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    override fun getViewBinding(parent: ViewGroup) = ItemMembersListBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )

    @SuppressLint("SetTextI18n", "CheckResult")
    override fun onBindViewHolder(
        holder: VBViewHolder<ItemMembersListBinding>,
        position: Int
    ) {
        val item = items[position]
        with(holder.binding) {

            val properNoun =
                arrayOf("#FE0000", "#0090B5", "#4E9D67", "#DB41E1", "#F83B00", "#515B3A", "#7AC74F")
            val random = Random()
            val index: Int = random.nextInt(properNoun.size)
            cvMeroKahiPani.setStrokeColor(ColorStateList.valueOf(Color.parseColor(properNoun[index])))
            if (item.MemNameNepali.length > 5) {
                tvName.text = item.MemNameNepali
            } else {
                tvName.text = item.MemName
            }

            if (item.MemAddNepali.toString().length > 5) {
                tvAddress.text = item.MemAddNepali.toString()
            } else {
                tvAddress.text = item.Address.toString()
            }
            memberId.text = item.MemberID.toString()
        }
    }
}