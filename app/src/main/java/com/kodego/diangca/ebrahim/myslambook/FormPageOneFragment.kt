package com.kodego.diangca.ebrahim.myslambook

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.core.os.bundleOf
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.get
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kodego.diangca.ebrahim.myslambook.databinding.FragmentFormPageOneBinding
import com.kodego.diangca.ebrahim.myslambook.model.SlamBook
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class FormPageOneFragment() : Fragment() {

    private lateinit var binding: FragmentFormPageOneBinding

    private lateinit var slamBook: SlamBook

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        Log.d("ON_RESUME", "RESUME_PAGE_ONE")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("ON_ATTACH", "ATTACH_PAGE_ONE")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFormPageOneBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener("slamBookData") { _, bundle ->
            val updatedSlamBook = bundle.getParcelable<SlamBook>("slamBook")
            if (updatedSlamBook != null) {
                slamBook = updatedSlamBook
                restoreField()
            }
        }



        initComponents()
    }

    private fun initComponents() {
        if (arguments != null && arguments?.containsKey("slamBook") == true) { // ✅ fixed key
            slamBook = arguments?.getParcelable("slamBook")!!
            slamBook.printLog()
            restoreField()
        } else {
            slamBook = SlamBook()
        }

        with(binding) {
            btnBack.setOnClickListener { btnBackOnClickListener() }
            btnNext.setOnClickListener { btnNextOnClickListener() }
            dateMonth.prompt = "Please enter Birth Month"
            dateDay.prompt = "Please enter Birth day"
            gender.prompt = "Please enter Gender"
            status.prompt = "Please enter Status"
            contactNo.filters = arrayOf(android.text.InputFilter.LengthFilter(11))
        }
    }


    @SuppressLint("SimpleDateFormat")
    private fun restoreField() {

        binding.apply {
            nickName.setText(slamBook.nickName)
            friendCall.setText(slamBook.friendCallMe)
            likeToCall.setText(slamBook.likeToCallMe)
            lastName.setText(slamBook.lastName)
            firstName.setText(slamBook.firstName)

            if(!slamBook.birthDate.isNullOrEmpty()){
                val arrayMonth = resources.getStringArray(R.array.monthName)
                val arrayDay = resources.getStringArray(R.array.monthDay)
                val birtDate: Date = Date(slamBook.birthDate)

                dateMonth.setSelection(arrayMonth.indexOf(SimpleDateFormat("MMMM").format(birtDate)));
                dateDay.setSelection(arrayDay.indexOf(SimpleDateFormat("dd").format(birtDate)));
                dateYear.setText(SimpleDateFormat("yyyy").format(birtDate));
            }

            if(!slamBook.gender.isNullOrEmpty()){
                val arrayGender = resources.getStringArray(R.array.gender)
                gender.setSelection(arrayGender.indexOf(slamBook.gender))
            }

            if(!slamBook.status.isNullOrEmpty()){
            val arrayStatus = resources.getStringArray(R.array.status)
            status.setSelection(arrayStatus.indexOf(slamBook.status))
            }
            emailAdd.setText(slamBook.email)
            contactNo.setText(slamBook.contactNo)
            address.setText(slamBook.address)

        }
    }

    private fun isReadableName(input: String): Boolean {
        // Only allows letters, spaces, hyphens, and apostrophes (no numbers or symbols)
        val regex = Regex("^[a-zA-ZÀ-ž\\s'-]{2,}$")
        return regex.matches(input)
    }

    private fun isValidEmail(email: String): Boolean {
        val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
        return regex.matches(email)
    }

    private fun isReadableAddress(address: String): Boolean {
        // Allows letters, digits, commas, periods, and spaces (no nonsense characters)
        val regex = Regex("^[a-zA-Z0-9À-ž\\s,.'#-]{5,}$")
        return regex.matches(address)
    }
    private fun isValidPhilippineNumber(number: String): Boolean {
        val trimmed = number.trim()
        val regex = Regex("^(\\+639|09)\\d{9}$")
        return regex.matches(number)
    }

    private fun getPhoneErrorMessage(number: String): String? {
        val trimmed = number.trim()
        return when {
            trimmed.isEmpty() -> "Please enter Contact Number"
            trimmed.startsWith("09") && trimmed.length != 11 ->
                "PH numbers starting with 09 must be 11 digits long"
            !isValidPhilippineNumber(trimmed) ->
                "Please enter a valid Philippine contact number"
            else -> null
        }
    }

    private fun btnNextOnClickListener() {
        with(binding) {
            var hasError = false

            // Validate names
            if (nickName.text.isNullOrEmpty() || !isReadableName(nickName.text.toString())) {
                nickName.error = "Please enter a valid Nickname"
                hasError = true
            }
            if (friendCall.text.isNullOrEmpty() || !isReadableName(friendCall.text.toString())) {
                friendCall.error = "Please enter a valid 'Friend call you as'"
                hasError = true
            }
            if (likeToCall.text.isNullOrEmpty() || !isReadableName(likeToCall.text.toString())) {
                likeToCall.error = "Please enter a valid 'Like to call me as'"
                hasError = true
            }
            if (lastName.text.isNullOrEmpty() || !isReadableName(lastName.text.toString())) {
                lastName.error = "Please enter a valid Last Name"
                hasError = true
            }
            if (firstName.text.isNullOrEmpty() || !isReadableName(firstName.text.toString())) {
                firstName.error = "Please enter a valid First Name"
                hasError = true
            }

            // Validate dropdowns and date
            val yearText = dateYear.text.toString().trim()
            if (yearText.isNotEmpty()) {
                val year = yearText.toIntOrNull()
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                if (year == null || year > currentYear || year == 2025) {
                    dateYear.error = "Please enter a valid year (not 2025 or future)"
                    Snackbar.make(root, "Invalid birth year: $yearText", Snackbar.LENGTH_SHORT).show()
                    hasError = true
                }
            }

            if (dateMonth.selectedItemPosition == 0 ||
                dateDay.selectedItemPosition == 0 ||
                dateYear.text.isNullOrEmpty()


            ) {
                Snackbar.make(root, "Please select a valid birth date", Snackbar.LENGTH_SHORT).show()
                hasError = true
            }
            if (gender.selectedItemPosition == 0) hasError = true
            if (status.selectedItemPosition == 0) hasError = true

            // Validate email
            if (emailAdd.text.isNullOrEmpty() || !isValidEmail(emailAdd.text.toString())) {
                emailAdd.error = "Please enter a valid Email"
                hasError = true
            }

            // Validate address
            if (address.text.isNullOrEmpty() || !isReadableAddress(address.text.toString())) {
                address.error = "Please enter a valid Address"
                hasError = true
            }

            // Validate contact number
            val contactText = contactNo.text?.toString()?.trim() ?: ""
            val contactError = getPhoneErrorMessage(contactText)

            if (contactError != null) {
                contactNo.error = contactError
                hasError = true
            }

            if (hasError) {
                Snackbar.make(root, "Please check invalid or empty fields", Snackbar.LENGTH_SHORT).show()
                return
            }

            // ✅ If all valid, save to SlamBook
            slamBook.nickName = nickName.text.toString()
            slamBook.friendCallMe = friendCall.text.toString()
            slamBook.likeToCallMe = likeToCall.text.toString()
            slamBook.lastName = lastName.text.toString()
            slamBook.firstName = firstName.text.toString()
            slamBook.birthDate =
                "${dateMonth.selectedItem} ${dateDay.selectedItem}, ${dateYear.text}"
            slamBook.gender = gender.selectedItem.toString()
            slamBook.status = status.selectedItem.toString()
            slamBook.email = emailAdd.text.toString()
            slamBook.contactNo = contactNo.text.toString()
            slamBook.address = address.text.toString()

            val bundle = Bundle()
            bundle.putParcelable("slamBook", slamBook)

            findNavController().navigate(
                R.id.action_formPageOneFragment_to_formPageTwoFragment,
                bundleOf("slamBook" to slamBook)
            )

        }
    }


    private fun btnBackOnClickListener() {
        val intent = Intent(requireContext(), MenuActivity::class.java)
        startActivity(intent)
        requireActivity().finish()  // ✅ close the form activity to avoid black screen
    }


}