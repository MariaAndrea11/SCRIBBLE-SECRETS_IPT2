package com.kodego.diangca.ebrahim.myslambook

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.kodego.diangca.ebrahim.myslambook.databinding.ActivityMenuBinding
import com.kodego.diangca.ebrahim.myslambook.model.SlamBook
import com.kodego.diangca.ebrahim.myslambook.model.SlamBookStorage  // ✅ Moved import HERE (outside class)

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ Receive data from FormPageThreeFragment
        val newSlamBook = intent.getParcelableExtra<SlamBook>("slamBookData")
        if (newSlamBook != null) {
            SlamBookStorage.addEntry(newSlamBook)
        }

        // ✅ Initialize list view
        updateListView()

        // ✅ “Create” button — open form
        binding.btnCreate.setOnClickListener {
            val nextForm = Intent(this, FormActivity::class.java)
            startActivity(nextForm)
        }

        // ✅ “View List” button — toggle visibility
        binding.btnView.setOnClickListener {
            toggleListPanel()
        }

        // ✅ Handle item clicks (open detail page)
        binding.listViewEntries.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val allBooks = SlamBookStorage.getAll()  // ✅ Fix here
                val selected = allBooks[position]

                val intent = Intent(this, ViewEntryActivity::class.java)
                intent.putExtra("slamBookEntry", selected)
                startActivity(intent)
            }
    }
    override fun onResume() {
        super.onResume()
        updateListView()   // Refresh list whenever user comes back to MenuActivity
    }
    private fun toggleListPanel() {
        binding.listPanel.visibility =
            if (binding.listPanel.visibility == View.GONE) View.VISIBLE else View.GONE
    }

    private fun updateListView() {
        val allBooks = SlamBookStorage.getAll()  // ✅ Use storage
        if (allBooks.isNotEmpty()) {
            val names = allBooks.map { "${it.firstName} ${it.lastName}" }
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, names)
            binding.listViewEntries.adapter = adapter
            binding.listPanel.visibility = View.VISIBLE
        } else {
            binding.listPanel.visibility = View.GONE
        }
    }
}