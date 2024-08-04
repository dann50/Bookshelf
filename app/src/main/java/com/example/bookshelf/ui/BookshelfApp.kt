@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.bookshelf.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookshelf.ui.screens.BooksViewModel
import com.example.bookshelf.ui.screens.HomeScreen


@Composable
fun BookshelfApp() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val booksViewModel: BooksViewModel = viewModel(factory = BooksViewModel.Factory)

    val searchText by booksViewModel.searchText.collectAsState()
    val isSearching by booksViewModel.isSearching.collectAsState()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Box {
                TopSearchBar(
                    query = searchText,
                    onQueryChange = booksViewModel::changeSearchText,
                    onSearch = {
                        booksViewModel.getBooks()
                        booksViewModel.toggleSearch()
                    },
                    isSearching = isSearching,
                    onActiveChange = { booksViewModel.toggleSearch() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .padding(horizontal = 16.dp)
                )
            }
        }
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            HomeScreen(
                booksUiState = booksViewModel.booksUiState,
                retryAction = booksViewModel::getBooks,
                contentPadding = it
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    isSearching: Boolean,
    onActiveChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = isSearching,
        onActiveChange = onActiveChange,
        placeholder = {
            Text(text = "Enter search term")
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        },
        modifier = modifier
    ) {

    }
}