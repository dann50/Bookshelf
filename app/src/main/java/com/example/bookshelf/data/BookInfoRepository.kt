package com.example.bookshelf.data

import com.example.bookshelf.network.BookInfo
import com.example.bookshelf.network.BooksApiService

interface BookInfoRepository {
    suspend fun getBookInfo(bookId: String): BookInfo
}

class NetworkBookInfoRepository(private val booksApiService: BooksApiService) : BookInfoRepository {
    override suspend fun getBookInfo(bookId: String): BookInfo = booksApiService.getBookInfo(bookId)
}