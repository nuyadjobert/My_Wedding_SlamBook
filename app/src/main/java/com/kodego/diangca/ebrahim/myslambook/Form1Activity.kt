package com.kodego.diangca.ebrahim.myslambook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.kodego.diangca.ebrahim.myslambook.databinding.ActivityForm1Binding
import com.kodego.diangca.ebrahim.myslambook.model.SlamBook

class Form1Activity : AppCompatActivity() {

    private lateinit var binding: ActivityForm1Binding
    private lateinit var slamBook: SlamBook

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForm1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent != null) {
            slamBook = ((intent.extras?.getSerializable("slamBooK") as SlamBook?)!!)
        } else {
            slamBook = SlamBook()
        }

        binding.btnBack.setOnClickListener {
            btnBackOnClickListener()
        }
        binding.btnNext.setOnClickListener {
            btnNextOnClickListener()
        }
    }

    private fun btnNextOnClickListener() {
        if (binding.firstName.text.isNullOrEmpty() ||
            binding.lastName.text.isNullOrEmpty() ||
            binding.nickName.text.isNullOrEmpty() ||
            binding.Relationship.selectedItemPosition == 0 || // Using your existing Relationship spinner
            binding.emailAdd.text.isNullOrEmpty() ||
            binding.contactNo.text.isNullOrEmpty() ||
            binding.address.text.isNullOrEmpty()
        ) {
            // Show individual field errors
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
        slamBook.howIKnowTheCouple = binding.Relationship.selectedItem.toString() // Using Relationship spinner
        slamBook.email = binding.emailAdd.text.toString()
        slamBook.contactNo = binding.contactNo.text.toString()
        slamBook.address = binding.address.text.toString()

        Log.d(
            "WEDDING_FORM_1",
            "Guest: ${slamBook.firstName} ${slamBook.lastName} - Relationship: ${slamBook.howIKnowTheCouple}"
        )

        val nextForm = Intent(this, Form2Activity::class.java)
        nextForm.putExtra("slamBooK", slamBook)
        startActivity(nextForm)
        finish()
    }

    private fun btnBackOnClickListener() {
        startActivity(Intent(this, MenuActivity::class.java))
        finish()
    }
}