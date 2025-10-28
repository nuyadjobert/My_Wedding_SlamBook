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
        with(binding) {
            slamBook.whatLoveMeansToThem = whatLoveMeansToThem.text.toString()
            slamBook.wishesForTheirMarriage = wishesForTheirMarriage.text.toString()
            slamBook.favoriteMemory = favoriteMemory.text.toString()
            slamBook.favoriteThingAboutCouple = favoriteThingAboutCouple.text.toString()
            slamBook.marriageAdvice = marriageAdvice.text.toString()
            slamBook.coupleRating = ratingBar.rating.toInt()
        }

        Toast.makeText(requireContext(), "Submission Complete!", Toast.LENGTH_SHORT).show()

        // ⭐ Create an intent to go back to MenuActivity
        val intent = Intent(requireContext(), MenuActivity::class.java).apply {
            putExtra("slamBooK", slamBook) // send the completed SlamBook
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }

        startActivity(intent) // go back to menu
        requireActivity().finish() // close the form activity
    }



    private fun btnBackOnClickListener() {
        var bundle = Bundle()
        bundle.putParcelable("slamBooK", slamBook)

        // Navigate back to FormPageTwoFragment within the NavHost
        findNavController().navigate(R.id.action_formPageThreeFragment_to_formPageTwoFragment, bundle)
    }


}
