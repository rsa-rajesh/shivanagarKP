package com.heartsun.shivanagarkp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidcommon.base.ImmutableRecyclerAdapter
import androidcommon.base.VBViewHolder
import com.heartsun.shivanagarkp.databinding.DilogLocationRequestBinding

class TestAdapter: ImmutableRecyclerAdapter<String,DilogLocationRequestBinding>() {
    override var items: List<String>
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun getViewBinding(parent: ViewGroup)= DilogLocationRequestBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    override fun onBindViewHolder(
        holder: VBViewHolder<DilogLocationRequestBinding>,
        position: Int) {
        with(
            holder.binding
        ){
//your viw hear
        }
    }
}