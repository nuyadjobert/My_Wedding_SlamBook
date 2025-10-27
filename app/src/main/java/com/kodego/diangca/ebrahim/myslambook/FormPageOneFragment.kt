package com.kodego.diangca.ebrahim.myslambook

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kodego.diangca.ebrahim.myslambook.databinding.FragmentFormPageOneBinding
import com.kodego.diangca.ebrahim.myslambook.model.SlamBook

class FormPageOneFragment : Fragment() {

    private lateinit var binding: FragmentFormPageOneBinding
    private lateinit var slamBook: SlamBook
    private var selectedCountryCode: String = "+63"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        Log.d("ON_RESUME", "RESUME_PAGE_ONE")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("ON_ATTACH", "ATTACH_PAGE_ONE")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFormPageOneBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        if (arguments != null) {
            slamBook = ((arguments?.getParcelable("slamBooK") as SlamBook?)!!)
            slamBook.printLog()
            restoreField()
        } else {
            slamBook = SlamBook()
        }
        setupCountryCodeSpinner()

        with(binding) {
            btnBack.setOnClickListener {
                btnBackOnClickListener()
            }
            btnNext.setOnClickListener {
                btnNextOnClickListener()
            }

            // Update prompts for wedding context
            Relationship.prompt = "How do you know the couple?"
        }
    }

    private fun setupCountryCodeSpinner() {
        binding.countryCodeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCountryCode = when (position) {
                    0 -> "+63"
                    1 -> "+1"
                    2 -> "+44"
                    else -> "+63"
                }

                binding.contactNo.hint = "Phone Number ($selectedCountryCode)"

                val currentText = binding.contactNo.text?.toString() ?: ""
                val cleanNumber = currentText.replace(Regex("^\\+[0-9]+\\s?"), "")
                binding.contactNo.setText("$selectedCountryCode $cleanNumber")
                binding.contactNo.setSelection(binding.contactNo.text!!.length)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedCountryCode = "+63"
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun restoreField() {
        binding.apply {
            firstName.setText(slamBook.firstName)
            lastName.setText(slamBook.lastName)
            nickName.setText(slamBook.nickName)
            emailAdd.setText(slamBook.email)
            contactNo.setText(slamBook.contactNo)
            address.setText(slamBook.address)

            // Restore relationship to couple
            if (slamBook.howIKnowTheCouple.isNotBlank()) {
                val relationships = resources.getStringArray(R.array.relationToCouple)
                val position = relationships.indexOf(slamBook.howIKnowTheCouple)
                if (position >= 0) {
                    Relationship.setSelection(position)
                }
            }
        }
    }

    private fun btnNextOnClickListener() {
        if (binding.firstName.text.isNullOrEmpty() ||
            binding.lastName.text.isNullOrEmpty() ||
            binding.nickName.text.isNullOrEmpty() ||
            binding.Relationship.selectedItemPosition == 0 ||
            binding.emailAdd.text.isNullOrEmpty() ||
            binding.contactNo.text.isNullOrEmpty() ||
            binding.address.text.isNullOrEmpty()
        ) {
            // Show validation errors
            if (binding.firstName.text.isNullOrEmpty()) {
                binding.firstName.error = "Please enter your first name"
            }
            if (binding.lastName.text.isNullOrEmpty()) {
                binding.lastName.error = "Please enter your last name"
            }
            if (binding.nickName.text.isNullOrEmpty()) {
                binding.nickName.error = "Please enter your nickname"
            }
            if (binding.Relationship.selectedItemPosition == 0) {
                Snackbar.make(binding.root, "Please select your relationship to the couple", Snackbar.LENGTH_SHORT).show()
                return
            }
            if (binding.emailAdd.text.isNullOrEmpty()) {
                binding.emailAdd.error = "Please enter your email"
            }
            if (binding.contactNo.text.isNullOrEmpty()) {
                binding.contactNo.error = "Please enter your contact number"
            }
            if (binding.address.text.isNullOrEmpty()) {
                binding.address.error = "Please enter your address"
            }

            Snackbar.make(binding.root, "Please check empty fields", Snackbar.LENGTH_SHORT).show()
            return
        }

        // Save data to slamBook object
        slamBook.firstName = binding.firstName.text.toString()
        slamBook.lastName = binding.lastName.text.toString()
        slamBook.nickName = binding.nickName.text.toString()
        slamBook.howIKnowTheCouple = binding.Relationship.selectedItem.toString()
        slamBook.email = binding.emailAdd.text.toString()

        val phoneNumber = binding.contactNo.text.toString().trim()
        slamBook.contactNo = "$selectedCountryCode $phoneNumber"

        slamBook.address = binding.address.text.toString()

        Log.d(
            "WEDDING_FORM_1",
            "Guest: ${slamBook.firstName} ${slamBook.lastName} - Relationship: ${slamBook.howIKnowTheCouple}"
        )

        val bundle = Bundle()
        bundle.putParcelable("slamBooK", slamBook)

        findNavController().navigate(R.id.action_formPageOneFragment_to_formPageTwoFragment, bundle)
    }

    private fun btnBackOnClickListener() {
        val intent = Intent(requireContext(), MenuActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

}