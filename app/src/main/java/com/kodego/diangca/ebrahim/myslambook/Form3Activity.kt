package com.kodego.diangca.ebrahim.myslambook

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kodego.diangca.ebrahim.myslambook.databinding.ActivityForm3Binding
import com.kodego.diangca.ebrahim.myslambook.model.SlamBook

class Form3Activity : AppCompatActivity() {

    private lateinit var binding: ActivityForm3Binding
    private var slamBook: SlamBook? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForm3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ Get the previous data (from Form2 or earlier forms)
        slamBook = intent.getParcelableExtra("slamBook") ?: SlamBook()

        // ✅ Handle Save / Submit button click
        binding.btnSave.setOnClickListener {
            saveSlamBookData()
        }

        // Optional: You can add functionality later for these
        // binding.btnCamera.setOnClickListener { /* TODO: open camera */ }
        // binding.btnBrowse.setOnClickListener { /* TODO: open gallery */ }
    }

    private fun saveSlamBookData() {
        // ✅ Update only Form 3 fields
        slamBook?.apply {
            defineLove = binding.defineLove.text.toString()
            defineFriendship = binding.defineFriendship.text.toString()
            memorableExperience = binding.memorableExperience.text.toString()
            describeMe = binding.describeMe.text.toString()
            adviceForMe = binding.adviceForMe.text.toString()
            rateMe = binding.ratingBar.rating
        }

        // ✅ Send the full SlamBook data to MenuActivity
        val intent = Intent(this, MenuActivity::class.java)
        intent.putExtra("slamBookData", slamBook)
        startActivity(intent)
        finish()
    }
}
