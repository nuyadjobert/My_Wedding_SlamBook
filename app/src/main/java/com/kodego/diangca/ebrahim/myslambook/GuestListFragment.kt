package com.kodego.diangca.ebrahim.myslambook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kodego.diangca.ebrahim.myslambook.databinding.FragmentGuestListBinding
import com.kodego.diangca.ebrahim.myslambook.model.SlamBook
import com.kodego.diangca.ebrahim.myslambook.adapter.GuestListAdapter // ⭐ You will create this file below ⭐

class GuestListFragment : Fragment() {

    private lateinit var binding: FragmentGuestListBinding
    private lateinit var adapter: GuestListAdapter

    // ⭐ Placeholder: This list should be loaded from your database/repository
    private val guestList = mutableListOf<SlamBook>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Assuming your XML is named fragment_guest_list.xml
        binding = FragmentGuestListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Load Data (Replace with your database logic)
        loadSampleData()

        // 2. Setup the RecyclerView
        setupRecyclerView()
    }

    // ⭐ Key Function: Handles the click on a guest item (passed to the adapter) ⭐
    private fun onGuestItemClicked(selectedSlamBook: SlamBook) {
        val bundle = Bundle().apply {
            // CRITICAL: Key must match argument in ViewMemoriesFragment's nav_graph
            putParcelable("slamBooK", selectedSlamBook)
        }

        // Navigate to the detail view
        findNavController().navigate(
            R.id.action_guestListFragment_to_viewMemoriesFragment, // Action ID from nav_graph
            bundle
        )
    }

    private fun setupRecyclerView() {
        // Initialize the adapter and pass the click handler function
        adapter = GuestListAdapter(guestList, ::onGuestItemClicked)

        binding.recyclerViewGuests.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@GuestListFragment.adapter
        }
    }

    private fun loadSampleData() {
        // ⭐ Replace this with actual database loading ⭐
        // Example:
        guestList.add(SlamBook().apply {
            firstName = "Alex"
            lastName = "Smith"
            howIKnowTheCouple = "College Friend"
            coupleRating = 5
        })
        guestList.add(SlamBook().apply {
            firstName = "Jordan"
            lastName = "Lee"
            howIKnowTheCouple = "Cousin"
            coupleRating = 4
        })
        // Notify the adapter that data has changed (if using real data)
        // adapter.notifyDataSetChanged()
    }
}