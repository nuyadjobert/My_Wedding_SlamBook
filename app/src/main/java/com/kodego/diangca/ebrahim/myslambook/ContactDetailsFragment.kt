package com.kodego.diangca.ebrahim.myslambook

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kodego.diangca.ebrahim.myslambook.databinding.FragmentContactDetailsBinding
import com.kodego.diangca.ebrahim.myslambook.model.SlamBook

class ContactDetailsFragment : Fragment() {

    private lateinit var binding: FragmentContactDetailsBinding
    private lateinit var slamBook: SlamBook

    // 1. Get the data (SlamBook object) passed from ViewMemoriesFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ⭐ UPDATED ONCREATE METHOD ⭐

        // Check arguments and retrieve the SlamBook object safely
        val loadedSlamBook = arguments?.let {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable("slamBooK", SlamBook::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getParcelable<SlamBook>("slamBooK")
            }
        }

        // Initialize slamBook, and if it's null, handle the failure (pop back)
        if (loadedSlamBook != null) {
            slamBook = loadedSlamBook
        } else {
            Log.e("CONTACT_DETAILS", "SlamBook argument is NULL. Crashing fragment.")
            // This is the clean way to handle the crash: pop the failed fragment off the stack
            findNavController().popBackStack()
        }
    }

    // 2. Inflate the new contact details layout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // NOTE: The binding class name will be generated based on the XML file name
        binding = FragmentContactDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    // 3. Bind the data to the TextViews
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Only call displayContactDetails if slamBook was successfully initialized
        if (::slamBook.isInitialized) {
            displayContactDetails()
        }
    }

    private fun displayContactDetails() {
        // This function now correctly binds the fields that exist in its own layout
        with(binding) {
            guestRelationship.text = slamBook.howIKnowTheCouple
            guestEmail.text = slamBook.email
            guestContact.text = slamBook.contactNo
            guestAddress.text = slamBook.address
        }
    }
}