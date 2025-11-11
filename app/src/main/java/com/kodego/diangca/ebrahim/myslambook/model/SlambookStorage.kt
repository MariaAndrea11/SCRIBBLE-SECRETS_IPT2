package com.kodego.diangca.ebrahim.myslambook.model

object SlamBookStorage {
    private val entries = mutableListOf<SlamBook>()

    fun addEntry(entry: SlamBook) {
        entries.add(entry)
    }

    fun getAll(): List<SlamBook> = entries
}
