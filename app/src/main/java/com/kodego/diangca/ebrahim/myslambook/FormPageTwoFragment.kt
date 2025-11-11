package com.kodego.diangca.ebrahim.myslambook

import android.os.Build
import android.os.Bundle
import androidx.core.os.bundleOf
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.setFragmentResult
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kodego.diangca.ebrahim.myslambook.adapter.*
import com.kodego.diangca.ebrahim.myslambook.databinding.FragmentFormPageTwoBinding
import com.kodego.diangca.ebrahim.myslambook.model.*

class FormPageTwoFragment : Fragment() {

    private var _binding: FragmentFormPageTwoBinding? = null
    private val binding get() = _binding!!

    private lateinit var slamBook: SlamBook

    private val songs = ArrayList<Song>()
    private val movies = ArrayList<Movie>()
    private val hobbiesObjects = ArrayList<Hobbies>()
    private val skillsObjects = ArrayList<Skill>()

    private val songsText = ArrayList<String>()
    private val moviesText = ArrayList<String>()
    private val hobbiesText = ArrayList<String>()
    private val skillsPairs = ArrayList<Pair<String, String>>() // (skillName, skillRate)

    private lateinit var adapterSong: AdapterSong
    private lateinit var adapterMovie: AdapterMovie
    private lateinit var adapterHobbies: AdapterHobbies
    private lateinit var adapterSkill: AdapterSkill

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Retrieve SlamBook data
        slamBook = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("slamBook", SlamBook::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable("slamBook")
        } ?: SlamBook()   // ✅ fixed typo (was SlamBooK)

        // ✅ Load data if available
        slamBook.favSongs.let { list ->
            songsText.addAll(list)
            songs.addAll(list.map { Song(it) })
        }
        slamBook.favMovies.let { list ->
            moviesText.addAll(list)
            movies.addAll(list.map { Movie(it) })
        }
        slamBook.hobbies.let { list ->
            hobbiesText.addAll(list)
            hobbiesObjects.addAll(list.map { Hobbies(it) })
        }
        slamBook.skills.let { list ->
            skillsPairs.addAll(list)
            skillsObjects.addAll(list.map { Skill(it.first, it.second.toIntOrNull() ?: 0) })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormPageTwoBinding.inflate(inflater,
            container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSkillRateSpinner()
        initRecyclerViews()
        setupButtonActions()
    }

    private fun setupSkillRateSpinner() {
        val rates = resources.getStringArray(R.array.skillRate)
        val spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, rates)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.skillRateInput.adapter = spinnerAdapter
    }

    private fun initRecyclerViews() = with(binding) {
        adapterSong = AdapterSong(requireContext(), songs)
        favSongList.layoutManager = LinearLayoutManager(requireContext())
        favSongList.adapter = adapterSong

        adapterMovie = AdapterMovie(requireContext(), movies)
        favMovieList.layoutManager = LinearLayoutManager(requireContext())
        favMovieList.adapter = adapterMovie

        adapterHobbies = AdapterHobbies(requireContext(), hobbiesObjects)
        hobbiesList.layoutManager = LinearLayoutManager(requireContext())
        hobbiesList.adapter = adapterHobbies

        adapterSkill = AdapterSkill(requireContext(), skillsObjects)
        skillsList.layoutManager = LinearLayoutManager(requireContext())
        skillsList.adapter = adapterSkill
    }

    // ✅ Check if user filled in at least one list
    private fun isFormFilled(): Boolean {
        return songsText.isNotEmpty() ||
                moviesText.isNotEmpty() ||
                hobbiesText.isNotEmpty() ||
                skillsPairs.isNotEmpty()
    }


