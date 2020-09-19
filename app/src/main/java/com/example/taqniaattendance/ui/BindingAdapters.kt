package com.example.taqniaattendance.ui

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.taqniaattendance.data.model.history.Attendance
import com.example.taqniaattendance.data.model.history.Punch
import com.example.taqniaattendance.data.model.notification.Notification
import com.example.taqniaattendance.ui.notification.NotificationsAdapter
import com.example.taqniaattendance.ui.searching.PunchesAdapter
import com.example.taqniaattendance.ui.searching.SummaryAdapter

import com.example.taqniaattendance.ui.searching.MainAdapter


//@BindingAdapter("app:imageUrl")
//fun loadImage(
//    view: ImageView,
//    url: String?
//) = WidgetUtil.loadImage(view, url, view.context)

@BindingAdapter("app:imageSrc")
fun loadImage(
    view: AppCompatImageView,
    src: Int
) = view.setImageResource(src)


@BindingAdapter("app:history")
fun setHistory(
    listView: RecyclerView,
    items: List<Attendance>?
) {
    (listView.adapter as MainAdapter).submitList(items)
}

@BindingAdapter("app:punches")
fun RecyclerView.setBooks(punches: List<Punch>?) {
    if (punches != null) {
            val punchesAdapter = PunchesAdapter()
            punchesAdapter.submitList(punches)
            adapter = punchesAdapter
    }
}

@BindingAdapter("app:items")
fun RecyclerView.setItem(items: List<Notification>?) {
    if (items != null) {
        val itemsAdapter = NotificationsAdapter()
        itemsAdapter.submitList(items)
        adapter = itemsAdapter
    }
}

@BindingAdapter("app:attendances")
fun RecyclerView.setSummary(items: List<Attendance>?) {
    if (items != null) {
        (this.adapter as SummaryAdapter).submitList(items)
    }
}

