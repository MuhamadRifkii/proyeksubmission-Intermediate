package com.dicoding.proyeksubmission_intermediate

import com.dicoding.proyeksubmission_intermediate.data.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "photoUrl + $i",
                "name + $i",
                "description + $i",
                "id + $i",
                lon = i.toDouble(),
                lat = i.toDouble()
            )
            items.add(story)
        }
        return items
    }
}