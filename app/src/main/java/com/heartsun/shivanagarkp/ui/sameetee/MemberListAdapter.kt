package com.heartsun.shivanagarkp.ui.sameetee

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidcommon.RDrawable
import androidcommon.base.ImmutableRecyclerAdapter
import androidcommon.base.VBViewHolder
import androidcommon.extension.load
import com.heartsun.shivanagarkp.databinding.ItemMemberListBinding
import com.heartsun.shivanagarkp.domain.dbmodel.TblContact
import java.util.*
import kotlin.properties.Delegates

class MemberListAdapter :
    ImmutableRecyclerAdapter<TblContact, ItemMemberListBinding>() {
    override var items: List<TblContact> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    override fun getViewBinding(parent: ViewGroup) = ItemMemberListBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )

    @SuppressLint("SetTextI18n", "CheckResult")
    override fun onBindViewHolder(
        holder: VBViewHolder<ItemMemberListBinding>,
        position: Int
    ) {
        val item = items[position]
        with(holder.binding) {

            val properNoun = arrayOf("#FE0000", "#0090B5", "#4E9D67", "#DB41E1", "#F83B00", "#515B3A","#7AC74F")
            val random = Random()
            val index: Int = random.nextInt(properNoun.size)
            cvMeroKahiPani.setStrokeColor(ColorStateList.valueOf(Color.parseColor(properNoun[index])))
            tvName.text = item.ContactName.toString()

            if(!item.ContactNumber.isNullOrEmpty()){
                tvContact.text = item.ContactNumber.toString()
            }
            if(!item.Address.isNullOrEmpty()){
                tvAddress.text = item.Address.toString()
            }
            tvPosition.text = item.Post.toString()
            if(item.Image!=null){
                ivProfile.load(item.Image,RDrawable.loading_anim,RDrawable.logo)
            }
        }
    }
}