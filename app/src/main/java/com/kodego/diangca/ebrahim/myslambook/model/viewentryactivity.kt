package com.kodego.diangca.ebrahim.myslambook

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kodego.diangca.ebrahim.myslambook.databinding.ActivityViewEntryBinding
import com.kodego.diangca.ebrahim.myslambook.model.SlamBook

class ViewEntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewEntryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val entry = intent.getParcelableExtra<SlamBook>("slamBookEntry")

        if (entry != null) {
            binding.txtName.text = "${entry.firstName} ${entry.lastName}"
            binding.txtLove.text = entry.defineLove
            binding.txtFriendship.text = entry.defineFriendship
            binding.txtExperience.text = entry.memorableExperience
            binding.txtDescribe.text = entry.describeMe
            binding.txtAdvice.text = entry.adviceForMe
            binding.txtRating.text = entry.rateMe.toString()

            // ✅ DISPLAY IMAGE
            if (!entry.imageUri.isNullOrEmpty()) {
                binding.imageProfile.setImageURI(Uri.parse(entry.imageUri))
            } else {
                binding.imageProfile.setImageResource(R.drawable.prof_icon) // fallback image
            }
        }
    }
}
