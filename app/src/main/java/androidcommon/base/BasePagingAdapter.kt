package androidcommon.base

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


abstract class BasePagingAdapter<T : Any, VB> :
    PagingDataAdapter<T, BasePagingAdapter.VBViewHolder<VB>>(diffCallback = DiffUtilCallback<T>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VBViewHolder<VB> {
        return VBViewHolder(getViewBinding(parent = parent))
    }

    abstract fun getViewBinding(parent: ViewGroup): VB

    class VBViewHolder<VB>(val binding: VB) : RecyclerView.ViewHolder((binding as ViewBinding).root)


    class DiffUtilCallback<IT : Any> : DiffUtil.ItemCallback<IT>() {
        override fun areItemsTheSame(oldItem: IT, newItem: IT): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: IT, newItem: IT): Boolean {
            return oldItem == newItem
        }
    }
}
/*

fun <T : Any> T.diffUtilCallBack(): DiffUtil.ItemCallback<T> {
    return object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }
    }
}
*/
