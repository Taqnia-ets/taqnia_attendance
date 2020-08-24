package com.example.taqniaattendance.ui.searching

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.example.taqniaattendance.data.model.history.Attendance
import com.example.taqniaattendance.databinding.ItemHistoryBinding

class VehiclesAdapter(private val viewModel: VehiclesViewModel)
    : ListAdapter<Attendance, VehiclesAdapter.ViewHolder>(TaskDiffCallback()) {

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) = holder.bind(viewModel, getItem(position), viewPool)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder.from(parent)

    class ViewHolder (private val binding: ItemHistoryBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            viewModel: VehiclesViewModel,
            item: Attendance,
            viewPool: RecyclerView.RecycledViewPool
        ) = with(binding) {
            viewmodel = viewModel
            attendance = item
            rcvPunches.setRecycledViewPool(viewPool)
            executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder = run {
                val binding = ItemHistoryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ViewHolder(binding)
            }
        }
    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class TaskDiffCallback : DiffUtil.ItemCallback<Attendance>() {
    override fun areItemsTheSame(oldItem: Attendance, newItem: Attendance)
            = false

    override fun areContentsTheSame(oldItem: Attendance, newItem: Attendance)
            = oldItem == newItem
}