    // Add Song
    private fun setupButtonActions() = with(binding) {

        // 🔹 Add Song
        addSongButton.setOnClickListener {
            val text = favSongInput.text?.toString()?.trim().orEmpty()
            if (text.isNotEmpty()) {
                songs.add(Song(text))
                songsText.add(text)
                adapterSong.notifyItemInserted(songs.size - 1)
                favSongInput.text?.clear()
            } else {
                Snackbar.make(root, "Please enter a song!", Snackbar.LENGTH_SHORT).show()
            }
        }

        // 🔹 Add Movie
        addMovieButton.setOnClickListener {
            val text = favMovieInput.text?.toString()?.trim().orEmpty()
            if (text.isNotEmpty()) {
                movies.add(Movie(text))
                moviesText.add(text)
                adapterMovie.notifyItemInserted(movies.size - 1)
                favMovieInput.text?.clear()
            } else {
                Snackbar.make(root, "Please enter a movie!", Snackbar.LENGTH_SHORT).show()
            }
        }

        // 🔹 Add Hobby
        addHobbyButton.setOnClickListener {
            val text = hobbyInput.text?.toString()?.trim().orEmpty()
            if (text.isNotEmpty()) {
                hobbiesObjects.add(Hobbies(text))
                hobbiesText.add(text)
                adapterHobbies.notifyItemInserted(hobbiesObjects.size - 1)
                hobbyInput.text?.clear()
            } else {
                Snackbar.make(root, "Please enter a hobby!", Snackbar.LENGTH_SHORT).show()
            }
        }

        // 🔹 Add Skill (with rating)
        addSkillButton.setOnClickListener {
            val skillText = skillInput.text?.toString()?.trim().orEmpty()
            val rateText = skillRateInput.selectedItem?.toString() ?: "Rate"

            if (skillText.isNotEmpty() && rateText != "Rate") {
                val rateInt = rateText.toIntOrNull() ?: 0
                skillsObjects.add(Skill(skillText, rateInt))
                skillsPairs.add(Pair(skillText, rateText))
                adapterSkill.notifyItemInserted(skillsObjects.size - 1)
                skillInput.text?.clear()
                skillRateInput.setSelection(0)
            } else {
                Snackbar.make(
                    root,
                    "Please enter a skill and select a rate!",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        // 🔹 BACK BUTTON
        backButton.setOnClickListener {
            setFragmentResult("slamBookData", bundleOf("slamBook" to slamBook))
            findNavController().popBackStack()
        }

        // 🔹 NEXT BUTTON (Validation)
        nextButton.setOnClickListener {
            // Add pending inputs if user typed but didn’t press add
            favSongInput.text?.toString()?.trim()?.takeIf { it.isNotEmpty() }?.let {
                songs.add(Song(it))
                songsText.add(it)
            }

            favMovieInput.text?.toString()?.trim()?.takeIf { it.isNotEmpty() }?.let {
                movies.add(Movie(it))
                moviesText.add(it)
            }

            hobbyInput.text?.toString()?.trim()?.takeIf { it.isNotEmpty() }?.let {
                hobbiesObjects.add(Hobbies(it))
                hobbiesText.add(it)
            }

            skillInput.text?.toString()?.trim()?.takeIf { it.isNotEmpty() }?.let { skill ->
                val rateText = skillRateInput.selectedItem?.toString() ?: "Rate"
                if (rateText != "Rate") {
                    val rateInt = rateText.toIntOrNull() ?: 0
                    skillsObjects.add(Skill(skill, rateInt))
                    skillsPairs.add(Pair(skill, rateText))
                }
            }

            // ✅ Validation: check if all lists have at least one item
            if (songsText.isEmpty()) {
                Snackbar.make(root, "Please add at least one favorite song!", Snackbar.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (moviesText.isEmpty()) {
                Snackbar.make(
                    root,
                    "Please add at least one favorite movie!",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (hobbiesText.isEmpty()) {
                Snackbar.make(root, "Please add at least one hobby!", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (skillsPairs.isEmpty()) {
                Snackbar.make(
                    root,
                    "Please add at least one skill with a rate!",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // ✅ Save everything to slamBook
            slamBook.favSongs = ArrayList(songsText)
            slamBook.favMovies = ArrayList(moviesText)
            slamBook.hobbies = ArrayList(hobbiesText)
            slamBook.skills = ArrayList(skillsPairs)

            val bundle = Bundle().apply { putParcelable("slamBook", slamBook) }

            try {
                findNavController().navigate(
                    R.id.action_formPageTwoFragment_to_formPageThreeFragment,
                    bundle
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Snackbar.make(root, "Navigation error: ${e.message}", Snackbar.LENGTH_LONG).show()
            }
        }
    }

        // ✅ Must be OUTSIDE setupButtonActions()
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

