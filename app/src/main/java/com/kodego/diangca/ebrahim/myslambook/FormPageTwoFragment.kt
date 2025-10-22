package com.kodego.diangca.ebrahim.myslambook

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.kodego.diangca.ebrahim.myslambook.adapter.AdapterHobbies
import com.kodego.diangca.ebrahim.myslambook.adapter.AdapterMovie
import com.kodego.diangca.ebrahim.myslambook.adapter.AdapterSkill
import com.kodego.diangca.ebrahim.myslambook.adapter.AdapterSong
import com.kodego.diangca.ebrahim.myslambook.databinding.FragmentFormPageTwoBinding
import com.kodego.diangca.ebrahim.myslambook.model.*

class FormPageTwoFragment() : Fragment() {

    private lateinit var binding: FragmentFormPageTwoBinding
    private lateinit var slamBook: SlamBook
    private lateinit var adapterReceptionParty: AdapterSong
    private var receptionParties: ArrayList<Song> = ArrayList()

    private lateinit var adapterFavoriteMemory: AdapterMovie
    private var favoriteMemories: ArrayList<Movie> = ArrayList()

    private lateinit var adapterMarriageAdvice: AdapterHobbies
    private var marriageAdvices: ArrayList<Hobbies> = ArrayList()

    private lateinit var adapterColorTheme: AdapterSkill
    private var colorThemes: ArrayList<Skill> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            slamBook = ((arguments?.getParcelable("slamBooK") as SlamBook?)!!)
        }
    }

    override fun onResume() {
        super.onResume()
        if (arguments != null) {
            slamBook = ((arguments?.getParcelable("slamBooK") as SlamBook?)!!)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFormPageTwoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        with(binding) {
            // Reception Party Adapter
            adapterReceptionParty = AdapterSong(root.context, receptionParties)
            binding.receptionPartyList.layoutManager = LinearLayoutManager(root.context)
            binding.receptionPartyList.adapter = adapterReceptionParty

            // Favorite Memory Adapter
            adapterFavoriteMemory = AdapterMovie(root.context, favoriteMemories)
            binding.favoriteMemoryList.layoutManager = LinearLayoutManager(root.context)
            binding.favoriteMemoryList.adapter = adapterFavoriteMemory

            // Marriage Advice Adapter
            adapterMarriageAdvice = AdapterHobbies(root.context, marriageAdvices)
            binding.marriageAdviceList.layoutManager = LinearLayoutManager(root.context)
            binding.marriageAdviceList.adapter = adapterMarriageAdvice

            // Color Theme Adapter
            adapterColorTheme = AdapterSkill(root.context, colorThemes)
            binding.colorThemeList.layoutManager = LinearLayoutManager(root.context)
            binding.colorThemeList.adapter = adapterColorTheme

            // Button Click Listeners
            btnAddReceptionParty.setOnClickListener {
                btnAddOnClickListener(binding.root, "ReceptionParty", binding.receptionParty, binding.receptionPartyList)
            }

            btnAddFavoriteMemory.setOnClickListener {
                btnAddOnClickListener(binding.root, "FavoriteMemory", binding.favoriteMemory, binding.favoriteMemoryList)
            }

            btnAddMarriageAdvice.setOnClickListener {
                btnAddOnClickListener(binding.root, "MarriageAdvice", binding.marriageAdvice, binding.marriageAdviceList)
            }

            btnAddColorTheme.setOnClickListener {
                btnAddOnClickListener(binding.root, "ColorTheme", binding.colorTheme, binding.colorThemeList)
            }

            btnBack.setOnClickListener {
                btnBackOnClickListener()
            }
            btnNext.setOnClickListener {
                btnNextOnClickListener()
            }
        }

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
        val text = field.text.toString()

        if (text.isEmpty()) {
            Snackbar.make(binding.root, "Please check empty fields.", Snackbar.LENGTH_SHORT).show()
            return
        }

        when (type) {
            "ReceptionParty" -> {
                receptionParties.add(Song(text))
            }
            "FavoriteMemory" -> {
                favoriteMemories.add(Movie(text))
            }
            "MarriageAdvice" -> {
                marriageAdvices.add(Hobbies(text))
            }
            "ColorTheme" -> {
                if (binding.colorThemeRate.selectedItemPosition.toInt() == 0) {
                    Snackbar.make(binding.root, "Please select rate first.", Snackbar.LENGTH_SHORT).show()
                    return
                }
                colorThemes.add(Skill(text, binding.colorThemeRate.selectedItemPosition))
            }
        }

        Snackbar.make(binding.root, "Data has been successfully added.", Snackbar.LENGTH_SHORT).show()

        field.setText("")
        recyclerView.adapter!!.notifyDataSetChanged()

        // Hide keyboard
        if (view != null) {
            val inputMethodManager =
                binding.root.context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            recyclerView.requestFocus()
        }
    }

    private fun btnNextOnClickListener() {
        // Check if we have at least some data
        if (receptionParties.isEmpty() && favoriteMemories.isEmpty() &&
            marriageAdvices.isEmpty() && colorThemes.isEmpty()) {
            Snackbar.make(binding.root, "Please add at least one item before proceeding", Snackbar.LENGTH_SHORT).show()
            return
        }

        // SAVE THE DATA TO SLAMBOOK OBJECT BEFORE NAVIGATING
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
        // Map FormPageTwo data to SlamBook fields

        // "Reception Party Ideas" -> Store as wedding wishes
        if (receptionParties.isNotEmpty()) {
            slamBook.wishesForTheirMarriage = receptionParties.joinToString("\n") { it.title }
        }

        // "Favorite Memory of the Couple" -> This maps directly to favoriteMemory
        if (favoriteMemories.isNotEmpty()) {
            slamBook.favoriteMemory = favoriteMemories.joinToString("\n") { it.title }
        }

        // "Best Marriage Advice" -> This maps directly to marriageAdvice
        if (marriageAdvices.isNotEmpty()) {
            slamBook.marriageAdvice = marriageAdvices.joinToString("\n") { it.advice }
        }

        // "Color Theme Ideas" -> Store as favorite thing about the couple
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
}