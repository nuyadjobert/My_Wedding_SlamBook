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

            // ⭐ NEW: RESTORE BIRTHDATE FIELDS (for when navigating back to this page) ⭐
            if (slamBook.birthMonth.isNotBlank()) {
                val months = resources.getStringArray(R.array.monthName)
                dateMonth.setSelection(months.indexOf(slamBook.birthMonth))
            }
            if (slamBook.birthDay.isNotBlank()) {
                val days = resources.getStringArray(R.array.monthDay)
                dateDay.setSelection(days.indexOf(slamBook.birthDay))
            }
            if (slamBook.birthYear.isNotBlank()) {
                val years = resources.getStringArray(R.array.year)
                dateYear.setSelection(years.indexOf(slamBook.birthYear))
            }
        }
    }

    private fun btnNextOnClickListener() {
        // Save data to slamBook object
        slamBook.firstName = binding.firstName.text.toString()
        slamBook.lastName = binding.lastName.text.toString()
        slamBook.nickName = binding.nickName.text.toString()
        slamBook.howIKnowTheCouple = binding.Relationship.selectedItem.toString()
        slamBook.email = binding.emailAdd.text.toString()

        // ⭐ THE FIX: SAVE BIRTHDATE DATA FROM SPINNERS ⭐
        slamBook.birthMonth = binding.dateMonth.selectedItem.toString()
        slamBook.birthDay = binding.dateDay.selectedItem.toString()
        slamBook.birthYear = binding.dateYear.selectedItem.toString()

        // Save Guest Type
        slamBook.guestType = binding.guestType.selectedItem.toString()

        val phoneNumber = binding.contactNo.text.toString().trim()
        // Save contact number with country code prefix
        slamBook.contactNo = phoneNumber.replace(selectedCountryCode, "").trim()

        slamBook.address = binding.address.text.toString()

        // Log the data to ensure it was saved correctly
        slamBook.printLog()

        // Navigate to Page 2
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
