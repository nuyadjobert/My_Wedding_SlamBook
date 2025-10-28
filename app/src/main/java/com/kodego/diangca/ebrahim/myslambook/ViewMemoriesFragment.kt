package com.kodego.diangca.ebrahim.myslambook

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kodego.diangca.ebrahim.myslambook.databinding.FragmentViewMemoriesBinding
import com.kodego.diangca.ebrahim.myslambook.model.SlamBook

class ViewMemoriesFragment : Fragment() {

    private lateinit var binding: FragmentViewMemoriesBinding
    private lateinit var slamBook: SlamBook
    private var isExpanded: Boolean = false // State to track if content is visible

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loadedSlamBook = arguments?.let {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable("slamBooK", SlamBook::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getParcelable<SlamBook>("slamBooK")
            }
        }

        if (loadedSlamBook != null) {
            slamBook = loadedSlamBook
            Log.d("VIEW_MEMORIES", "Loaded Guest: ${slamBook.firstName} ${slamBook.lastName}")
        } else {
            Log.e("VIEW_MEMORIES", "SlamBook argument is NULL. Cannot initialize data.")
            // Handle error state if necessary
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewMemoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Display only the header details immediately
        displayHeaderDetails()

        binding.btnBackToMenu.setOnClickListener {
            requireActivity().finish() // closes FormActivity and returns to MenuActivity
        }

        // 2. Set up the toggle button listener
        binding.btnToggleDetails.setOnClickListener {
            toggleContentVisibility()
        }

        // 3. Set up the contact button listener (if still needed)
        binding.btnViewContactDetails.setOnClickListener {
            val bundle= Bundle().apply {
                putParcelable("slamBook",slamBook)
            }
            findNavController().navigate(
                R.id.action_viewMemoriesFragment_to_contactDetailsFragment,
                bundle
            )
        }
    }

    private fun displayHeaderDetails() {
        // Only set the name and nickname here
        with(binding) {
            guestName.text = "${slamBook.firstName} ${slamBook.lastName}"
            guestNickname.text = slamBook.nickName
        }
    }

    private fun displayAllContentDetails() {
        // Set all the hidden content details
        with(binding) {
            guestLoveMeans.text = slamBook.whatLoveMeansToThem
            guestWishes.text = slamBook.wishesForTheirMarriage
            guestFavoriteMemory.text = slamBook.favoriteMemory
            guestFavoriteThing.text = slamBook.favoriteThingAboutCouple
            guestMarriageAdvice.text = slamBook.marriageAdvice
            // Use String interpolation for a cleaner display
            guestRating.text = "${slamBook.coupleRating}/5 Stars"
        }
    }

    private fun toggleContentVisibility() {
        isExpanded = !isExpanded // Toggle the state

        if (isExpanded) {
            // Show content
            binding.contentContainer.visibility = View.VISIBLE
            // Load all data when expanded
            displayAllContentDetails()
            // Rotate the arrow up (assuming 180 degrees is the 'up' state)
            binding.btnToggleDetails.rotation = 180f
        } else {
            // Hide content
            binding.contentContainer.visibility = View.GONE
            // Rotate the arrow down (0 degrees is the 'down' state)
            binding.btnToggleDetails.rotation = 0f
        }
    }

}
