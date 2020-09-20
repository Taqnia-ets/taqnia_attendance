package com.example.taqniaattendance.ui.searching

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.taqniaattendance.data.model.history.Attendance
import com.example.taqniaattendance.databinding.ItemDaySummaryBinding
import kotlinx.android.synthetic.main.item_day_summary.view.*
import timber.log.Timber
import kotlin.math.roundToInt

class SummaryAdapter(private val departmentHours : Double) : ListAdapter<Attendance, SummaryAdapter.CustomViewHolder>(Companion) {
    companion object : DiffUtil.ItemCallback<Attendance>() {
        override fun areItemsTheSame(oldItem: Attendance, newItem: Attendance): Boolean {
            return  oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Attendance, newItem: Attendance): Boolean {
            return  false
        }
    }

    val BAR_HEIGHT = 280

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDaySummaryBinding.inflate(inflater, parent, false)
        binding.root.post {
            binding.root.layoutParams.width = parent.width/itemCount
            binding.root.requestLayout()
        }
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        with(holder.binding) {
            root.viewBar.layoutParams.height = getEmployeeWorkingHoursBarHeight(BAR_HEIGHT,
                departmentHours, getItem(position).getEmployeeWorkingHoursAsDouble())

            root.viewBarBg.layoutParams.height = BAR_HEIGHT
            attendance = getItem(position)
            departmentWorkingHours = departmentHours
            executePendingBindings()
        }
    }

    private fun getEmployeeWorkingHoursBarHeight(barHeight: Int, departmentWorkingHours: Double, EmployeeWorkingHours: Double) : Int {
        try {
            val workingHoursPercentage =  (barHeight / departmentWorkingHours) * EmployeeWorkingHours
            if (workingHoursPercentage > barHeight){
                return barHeight
            }

            return workingHoursPercentage.roundToInt()
        } catch (e : IllegalArgumentException) {
            Timber.e(e)
            return 0
        }
    }

    // ViewHolder
    class CustomViewHolder(val binding: ItemDaySummaryBinding) : RecyclerView.ViewHolder(binding.root)
}