package com.example.bookshelf.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookshelf.network.BookInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookInfoBottomSheet(
    bookInfoUiState: BookInfoUiState,
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    bottomPadding: Dp,
    windowInsets: WindowInsets,
    retryAction: () -> Unit
) {

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        sheetMaxWidth = Dp.Unspecified,
        windowInsets = windowInsets,
        modifier = Modifier.padding(bottom = bottomPadding+ 10.dp)
    ) {
        when(bookInfoUiState) {
            is BookInfoUiState.Loading -> {
                LoadingScreen(
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottomPadding+10.dp))
            }
            is BookInfoUiState.Success -> {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp, start = 16.dp, bottom = bottomPadding + 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BottomSheet(bookInfoUiState.book)
                }
            }
            is BookInfoUiState.Error -> ErrorScreen(
                retryAction = retryAction,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottomPadding+10.dp)
            )
        }
    }
}

@Composable
fun BottomSheet(book: BookInfo) {
    val s = (book.volumeInfo?.authors ?: " Not Available ").toString()
    val c = (book.volumeInfo?.categories ?: " Not Available ").toString()

    Text(
        text = book.volumeInfo?.title ?: "Title Not Available",
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 35.dp, start = 35.dp, bottom = 8.dp),
        fontSize = 22.sp,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold
    )
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Detail(placeholder = "Author(s)", info = s.substring(1, s.length - 1))
        Detail(
            placeholder = "Publisher:",
            info = book.volumeInfo?.publisher ?: "Not Available"
        )
        Detail(
            placeholder = "Published:",
            info = book.volumeInfo?.publishedDate ?: "Not Available"
        )
        Detail(
            placeholder = "Pages:",
            info = (book.volumeInfo?.pageCount ?: "Not Available").toString()
        )
        Detail(placeholder = "Categories:", info = c.substring(1, c.length - 1))
    }

}