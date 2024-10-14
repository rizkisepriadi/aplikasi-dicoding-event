package com.example.dicodingevent.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.ItemEventBinding

class EventAdapter : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    private val events: MutableList<ListEventsItem> = mutableListOf()

    inner class EventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.tvName.text = event.name
            Glide.with(binding.root.context).load(event.imageLogo).into(binding.ivThumbnail)
            binding.tvLocation.text = event.cityName
            binding.tvOwner.text = event.ownerName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    fun setEvents(newEvents: List<ListEventsItem?>?) {
        events.clear()
        newEvents?.let {
            it.filterNotNull().let { filteredEvents ->
                events.addAll(filteredEvents)
            }
        }
        notifyDataSetChanged()
    }

}
