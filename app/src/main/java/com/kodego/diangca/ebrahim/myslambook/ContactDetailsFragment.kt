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

        val loadedSlamBook = arguments?.let {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                // Key used for passing data: "slamBook"
                it.getParcelable("slamBook", SlamBook::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getParcelable<SlamBook>("slamBook")
            }
        }

        if (loadedSlamBook != null) {
            // Data successfully loaded
            slamBook = loadedSlamBook
        } else {
            Log.e("CONTACT_DETAILS", "SlamBook argument is NULL. Cannot display details.")
            // Navigate back if data is missing, preventing a crash.
            findNavController().popBackStack()
        }
    }

    // 2. Inflate the contact details layout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    // 3. Bind the data to the TextViews
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBackToViewMemories.setOnClickListener {
            findNavController().navigateUp()
        }
        // Only call displayContactDetails if slamBook was successfully initialized
        if (::slamBook.isInitialized) {
            displayContactDetails()
        }
    }

    private fun displayContactDetails() {
        with(binding) {
            // --- IDENTITY FIELDS ---
            // 1. Nickname
            guestNickname.text = slamBook.nickName

            // 2. Full Name (using the helper function from SlamBook)
            guestFullName.text = slamBook.getFullName()

            // 3. Guest Type
            guestTypeDisplay.text = slamBook.guestType

            // 4. Relationship
            guestRelationship.text = slamBook.howIKnowTheCouple

            // 5. Birthdate (NEW: Combined from SlamBook fields)
            val birthDate = "${slamBook.birthMonth} ${slamBook.birthDay}, ${slamBook.birthYear}".trim()
            if (birthDate.isNotBlank()) {
                guestBirthdate.text = birthDate
            } else {
                guestBirthdate.text = "N/A"
            }

            // --- CONTACT FIELDS ---
            // 6. Email
            guestEmail.text = slamBook.email

            // 7. Contact Number
            guestContact.text = slamBook.contactNo

            // 8. Address
            guestAddress.text = slamBook.address
        }
    }
}
