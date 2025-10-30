package com.kodego.diangca.ebrahim.myslambook

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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
            slamBook = (arguments?.getParcelable("slamBooK") as SlamBook?)!!
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
                if (validateInputs()) {
                    btnNextOnClickListener()
                }
            }

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

            if (slamBook.howIKnowTheCouple.isNotBlank()) {
                val relationships = resources.getStringArray(R.array.relationToCouple)
                val position = relationships.indexOf(slamBook.howIKnowTheCouple)
                if (position >= 0) Relationship.setSelection(position)
            }

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

    /**VALIDATION FUNCTION **/
    private fun validateInputs(): Boolean {
        val fName = binding.firstName.text.toString().trim()
        val lName = binding.lastName.text.toString().trim()
        val nName = binding.nickName.text.toString().trim()
        val email = binding.emailAdd.text.toString().trim()
        val contact = binding.contactNo.text.toString().trim()
        val address = binding.address.text.toString().trim()

        when {
            fName.isEmpty() -> {
                binding.firstName.error = "First name is required"
                binding.firstName.requestFocus()
                return false
            }
            lName.isEmpty() -> {
                binding.lastName.error = "Last name is required"
                binding.lastName.requestFocus()
                return false
            }
            nName.isEmpty() -> {
                binding.nickName.error = "Nickname is required"
                binding.nickName.requestFocus()
                return false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.emailAdd.error = "Enter a valid email"
                binding.emailAdd.requestFocus()
                return false
            }
            contact.isEmpty() || !contact.matches(Regex(".*\\d{7,}.*")) -> {
                binding.contactNo.error = "Enter a valid contact number"
                binding.contactNo.requestFocus()
                return false
            }
            address.isEmpty() -> {
                binding.address.error = "Address cannot be empty"
                binding.address.requestFocus()
                return false
            }
            binding.Relationship.selectedItemPosition == 0 -> {
                Snackbar.make(binding.root, "Please select how you know the couple", Snackbar.LENGTH_SHORT).show()
                return false
            }
            binding.dateMonth.selectedItemPosition == 0 ||
                    binding.dateDay.selectedItemPosition == 0 ||
                    binding.dateYear.selectedItemPosition == 0 -> {
                Snackbar.make(binding.root, "Please select your complete birthdate", Snackbar.LENGTH_SHORT).show()
                return false
            }
            binding.guestType.selectedItemPosition == 0 -> {
                Snackbar.make(binding.root, "Please select your guest type", Snackbar.LENGTH_SHORT).show()
                return false
            }
            else -> return true
        }
    }

    private fun btnNextOnClickListener() {
        slamBook.firstName = binding.firstName.text.toString()
        slamBook.lastName = binding.lastName.text.toString()
        slamBook.nickName = binding.nickName.text.toString()
        slamBook.howIKnowTheCouple = binding.Relationship.selectedItem.toString()
        slamBook.email = binding.emailAdd.text.toString()

        slamBook.birthMonth = binding.dateMonth.selectedItem.toString()
        slamBook.birthDay = binding.dateDay.selectedItem.toString()
        slamBook.birthYear = binding.dateYear.selectedItem.toString()

        slamBook.guestType = binding.guestType.selectedItem.toString()

        val phoneNumber = binding.contactNo.text.toString().trim()
        slamBook.contactNo = phoneNumber.replace(selectedCountryCode, "").trim()

        slamBook.address = binding.address.text.toString()
        slamBook.printLog()

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
