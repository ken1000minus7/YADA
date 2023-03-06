package com.kamikaze.yada.diary

import com.kamikaze.yada.model.Notes

data class Diary(
    var title: String?,
    var description: String?,
    var location: String? = null,
    var bgImageUrl: String? = null,
    var note: Notes? = null,
    var color: Int = -1,
    var images: MutableList<String?>? = mutableListOf()
)