package com.kodego.diangca.ebrahim.myslambook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.kodego.diangca.ebrahim.myslambook.model.SlamBook

class FormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Check what action MenuActivity wants to do
        val navigateTo = intent.getStringExtra("NAVIGATE_TO")
        val slamBookToView = intent.getParcelableExtra<SlamBook>("slamBooK")

        when (navigateTo) {
            "VIEW_MEMORIES" -> {
                if (slamBookToView != null) {
                    val bundle = Bundle().apply {
                        putParcelable("slamBooK", slamBookToView)
                    }
                    // Go directly to ViewMemoriesFragment
                    navController.navigate(R.id.viewMemoriesFragment, bundle)
                } else {
                    finish() // No data — close the activity
                }
            }

            "CREATE_MEMORIES" -> {
                // Start from the first form page
                navController.navigate(R.id.formPageOneFragment)
            }

            else -> {
                // Safety fallback — start form page one
                navController.navigate(R.id.formPageOneFragment)
            }
        }
    }
}
