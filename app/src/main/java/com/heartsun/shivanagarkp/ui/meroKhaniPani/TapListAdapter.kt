package com.heartsun.shivanagarkp.ui.meroKhaniPani

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidcommon.base.ImmutableRecyclerAdapter
import androidcommon.base.VBViewHolder
import com.heartsun.shivanagarkp.databinding.ItemCustomerListBinding
import com.heartsun.shivanagarkp.domain.dbmodel.TblMember
import kotlin.properties.Delegates

class TapListAdapter(
    private val onItemClick: (item: TblMember) -> Unit = {},
    private val onDeleteClick: (item: TblMember) -> Unit = {}
) :
    ImmutableRecyclerAdapter<TblMember, ItemCustomerListBinding>() {
    override var items: List<TblMember> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    override fun getViewBinding(parent: ViewGroup) = ItemCustomerListBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: VBViewHolder<ItemCustomerListBinding>,
        position: Int
    ) {
        val item = items[position]
        with(holder.binding) {
            tvName.text = "नाम :- " + item.MemName.toString().trim()
            tvMemberId.text = "दर्ता न. :- " + item.MemberID.toString().trim()
            btDelete.setOnClickListener {
                onDeleteClick(items[position])
            }
        }
        holder.binding.root.setOnClickListener {
            onItemClick(items[position])
        }


    }
}