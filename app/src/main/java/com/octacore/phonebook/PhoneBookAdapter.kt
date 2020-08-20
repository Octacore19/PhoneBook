package com.octacore.phonebook

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.octacore.phonebook.databinding.ItemListBinding

class PhoneBookAdapter(
    private val phoneBookList: ArrayList<PhoneBook>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<PhoneBookAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_list,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = phoneBookList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = phoneBookList[position]
        holder.getBinding().contact = book
        holder.getBinding().listener = listener
    }

    class ViewHolder(private val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun getBinding() = binding
    }

    interface OnItemClickListener {
        fun onItemClick(book: PhoneBook)
    }
}