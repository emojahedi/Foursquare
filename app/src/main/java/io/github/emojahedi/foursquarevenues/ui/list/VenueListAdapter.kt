package io.github.emojahedi.foursquarevenues.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.emojahedi.foursquarevenues.R
import io.github.emojahedi.foursquarevenues.data.source.database.VenueEntry

class VenueListAdapter(
    context: Context
) : RecyclerView.Adapter<VenueListAdapter.VenueViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mVenues: List<VenueEntry>? = emptyList()// Cached copy of venues

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueViewHolder {
        val itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false)
        return VenueViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: VenueViewHolder, position: Int) {
        val current = mVenues?.get(position)
        holder.venueItemName.text = current?.name
        holder.venueItemAddress.text = current?.formatted_address_line0
        holder
    }

    internal fun setVenues(venues: List<VenueEntry>?) {
        mVenues = venues
        notifyDataSetChanged()
    }

    override fun getItemCount() = (mVenues?.size) ?: 0


    fun getItemIdAt(position: Int): String? {
        return mVenues?.get(position)?.id
    }

    inner class VenueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val venueItemName: TextView = itemView.findViewById(R.id.list_item_name)
        val venueItemAddress: TextView = itemView.findViewById(R.id.list_item_address)
    }
}