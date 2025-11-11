package com.kodego.diangca.ebrahim.myslambook

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.kodego.diangca.ebrahim.myslambook.databinding.FragmentFormPageThreeBinding
import com.kodego.diangca.ebrahim.myslambook.model.SlamBook

class FormPageThreeFragment : Fragment() {

    private lateinit var slamBook: SlamBook
    private lateinit var binding: FragmentFormPageThreeBinding

    // To store the selected/taken image URI
    private var imageUri: Uri? = null

    // ✅ Take Picture (camera)
    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            bitmap?.let {
                binding.imageProfile.setImageBitmap(it)
                // Optionally, convert bitmap to URI or Base64 if needed
            }
        }

    // ✅ Browse Image (gallery)
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = it
                binding.imageProfile.setImageURI(it)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        slamBook = arguments?.getParcelable("slamBook") ?: SlamBook()
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
            btnBack.setOnClickListener {
                setFragmentResult("slamBookDataFromForm3", bundleOf("slamBook" to slamBook))
                findNavController().popBackStack()
            }

            btnSave.setOnClickListener {
                btnSaveOnClickListener()
            }

            // ✅ New: Handle Picture Buttons
            btnTakePicture.setOnClickListener { openCamera() }
            btnBrowse.setOnClickListener { openGallery() }
        }
    }

    // ✅ Opens Camera
    private fun openCamera() {
        takePictureLauncher.launch(null)
    }

    // ✅ Opens Gallery
    private fun openGallery() {
        pickImageLauncher.launch("image/*")
    }

    private fun btnSaveOnClickListener() {
        slamBook.defineLove = binding.defineLove.text.toString()
        slamBook.defineFriendship = binding.defineFriendship.text.toString()
        slamBook.memorableExperience = binding.memorableExperience.text.toString()
        slamBook.describeMe = binding.describeMe.text.toString()
        slamBook.adviceForMe = binding.adviceForMe.text.toString()
        slamBook.rateMe = binding.ratingBar.rating

        // Optionally, save the image URI
        slamBook.imageUri = imageUri?.toString()

        val intent = Intent(requireContext(), MenuActivity::class.java)
        intent.putExtra("slamBookData", slamBook)
        startActivity(intent)

    }

    private fun btnBackOnClickListener() {
        val bundle = Bundle()
        bundle.putParcelable("slamBook", slamBook)
        findNavController().navigate(R.id.action_formPageThreeFragment_to_formPageTwoFragment, bundle)
    }
}
