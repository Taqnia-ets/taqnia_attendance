package com.example.taqniaattendance.ui.searching

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.taqniaattendance.data.model.history.Attendance
import com.example.taqniaattendance.databinding.ItemDaySummaryBinding

class SummaryAdapter(private val departmentHours : Int) : ListAdapter<Attendance, SummaryAdapter.CustomViewHolder>(Companion) {
    companion object : DiffUtil.ItemCallback<Attendance>() {
        override fun areItemsTheSame(oldItem: Attendance, newItem: Attendance): Boolean {
            return  oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Attendance, newItem: Attendance): Boolean {
            return  false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDaySummaryBinding.inflate(inflater, parent, false)

        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        with(holder.binding) {
            attendance = getItem(position)
            departmentWorkingHours = departmentHours
            executePendingBindings()
        }
    }

    // ViewHolder
    class CustomViewHolder(val binding: ItemDaySummaryBinding) : RecyclerView.ViewHolder(binding.root)
}