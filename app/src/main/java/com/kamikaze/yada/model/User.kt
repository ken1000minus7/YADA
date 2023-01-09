package com.kamikaze.yada.model
import com.kamikaze.yada.diary.Diary
import java.util.ArrayList

data class User(
    val uid: String = "",
    val displayName: String? = "",
    val imageUrl: String = "",
    var diaries: ArrayList<Diary> = ArrayList(),
    var about: String = ""
)




