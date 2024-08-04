package com.example.bookshelf.network

import kotlinx.serialization.Serializable

@Serializable
data class BookInfo(
    val id: String? = null,
    val volumeInfo: VolumeInfo? = null
) {
    @Serializable
    class VolumeInfo {
        val title : String? = null
        val authors: List<String>? = null
        val publisher: String? = null
        val publishedDate: String? = null
        val pageCount: Int? = null
        val categories: List<String>? = null
        val imageLinks: ImageLinks? = null
    }

    @Serializable
    class ImageLinks {
        val thumbnail: String? = null
    }
}