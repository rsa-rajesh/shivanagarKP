package com.heartsun.shivanagarkp.ui.activityes

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidcommon.base.ImmutableRecyclerAdapter
import androidcommon.base.VBViewHolder
import androidcommon.extension.load
import androidx.core.text.parseAsHtml
import androidx.core.view.isVisible
import com.heartsun.shivanagarkp.databinding.ItemNoticeListBinding
import com.heartsun.shivanagarkp.domain.dbmodel.TblActivity
import java.util.*
import kotlin.properties.Delegates

class ActivitiesListAdapter(
    private val onItemClick: (item: TblActivity) -> Unit = {}
) :
    ImmutableRecyclerAdapter<TblActivity, ItemNoticeListBinding>() {
    override var items: List<TblActivity> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    override fun getViewBinding(parent: ViewGroup) = ItemNoticeListBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: VBViewHolder<ItemNoticeListBinding>,
        position: Int
    ) {
        val item = items[position]
        with(holder.binding) {
            tvPublishedDate.text = "मिति :- " + item.DateNep.orEmpty()
            tvNoticeTitle.text = item.ActivityHeadline.orEmpty().parseAsHtml()
            tvNoticeDetails.text = item.ActivityDesc.orEmpty().parseAsHtml()
            val properNoun = arrayOf("#FE0000", "#0090B5", "#4E9D67", "#DB41E1", "#F83B00", "#515B3A","#7AC74F")
            val random = Random()
            val index: Int = random.nextInt(properNoun.size)
            cvMeroKahiPani.setStrokeColor(ColorStateList.valueOf(Color.parseColor(properNoun[index])))
            if (item.ActivityFile.isNullOrEmpty()) {
                cvImage.isVisible = false
            } else {
                ivImage.load( item.ActivityFile)
            }
        }

        holder.itemView.setOnClickListener {
            onItemClick(items[position])

        }
    }
}