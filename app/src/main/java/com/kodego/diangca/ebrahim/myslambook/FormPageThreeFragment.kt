package com.kodego.diangca.ebrahim.myslambook

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kodego.diangca.ebrahim.myslambook.databinding.FragmentFormPageThreeBinding
import com.kodego.diangca.ebrahim.myslambook.model.SlamBook

class FormPageThreeFragment : Fragment() {

    private lateinit var binding: FragmentFormPageThreeBinding
    private lateinit var slamBook: SlamBook

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            slamBook = it.getParcelable("slamBooK") ?: SlamBook()
            slamBook.printLog()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFormPageThreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        with(binding) {
            btnBack.setOnClickListener { btnBackOnClickListener() }
            btnSave.setOnClickListener { btnSaveOnClickListener() }
        }
    }

    private fun btnSaveOnClickListener() {
        with(binding) {
            // Get all field values
            val whatLoveMeans = whatLoveMeansToThem.text.toString().trim()
            val wishes = wishesForTheirMarriage.text.toString().trim()
            val memory = favoriteMemory.text.toString().trim()
            val favoriteThing = favoriteThingAboutCouple.text.toString().trim()
            val advice = marriageAdvice.text.toString().trim()
            val rating = ratingBar.rating.toInt()

            //  VALIDATION SECTION
            when {
                whatLoveMeans.isEmpty() -> {
                    showError("Please share what love means to them.")
                    return
                }
                wishes.isEmpty() -> {
                    showError("Please write a wish for their marriage.")
                    return
                }
                memory.isEmpty() -> {
                    showError("Please describe your favorite memory of the couple.")
                    return
                }
                favoriteThing.isEmpty() -> {
                    showError("Please share your favorite thing about the couple.")
                    return
                }
                advice.isEmpty() -> {
                    showError("Please give them your best marriage advice.")
                    return
                }
                rating == 0 -> {
                    showError("Please rate the couple before submitting.")
                    return
                }
            }

            // SAVE VALIDATED DATA INTO slamBook OBJECT
            slamBook.apply {
                whatLoveMeansToThem = whatLoveMeans
                wishesForTheirMarriage = wishes
                favoriteMemory = memory
                favoriteThingAboutCouple = favoriteThing
                marriageAdvice = advice
                coupleRating = rating
            }


            Toast.makeText(requireContext(), "Submission Complete!", Toast.LENGTH_SHORT).show()

            //  Go back to MenuActivity
            val intent = Intent(requireContext(), MenuActivity::class.java).apply {
                putExtra("slamBooK", slamBook)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }

            startActivity(intent)
            requireActivity().finish()
        }
    }

    /**
     *  Navigate back to previous page (FormPageTwoFragment)
     */
    private fun btnBackOnClickListener() {
        val bundle = Bundle()
        bundle.putParcelable("slamBooK", slamBook)
        findNavController().navigate(
            R.id.action_formPageThreeFragment_to_formPageTwoFragment,
            bundle
        )
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}
