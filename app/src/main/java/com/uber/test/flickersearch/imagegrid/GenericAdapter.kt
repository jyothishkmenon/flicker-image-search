package com.uber.test.flickersearch.imagegrid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class GenericAdapter<Data, Binding: ViewDataBinding>:
        RecyclerView.Adapter<GenericAdapter<Data, Binding>.MyViewHolder>() {
    private var mItemList: MutableList<Data> = mutableListOf()
    abstract var resId: Int

    fun setData(itemList: MutableList<Data>) {
        mItemList.clear()
        mItemList = itemList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<Binding>(layoutInflater, resId, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mItemList.size
    }

    inner class MyViewHolder(val binding: Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Data) {
            bindToView(data, binding)
        }
    }

    abstract fun bindToView(data: Data, itemBinding: Binding)

    final override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val itemData = mItemList[position]
        holder.bind(itemData)
        registerListenersOnBind(holder.binding, itemData)
    }

    final override fun onBindViewHolder(holder: MyViewHolder, position: Int,
                                        payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
    }

    open fun registerListenersOnBind(binding: Binding, itemData: Data) {}
}
