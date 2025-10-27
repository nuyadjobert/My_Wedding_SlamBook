package com.kodego.diangca.ebrahim.myslambook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kodego.diangca.ebrahim.myslambook.databinding.FragmentFormPageOneBinding
import com.kodego.diangca.ebrahim.myslambook.databinding.FragmentFormPageThreeBinding
import com.kodego.diangca.ebrahim.myslambook.databinding.FragmentFormPageTwoBinding
import com.kodego.diangca.ebrahim.myslambook.model.SlamBook


class FormPageThreeFragment() : Fragment() {
    private lateinit var slamBook: SlamBook

    private lateinit var binding: FragmentFormPageThreeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments!=null) {
            slamBook = ((arguments?.getParcelable("slamBooK") as SlamBook?)!!)
            slamBook.printLog()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFormPageThreeBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponents()
    }


    private fun initComponents() {
        with(binding) {
            btnBack.setOnClickListener {
                btnBackOnClickListener()
            }
            btnSave.setOnClickListener {
                btnSaveOnClickListener()
            }
        }
    }

    private fun btnSaveOnClickListener() {
        // --- 1. COLLECT DATA (Ensure this section is complete before navigation) ---
        // You should have already ensured all page 3 fields are saved to 'slamBook' here.

        // Example (RE-INCLUDE YOUR DATA COLLECTION HERE, IF NOT ALREADY DONE):
        /*
        with(binding) {
            slamBook.whatLoveMeansToThem = whatLoveMeansToThem.text.toString()
            // ... collect all other fields ...
            slamBook.rating = ratingBar.rating.toInt()
        }
        */

        // 2. Show feedback
        Toast.makeText(requireContext(), "Submitting entry...", Toast.LENGTH_SHORT).show()

        // 3. Prepare the Bundle
        val bundle = Bundle().apply {
            // Pass the fully updated SlamBook object
            putParcelable("slamBooK", slamBook)
        }

        // 4. Navigate to ViewMemoriesFragment and display the details
        // Note: We use the existing action defined in nav_graph.xml
        findNavController().navigate(
            R.id.action_FormPageThreeFragment_to_viewMemoriesFragment,
            bundle
        )

        // DO NOT use requireActivity().finish() here, as you want the host activity to remain.
        // The user can now use the back button to return to the menu/main screen if needed.
    }



    private fun btnBackOnClickListener() {
        var bundle = Bundle()
        bundle.putParcelable("slamBooK", slamBook)

        findNavController().navigate(R.id.action_formPageThreeFragment_to_formPageTwoFragment, bundle)
    }


}