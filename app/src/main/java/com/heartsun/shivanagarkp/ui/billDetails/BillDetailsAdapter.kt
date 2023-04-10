package com.heartsun.shivanagarkp.ui.billDetails

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidcommon.base.ImmutableRecyclerAdapter
import androidcommon.base.VBViewHolder
import com.heartsun.shivanagarkp.databinding.ItemBillDetailsListBinding
import com.heartsun.shivanagarkp.domain.BillDetails
import kotlin.properties.Delegates

class BillDetailsAdapter :
    ImmutableRecyclerAdapter<BillDetails, ItemBillDetailsListBinding>() {
    override var items: List<BillDetails> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    override fun getViewBinding(parent: ViewGroup) = ItemBillDetailsListBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: VBViewHolder<ItemBillDetailsListBinding>,
        position: Int
    ) {
        val item = items[position]
        with(holder.binding) {

            if (position.mod(2) == 0) {
                root.setBackgroundColor(Color.parseColor("#9EA7F1"))
            } else {
                root.setBackgroundColor(Color.parseColor("#F3F2F2"))
            }

            tvDate.text=item.Sam_Date
            tvUnit.text=item.TotReading.toString()
            tvAmt.text=item.Amt.toString()
            tvNetAmt.text=item.NetAmt.toString()

            if ((item.Dis==0f ||item.Dis==null) && item.Fine==0f || item.Fine==null){
                tvDisOrFine.text="रु. 0"

            }else if (item.Dis!!>0f){
                tvDisOrFine.text=item.TotReading.toString()
                tvDisOrFine.text="रु."+item.Dis
                tvDisOrFine.setTextColor(Color.GREEN)
            }else{
                tvDisOrFine.text=item.TotReading.toString()
                tvDisOrFine.text="रु."+item.Fine
                tvDisOrFine.setTextColor(Color.RED)
            }




            if (position==items.size-1){
                root.setBackgroundColor(Color.parseColor("#111111"))

                tvDate.text="कुल"
                tvDate.setTextColor(Color.WHITE)
                tvUnit.setTextColor(Color.WHITE)
                tvAmt.setTextColor(Color.WHITE)
                tvNetAmt.setTextColor(Color.WHITE)

                if (item.Dis!! > item.Fine!!){
                    tvDisOrFine.setTextColor(Color.GREEN)
                    tvDisOrFine.text="रु."+(item.Dis!! - item.Fine!!).toString()
                }else{
                    tvDisOrFine.setTextColor(Color.RED)
                    tvDisOrFine.text="रु."+(item.Fine!! - item.Dis!!).toString()
                }
                tvAmt.setTextColor(Color.WHITE)

            }



        }
    }
}