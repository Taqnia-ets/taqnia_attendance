package com.example.taqniaattendance.ui.searching

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.taqniaattendance.data.model.history.Punch
import com.example.taqniaattendance.databinding.ItemPunchBinding


class PunchesAdapter : ListAdapter<Punch, PunchesAdapter.CustomViewHolder>(Companion) {
    companion object : DiffUtil.ItemCallback<Punch>() {
        override fun areItemsTheSame(oldItem: Punch, newItem: Punch): Boolean {
            return  oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Punch, newItem: Punch): Boolean {
            return  oldItem.timestamp == newItem.timestamp
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPunchBinding.inflate(inflater, parent, false)

        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        with(holder.binding) {
            punch = getItem(position)
            executePendingBindings()
        }
    }

    // ViewHolder
    class CustomViewHolder(val binding: ItemPunchBinding) : RecyclerView.ViewHolder(binding.root)
}