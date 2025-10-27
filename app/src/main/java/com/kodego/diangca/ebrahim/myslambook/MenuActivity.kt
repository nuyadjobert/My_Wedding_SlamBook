package com.kodego.diangca.ebrahim.myslambook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.kodego.diangca.ebrahim.myslambook.databinding.ActivityMenuBinding
import com.kodego.diangca.ebrahim.myslambook.model.SlamBook

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private var slamBook = SlamBook()
    // ⭐ IMPORTANT: This list must be populated with saved entries (e.g., from a database)
    private var slamBooks:ArrayList<SlamBook> = ArrayList()


    private val launchRegister = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data

        Log.d("FROM REGISTER", data!!.getStringExtra("username").toString())
        Snackbar.make(
            binding.root,
            "Hi  ${data!!.getStringExtra("firstname")}! \n Please wait for the confirmation of your Account",
            Snackbar.LENGTH_LONG
        ).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCreate.setOnClickListener {
            btnCreateOnClickListener()
        }

        // ⭐ NEW CODE: Make the VIEW MEMORIES button clickable ⭐
        binding.btnView.setOnClickListener {
            btnViewOnClickListener()
        }

        // ⭐ Placeholder: Populate slamBooks here before it's used ⭐
        // Example: loadSampleData()
    }

    private fun btnCreateOnClickListener() {
        var nextForm = Intent(this, FormActivity::class.java)
        nextForm.putExtra("slamBooK", slamBook) // Pass one empty book for creation
        startActivity(nextForm)
        finish()
    }

    // ⭐ NEW FUNCTION: Handles the VIEW MEMORIES button click ⭐
    private fun btnViewOnClickListener() {
        var viewMemoriesIntent = Intent(this, FormActivity::class.java)

        // Pass the entire list of saved SlamBooks to FormActivity
        // Key: "slamBooksList"
        viewMemoriesIntent.putParcelableArrayListExtra("slamBooksList", slamBooks)

        startActivity(viewMemoriesIntent)
        // Do NOT call finish() here, as the user should be able to press Back
    }
}