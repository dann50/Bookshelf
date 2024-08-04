package com.example.bookshelf.data

import com.example.bookshelf.network.BooksApiService
import com.example.bookshelf.network.Response

interface BooksRepository {
    suspend fun getBooks(searchKey: String, entries: Int): Response
}

class NetworkBooksRepository(private val booksApiService: BooksApiService) : BooksRepository {
    override suspend fun getBooks(searchKey: String, entries: Int): Response = booksApiService.getBooks(searchKey, entries)
}

