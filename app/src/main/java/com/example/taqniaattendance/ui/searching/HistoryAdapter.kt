package com.example.taqniaattendance.ui.searching

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.taqniaattendance.R
import com.example.taqniaattendance.data.model.history.Attendance
import kotlinx.android.synthetic.main.item_history.view.*

class HistoryAdapter : PagingDataAdapter<Attendance, HistoryAdapter.ViewHolder>(DataDifferntiator) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tvDate.text = getItem(position)?.getFormattedDate()
        holder.itemView.tvAttendanceState.text = getItem(position)?.attendanceStatus

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_history, parent, false)
        )
    }

    object DataDifferntiator : DiffUtil.ItemCallback<Attendance>() {

        override fun areItemsTheSame(oldItem: Attendance, newItem: Attendance): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: Attendance, newItem: Attendance): Boolean {
            return false
        }
    }

}