package com.heartsun.shivanagarkp.ui.memberRegisterRequest

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidcommon.base.ImmutableRecyclerAdapter
import androidcommon.base.VBViewHolder
import androidcommon.extension.load
import androidx.core.view.isVisible
import com.heartsun.shivanagarkp.databinding.ItemRegisterFilesListBinding
import com.heartsun.shivanagarkp.domain.RegistrationRequest
import java.util.*
import kotlin.properties.Delegates

class FilesListAdapter(
    private val onAddFileClicked: (item: Int) -> Unit = {},
    private val onRemoveFileClicked: (item: Int) -> Unit = {}

) :
    ImmutableRecyclerAdapter<RegistrationRequest.RequiredDocuments, ItemRegisterFilesListBinding>() {
    override var items: List<RegistrationRequest.RequiredDocuments> by Delegates.observable(
        emptyList()
    ) { _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    override fun getViewBinding(parent: ViewGroup) = ItemRegisterFilesListBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: VBViewHolder<ItemRegisterFilesListBinding>,
        position: Int
    ) {


        val item = items[position]
        with(holder.binding) {

            val properNoun = arrayOf("#FE0000", "#0090B5", "#4E9D67", "#DB41E1", "#F83B00", "#515B3A","#7AC74F")
            val random = Random()
            val index: Int = random.nextInt(properNoun.size)
            cvMeroKahiPani.setStrokeColor(ColorStateList.valueOf(Color.parseColor(properNoun[index])))



            tvFileName.text = item.DocumentName.toString()
            tvFileType.text = item.DocumentName.toString()
            if (item.DocImage==null){
                clAddFile.isVisible=true
                ivRemove.isVisible=false

            }else{
                clAddFile.isVisible=false
                ivRemove.isVisible=true
                ivFilePreview.load(item.DocImage.toString())
            }


            clAddFile.setOnClickListener{
                onAddFileClicked(position)
            }
            ivRemove.setOnClickListener{
                onRemoveFileClicked(position)
            }
        }
    }
}