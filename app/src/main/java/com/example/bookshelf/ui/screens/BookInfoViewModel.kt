package com.example.bookshelf.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bookshelf.BookshelfApplication
import com.example.bookshelf.data.BookInfoRepository
import com.example.bookshelf.network.BookInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface BookInfoUiState {
    data class Success(val book: BookInfo) : BookInfoUiState
    data object Error : BookInfoUiState
    data object Loading : BookInfoUiState
}

class BookInfoViewModel(private val bookInfoRepository: BookInfoRepository) : ViewModel() {

    var bookInfoUiState: BookInfoUiState by mutableStateOf(BookInfoUiState.Loading)
        private set

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BookshelfApplication)
                val booksRepository = application.container.bookInfoRepository
                BookInfoViewModel(booksRepository)
            }
        }
    }

    fun getBookInfo(bookId: String) {
        viewModelScope.launch {
            bookInfoUiState = BookInfoUiState.Loading
            try {
                bookInfoUiState = BookInfoUiState.Success(bookInfoRepository.getBookInfo(bookId))
            } catch (e: IOException) {
                bookInfoUiState = BookInfoUiState.Error
            }
        }
    }
}