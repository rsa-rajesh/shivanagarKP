package com.heartsun.shivanagarkp.ui.sameetee

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidcommon.base.ImmutableRecyclerAdapter
import androidcommon.base.VBViewHolder
import com.heartsun.shivanagarkp.databinding.ItemMemberTypeListBinding
import com.heartsun.shivanagarkp.domain.dbmodel.TblBoardMemberType
import java.util.*
import kotlin.properties.Delegates

class MemberTypeAdapter(
    private val onItemClick: (item: TblBoardMemberType) -> Unit = {}
) :
    ImmutableRecyclerAdapter<TblBoardMemberType, ItemMemberTypeListBinding>() {
    override var items: List<TblBoardMemberType> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    override fun getViewBinding(parent: ViewGroup) = ItemMemberTypeListBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: VBViewHolder<ItemMemberTypeListBinding>,
        position: Int
    ) {
        val item = items[position]
        with(holder.binding) {
            tv1.text = item.MemberType.toString()
            val properNoun = arrayOf("#FE0000", "#0090B5", "#4E9D67", "#DB41E1", "#F83B00", "#515B3A","#7AC74F")
            val random = Random()
            val index: Int = random.nextInt(properNoun.size)
            cvMeroKahiPani.setStrokeColor(ColorStateList.valueOf(Color.parseColor(properNoun[index])))

        }


        holder.itemView.setOnClickListener {
            onItemClick(items[position])

        }
    }
}