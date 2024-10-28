package com.nicolas.picstream.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.nicolas.picstream.R
import com.nicolas.picstream.connectivity.NetworkStatus
import com.nicolas.picstream.data.model.Photo
import com.nicolas.picstream.downloader.AndroidDownloader
import com.nicolas.picstream.utils.showToast
import kotlinx.coroutines.launch

@Composable
fun StaggeredListView(
    modifier: Modifier = Modifier,
    photos: LazyPagingItems<Photo>,
    networkStatus: State<NetworkStatus>,
    onDownloadImage: (String) -> Unit
) {

    val listState: LazyStaggeredGridState = rememberLazyStaggeredGridState()
    val context = LocalContext.current
    val isSelectItems = remember { mutableStateListOf<String>() }
    val downloader = remember { AndroidDownloader(context) }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        if (photos.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyVerticalStaggeredGrid(
                contentPadding = PaddingValues(horizontal = 10.dp),
                state = listState,
                modifier = modifier,
                columns = StaggeredGridCells.Adaptive(130.dp),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(photos.itemCount) { index ->
                    photos[index]?.let { currentPhoto ->

                        val isSelected = isSelectItems.contains(currentPhoto.id)

                        PhotoView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.medium),
                            photo = currentPhoto,
                            /* State Hoisting: pattern of de moving state to composable's caller. A: state -> B, B: event -> A  */
                            onDownload = {
                                if (networkStatus.value == NetworkStatus.Connected && !isSelected) {
                                    val downloadImageId = downloader.downloadFile(
                                        url = currentPhoto.url.regular,
                                        title = currentPhoto.slug.toString()
                                    )
                                    isSelectItems.add(currentPhoto.id)
                                    if (downloadImageId != -1L)
                                        onDownloadImage.invoke(currentPhoto.slug.toString())
                                }


                                if (networkStatus.value == NetworkStatus.Disconnected) {
                                    showToast(
                                        context = context,
                                        message = context.getString(R.string.label_cannot_download)
                                    )
                                }
                            },
                            icon = if (isSelected) Icons.Rounded.Check else Icons.Rounded.Download
                        )
                    }
                }
                item {
                    if (photos.loadState.append is LoadState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
        AnimatedContent(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp),
            targetState = listState.firstVisibleItemIndex > 0,
            label = "",
        ) {
            if (it) {
                Icon(
                    imageVector = Icons.Rounded.ArrowUpward, contentDescription = null,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable {
                            coroutineScope.launch { listState.animateScrollToItem(0) }
                        }
                        .padding(10.dp)
                        .align(Alignment.BottomEnd),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}