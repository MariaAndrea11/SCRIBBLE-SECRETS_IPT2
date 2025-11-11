package com.kodego.diangca.ebrahim.myslambook.model

import android.os.Parcelable
import android.util.Log
import kotlinx.parcelize.Parcelize

@Parcelize
data class SlamBook(
    // 🔹 Basic Info (Form 1)
    var nickName: String = "",
    var friendCallMe: String = "",
    var likeToCallMe: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var birthDate: String = "",   // combined year, month, day
    var gender: String = "",
    var status: String = "",
    var email: String = "",
    var contactNo: String = "",
    var address: String = "",

    // 🔹 Form 2: Favorites, hobbies, and skills
    var favSongs: ArrayList<String> = ArrayList(),
    var favMovies: ArrayList<String> = ArrayList(),
    var hobbies: ArrayList<String> = ArrayList(),
    var skills: ArrayList<Pair<String, String>> = ArrayList(),  // (Skill, Rating)

    // 🔹 Form 3: Personality and messages
    var defineLove: String = "",
    var defineFriendship: String = "",
    var memorableExperience: String = "",
    var describeMe: String = "",
    var adviceForMe: String = "",
    var rateMe: Float = 0f,
    var imageUri: String? = null,


    // 🔹 Optional profile picture (URI)
    var profilePic: String? = null
) : Parcelable {

    // ✅ Debug helper: Print all SlamBook data to Logcat
    fun printLog() {
        Log.d("SlamBook", """
            --- SlamBook Data ---
            Nickname: $nickName
            Friend Call Me: $friendCallMe
            Like To Call Me: $likeToCallMe
            First Name: $firstName
            Last Name: $lastName
            Birth Date: $birthDate
            Gender: $gender
            Status: $status
            Email: $email
            Contact No: $contactNo
            Address: $address
            Fav Songs: $favSongs
            Fav Movies: $favMovies
            Hobbies: $hobbies
            Skills: $skills
            Define Love: $defineLove
            Define Friendship: $defineFriendship
            Memorable Experience: $memorableExperience
            Describe Me: $describeMe
            Advice For Me: $adviceForMe
            Rate Me: $rateMe
            Profile Pic: $profilePic
            ---------------------
        """.trimIndent())
    }
}

/* ----------------------------------------------------------
   ✅ Shared Storage for SlamBook entries (works across activities)
   This prevents your list from resetting when opening new forms.
------------------------------------------------------------- */


