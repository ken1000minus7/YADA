package com.kamikaze.yada.model

import com.kamikaze.yada.diary.Diary

data class User(
    val uid: String = "",
    val displayName: String? = "",
    val imageUrl: String = "",
    var diaries: MutableList<Diary?> = mutableListOf(),
    var about: String = ""
)
