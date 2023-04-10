package com.heartsun.shivanagarkp.ui.meroKhaniPani.complaint

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidcommon.base.ImmutableRecyclerAdapter
import androidcommon.base.VBViewHolder
import com.heartsun.shivanagarkp.databinding.ItemMessageSentBinding
import com.heartsun.shivanagarkp.domain.ComplaintResponse
import kotlin.properties.Delegates
import android.text.format.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ComplaintListAdapter :
    ImmutableRecyclerAdapter<ComplaintResponse, ItemMessageSentBinding>() {
    override var items: List<ComplaintResponse> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    override fun getViewBinding(parent: ViewGroup) = ItemMessageSentBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: VBViewHolder<ItemMessageSentBinding>,
        position: Int
    ) {
        val item = items[position]
        with(holder.binding) {
            tvMessage.text = item.ComplaintMsg.toString()
            tvDate.text = convertDate(item.ComptDate.toString())
        }
    }

    private fun convertDate(sdate: String?): String? {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val nDate:Date?
        try {
            nDate = sdf.parse(sdate.toString())
        } catch (e: ParseException) {
            return sdate
        }
        val newDateTimeLong: Long = nDate.time
        val timeAgo = DateUtils.getRelativeTimeSpanString(
            newDateTimeLong,
            System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE
        )
        return timeAgo.toString()
    }

}