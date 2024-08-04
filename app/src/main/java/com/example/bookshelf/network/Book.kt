package com.example.bookshelf.network

import kotlinx.serialization.Serializable

@Serializable
class Response {
    val items: List<Book>? = null
}

@Serializable
data class Book(
    val id: String? = null,
    val volumeInfo: VolumeInfo? = null
) {
    @Serializable
    class VolumeInfo {
        val imageLinks: ImageLinks? = null
    }

    @Serializable
    class ImageLinks {
        val thumbnail: String? = null
    }
}

