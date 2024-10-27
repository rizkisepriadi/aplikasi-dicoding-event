package com.example.dicodingevent.viewmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.example.dicodingevent.data.remote.response.ListEventsItem
import com.example.dicodingevent.databinding.ItemEventBinding

class FinishedAdapter(private val onItemClick: ((Int?) -> Unit)? = null) :
    ListAdapter<ListEventsItem, FinishedAdapter.FinishedEventViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinishedEventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FinishedEventViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: FinishedEventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FinishedEventViewHolder(
        private val binding: ItemEventBinding, private val onItemClick: ((Int?) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.tvName.text = event.name
            binding.tvOwner.text = event.ownerName
            binding.tvLocation.text = event.cityName
            Glide.with(binding.root.context).load(event.imageLogo).into(binding.ivThumbnail)

            binding.root.setOnClickListener {
                onItemClick?.invoke(event.id)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
