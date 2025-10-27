package com.kodego.diangca.ebrahim.myslambook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kodego.diangca.ebrahim.myslambook.databinding.ItemGuestSummaryBinding // Assuming your item layout is item_guest_summary.xml
import com.kodego.diangca.ebrahim.myslambook.model.SlamBook

// Define a type alias for the click handler function
typealias GuestClickListener = (SlamBook) -> Unit

class GuestListAdapter(
    private val guestList: List<SlamBook>,
    private val clickListener: GuestClickListener // Function to call when an item is clicked
) : RecyclerView.Adapter<GuestListAdapter.GuestViewHolder>() {

    inner class GuestViewHolder(private val binding: ItemGuestSummaryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Binds the data to the views in item_guest_summary.xml
        fun bind(slamBook: SlamBook) {
            binding.tvGuestName.text = slamBook.getFullName() // Assumes SlamBook has getFullName()
            binding.tvGuestRelationship.text = slamBook.howIKnowTheCouple
            binding.tvGuestRating.text = "${slamBook.coupleRating}/5"

            // Set the click listener for the entire item view
            binding.root.setOnClickListener {
                clickListener(slamBook) // Call the function passed from the Fragment
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestViewHolder {
        val binding = ItemGuestSummaryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GuestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GuestViewHolder, position: Int) {
        holder.bind(guestList[position])
    }

    override fun getItemCount() = guestList.size
}