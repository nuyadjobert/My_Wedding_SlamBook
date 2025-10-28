package com.kodego.diangca.ebrahim.myslambook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.kodego.diangca.ebrahim.myslambook.databinding.ActivityMenuBinding
import com.kodego.diangca.ebrahim.myslambook.model.SlamBook

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private var slamBook = SlamBook() // For creating a new form
    private var slamBooks: ArrayList<SlamBook> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if a new SlamBook was passed from FormPageThreeFragment
        val newSlamBook = intent.getParcelableExtra<SlamBook>("slamBooK")
        if (newSlamBook != null) {
            // Add or update the slamBooks list
            slamBooks.clear()
            slamBooks.add(newSlamBook)
        } else if (slamBooks.isEmpty()) {
            // Fallback sample data
            slamBooks.add(SlamBook.createCompletedSample())
        }

        binding.btnCreate.setOnClickListener { openCreateMemories() }
        binding.btnView.setOnClickListener { openViewMemories() }
    }


    // ✅ 1. When "Create Memories" is clicked → Open FormActivity → Start at Page 1
    private fun openCreateMemories() {
        val intent = Intent(this, FormActivity::class.java)
        intent.putExtra("NAVIGATE_TO", "CREATE_MEMORIES")
        intent.putExtra("slamBooK", SlamBook()) // empty new one
        startActivity(intent)
    }

    // ✅ 2. When "View Memories" is clicked → Open FormActivity → Directly show ViewMemoriesFragment
    private fun openViewMemories() {
        if (slamBooks.isEmpty()) {
            Snackbar.make(
                binding.root,
                "No memories saved yet! Try creating one first.",
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        val intent = Intent(this, FormActivity::class.java)
        intent.putExtra("NAVIGATE_TO", "VIEW_MEMORIES")
        intent.putExtra("slamBooK", slamBooks[0])
        startActivity(intent)
    }
}
