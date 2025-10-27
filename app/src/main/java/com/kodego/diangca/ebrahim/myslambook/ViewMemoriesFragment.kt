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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ⭐ UPDATED ONCREATE METHOD ⭐

        // Check if arguments exist and try to retrieve the SlamBook object safely
        val loadedSlamBook = arguments?.let {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable("slamBooK", SlamBook::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getParcelable<SlamBook>("slamBooK")
            }
        }

        // Safely initialize slamBook if it was loaded, otherwise log an error
        if (loadedSlamBook != null) {
            slamBook = loadedSlamBook
            Log.d("VIEW_MEMORIES", "Loaded Guest: ${slamBook.firstName} ${slamBook.lastName}")
        } else {
            // Handle the error state where data is missing (e.g., if Fragment was launched incorrectly)
            Log.e("VIEW_MEMORIES", "SlamBook argument is NULL. Cannot initialize data.")
            // You might consider popping back if the data is essential:
            // findNavController().popBackStack()
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
        displayGuestDetails()

        binding.btnViewContactDetails.setOnClickListener {
            val bundle= Bundle().apply {
                putParcelable("slamBook",slamBook)
            }
            findNavController().navigate(
                R.id.action_viewMemoriesFragment_to_contactDetailsFragment,
                bundle // Pass the data bundle
            )
        }
    }

    private fun displayGuestDetails() {
        with(binding) {
            guestName.text = "${slamBook.firstName} ${slamBook.lastName}"
            guestNickname.text = slamBook.nickName
            guestLoveMeans.text = slamBook.whatLoveMeansToThem
            guestWishes.text = slamBook.wishesForTheirMarriage
            guestFavoriteMemory.text = slamBook.favoriteMemory
            guestFavoriteThing.text = slamBook.favoriteThingAboutCouple
            guestMarriageAdvice.text = slamBook.marriageAdvice
            guestRating.text = "Couple Rating: ${slamBook.coupleRating}/5"
        }

    }

}
