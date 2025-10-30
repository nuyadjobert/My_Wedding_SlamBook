package com.kodego.diangca.ebrahim.myslambook

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.kodego.diangca.ebrahim.myslambook.adapter.*
import com.kodego.diangca.ebrahim.myslambook.databinding.FragmentFormPageTwoBinding
import com.kodego.diangca.ebrahim.myslambook.model.*

class FormPageTwoFragment : Fragment() {

    private lateinit var binding: FragmentFormPageTwoBinding
    private lateinit var slamBook: SlamBook

    private lateinit var adapterReceptionParty: AdapterSong
    private lateinit var adapterFavoriteMemory: AdapterMovie
    private lateinit var adapterMarriageAdvice: AdapterHobbies
    private lateinit var adapterColorTheme: AdapterSkill

    private var receptionParties = ArrayList<Song>()
    private var favoriteMemories = ArrayList<Movie>()
    private var marriageAdvices = ArrayList<Hobbies>()
    private var colorThemes = ArrayList<Skill>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            slamBook = it.getParcelable("slamBooK") ?: SlamBook()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFormPageTwoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        with(binding) {
            // Initialize adapters
            adapterReceptionParty = AdapterSong(requireContext(), receptionParties)
            adapterFavoriteMemory = AdapterMovie(requireContext(), favoriteMemories)
            adapterMarriageAdvice = AdapterHobbies(requireContext(), marriageAdvices)
            adapterColorTheme = AdapterSkill(requireContext(), colorThemes)

            // Attach adapters to RecyclerViews
            receptionPartyList.layoutManager = LinearLayoutManager(requireContext())
            favoriteMemoryList.layoutManager = LinearLayoutManager(requireContext())
            marriageAdviceList.layoutManager = LinearLayoutManager(requireContext())
            colorThemeList.layoutManager = LinearLayoutManager(requireContext())

            receptionPartyList.adapter = adapterReceptionParty
            favoriteMemoryList.adapter = adapterFavoriteMemory
            marriageAdviceList.adapter = adapterMarriageAdvice
            colorThemeList.adapter = adapterColorTheme

            // Add button listeners
            btnAddReceptionParty.setOnClickListener {
                btnAddOnClickListener(it, "ReceptionParty", receptionParty, receptionPartyList)
            }

            btnAddFavoriteMemory.setOnClickListener {
                btnAddOnClickListener(it, "FavoriteMemory", favoriteMemory, favoriteMemoryList)
            }

            btnAddMarriageAdvice.setOnClickListener {
                btnAddOnClickListener(it, "MarriageAdvice", marriageAdvice, marriageAdviceList)
            }

            btnAddColorTheme.setOnClickListener {
                btnAddOnClickListener(it, "ColorTheme", colorTheme, colorThemeList)
            }

            btnBack.setOnClickListener { btnBackOnClickListener() }
            btnNext.setOnClickListener { btnNextOnClickListener() }
        }

        // Handle back press
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val bundle = Bundle()
                bundle.putParcelable("slamBooK", slamBook)
                findNavController().navigate(R.id.action_formPageTwoFragment_to_formPageOneFragment, bundle)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun btnAddOnClickListener(
        view: View?,
        type: String,
        field: TextInputEditText,
        recyclerView: RecyclerView
    ) {
        val text = field.text.toString().trim()

        if (text.isEmpty()) {
            Snackbar.make(binding.root, "Please fill in the field before adding.", Snackbar.LENGTH_SHORT).show()
            return
        }

        //  Duplicate check per category
        val alreadyExists = when (type) {
            "ReceptionParty" -> receptionParties.any { it.title.equals(text, ignoreCase = true) }
            "FavoriteMemory" -> favoriteMemories.any { it.title.equals(text, ignoreCase = true) }
            "MarriageAdvice" -> marriageAdvices.any { it.advice.equals(text, ignoreCase = true) }
            "ColorTheme" -> {
                val ratePosition = binding.colorThemeRate.selectedItemPosition
                colorThemes.any {
                    it.colorName.equals(text, ignoreCase = true) && it.rating == ratePosition
                }
            }
            else -> false
        }

        if (alreadyExists) {
            Snackbar.make(binding.root, "This entry already exists.", Snackbar.LENGTH_SHORT).show()
            return
        }

        //  Continue adding only if it’s new
        when (type) {
            "ReceptionParty" -> receptionParties.add(Song(text))
            "FavoriteMemory" -> favoriteMemories.add(Movie(text))
            "MarriageAdvice" -> marriageAdvices.add(Hobbies(text))
            "ColorTheme" -> {
                val ratePosition = binding.colorThemeRate.selectedItemPosition
                if (ratePosition == 0) {
                    Snackbar.make(binding.root, "Please select a rate before adding a color theme.", Snackbar.LENGTH_SHORT).show()
                    return
                }
                colorThemes.add(Skill(text, ratePosition))
            }
        }

        Snackbar.make(binding.root, "Added successfully.", Snackbar.LENGTH_SHORT).show()
        field.setText("")
        recyclerView.adapter?.notifyDataSetChanged()

        // Hide keyboard
        hideKeyboard(view)
        recyclerView.requestFocus()
    }


    private fun btnNextOnClickListener() {
        if (receptionParties.isEmpty() && favoriteMemories.isEmpty() &&
            marriageAdvices.isEmpty() && colorThemes.isEmpty()) {

            Snackbar.make(binding.root, "Please add at least one entry before proceeding.", Snackbar.LENGTH_LONG).show()
            return
        }

        saveFormData()

        val bundle = Bundle()
        bundle.putParcelable("slamBooK", slamBook)

        try {
            findNavController().navigate(R.id.action_formPageTwoFragment_to_formPageThreeFragment, bundle)
        } catch (e: Exception) {
            Snackbar.make(binding.root, "Navigation failed: ${e.message}", Snackbar.LENGTH_LONG).show()
        }
    }


    private fun saveFormData() {
        if (receptionParties.isNotEmpty()) {
            slamBook.wishesForTheirMarriage = receptionParties.joinToString("\n") { it.title }
        }
        if (favoriteMemories.isNotEmpty()) {
            slamBook.favoriteMemory = favoriteMemories.joinToString("\n") { it.title }
        }
        if (marriageAdvices.isNotEmpty()) {
            slamBook.marriageAdvice = marriageAdvices.joinToString("\n") { it.advice }
        }
        if (colorThemes.isNotEmpty()) {
            slamBook.favoriteThingAboutCouple = colorThemes.joinToString("\n") {
                "${it.colorName} (Rating: ${it.rating}/5)"
            }
        }
    }

    private fun btnBackOnClickListener() {
        val bundle = Bundle()
        bundle.putParcelable("slamBooK", slamBook)
        findNavController().navigate(R.id.action_formPageTwoFragment_to_formPageOneFragment, bundle)
    }


    private fun hideKeyboard(view: View?) {
        view?.let {
            val imm = requireContext().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}
