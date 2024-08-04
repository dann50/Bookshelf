package com.example.bookshelf.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookshelf.R
import com.example.bookshelf.network.Book
import com.example.bookshelf.ui.theme.BookshelfTheme


@Composable
fun HomeScreen(
    booksUiState: BooksUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    when(booksUiState) {
        is BooksUiState.Loading -> LoadingScreen(modifier.fillMaxSize())
        is BooksUiState.Success -> BooksGrid(books = booksUiState.books, modifier, contentPadding)
        is BooksUiState.Error -> ErrorScreen(retryAction, modifier.fillMaxSize())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksGrid(
    books: List<Book>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var currentBookId by remember { mutableStateOf("") }

    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val windowInsets: WindowInsets = WindowInsets.displayCutout
    val bookInfoViewModel: BookInfoViewModel = viewModel(factory = BookInfoViewModel.Factory)

    LazyVerticalGrid(
        modifier = modifier.padding(top = 4.dp, start = 4.dp, end = 4.dp),
        columns = GridCells.Adaptive(120.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = contentPadding
    ) {
        items(books) {
            BookCard(
                book = it,
                onClick = {book ->
                    currentBookId = book.id!!
                    bookInfoViewModel.getBookInfo(book.id)
                    showBottomSheet = true
                }
            )
        }
    }

    if (showBottomSheet) {
        BookInfoBottomSheet(
            bookInfoViewModel.bookInfoUiState,
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            bottomPadding = bottomPadding,
            windowInsets = windowInsets,
            retryAction = { bookInfoViewModel.getBookInfo(currentBookId) }
        )
    }
}

@Composable
fun BookCard(book: Book, modifier: Modifier = Modifier, onClick: (Book) -> Unit ) {
    Card(
        shape = RoundedCornerShape(0.dp),
        modifier = modifier.clickable { onClick(book) }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(book.volumeInfo?.imageLinks?.thumbnail?.replace("http", "https"))
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            error = painterResource(R.drawable.ic_broken_image),
            placeholder = painterResource(id = R.drawable.loading_img),
            modifier = Modifier.aspectRatio(0.65f)
        )
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            strokeWidth = 5.dp,
            modifier = Modifier.size(60.dp)
        )
        Text(text = "Fetching Books...", Modifier.padding(top = 35.dp))
    }
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun Detail(placeholder: String, info: String) {
    Row {
        Text(
            text = placeholder,
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = info,
            Modifier.padding(start = 8.dp)
        )
    }

}


@Preview(showBackground = true)
@Composable
fun ResultScreenPreview() {
    BookshelfTheme {
        Detail(placeholder = "Hello:", info = "Hi" )
    }
}