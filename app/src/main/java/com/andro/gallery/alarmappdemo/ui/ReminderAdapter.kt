package com.andro.gallery.alarmappdemo.ui


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andro.gallery.alarmappdemo.data.Reminder
import com.andro.gallery.alarmappdemo.databinding.ItemReminderBinding
import com.andro.gallery.alarmappdemo.util.DateTimeUtils

class ReminderAdapter(
    private val onEdit: (Long) -> Unit,
    private val onDelete: (Reminder) -> Unit
) : ListAdapter<Reminder, ReminderAdapter.VH>(DIFF) {


    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Reminder>() {
            override fun areItemsTheSame(oldItem: Reminder, newItem: Reminder) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Reminder, newItem: Reminder) =
                oldItem == newItem
        }
    }


    inner class VH(val b: ItemReminderBinding) : RecyclerView.ViewHolder(b.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemReminderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }


    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.b.txtName.text = item.name
        holder.b.txtDetails.text = item.details
        holder.b.txtTime.text = DateTimeUtils.format(item.timeMillis)
        holder.b.btnEdit.setOnClickListener { onEdit(item.id) }
        holder.b.btnDelete.setOnClickListener { onDelete(item) }
    }
}