package com.example.bookshelf.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BooksApiService {
    @GET("volumes")
    suspend fun getBooks(
        @Query("q") searchKey: String,
        @Query("maxResults") entries: Int
    ): Response
	//impl: https://www.googleapis.com/books/v1/volumes?q={searchKey}&maxResults={entries}

    @GET("volumes/{id}")
    suspend fun getBookInfo(@Path("id") bookId: String): BookInfo
	//impl: https://www.googleapis.com/books/v1/volumes/{id}
}