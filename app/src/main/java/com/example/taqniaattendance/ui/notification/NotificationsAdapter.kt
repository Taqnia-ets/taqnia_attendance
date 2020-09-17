package com.example.taqniaattendance.ui.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.example.taqniaattendance.data.model.notification.Notification
import com.example.taqniaattendance.databinding.ItemNotificationBinding

class NotificationsAdapter : ListAdapter<Notification, NotificationsAdapter.CustomViewHolder>(Companion) {
    companion object : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return  oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return  false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNotificationBinding.inflate(inflater, parent, false)

        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        with(holder.binding) {
            notification = getItem(position)
            executePendingBindings()
        }
    }

    // ViewHolder
    class CustomViewHolder(val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root)
}