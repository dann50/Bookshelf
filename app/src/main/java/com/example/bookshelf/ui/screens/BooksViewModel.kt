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
import com.example.bookshelf.data.BooksRepository
import com.example.bookshelf.network.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException


sealed interface BooksUiState {
    data class Success(val books: List<Book>) : BooksUiState
    data object Error : BooksUiState
    data object Loading : BooksUiState
}
class BooksViewModel(private val booksRepository: BooksRepository) : ViewModel() {
    private val entries = 40
    var booksUiState: BooksUiState by mutableStateOf(BooksUiState.Loading)
        private set

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow("google")
    val searchText = _searchText.asStateFlow()

    init {
        getBooks()
        changeSearchText("")
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BookshelfApplication)
                val booksRepository = application.container.booksRepository
                BooksViewModel(booksRepository)
            }
        }
    }

    fun getBooks() {
        viewModelScope.launch {
            booksUiState = BooksUiState.Loading
            try {
                booksUiState = BooksUiState.Success(booksRepository.getBooks(_searchText.value, entries).items ?: listOf())
            } catch (e: IOException) {
                booksUiState = BooksUiState.Error
            }
        }
    }

    fun changeSearchText(text: String) {
        _searchText.value = text
    }

    fun toggleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            changeSearchText("")
        }
    }
}