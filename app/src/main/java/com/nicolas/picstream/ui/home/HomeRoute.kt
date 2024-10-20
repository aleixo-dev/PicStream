package com.nicolas.picstream.ui.home

import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.nicolas.picstream.R
import com.nicolas.picstream.components.ErrorView
import com.nicolas.picstream.components.StaggeredListView
import com.nicolas.picstream.connectivity.NetworkStatus
import com.nicolas.picstream.data.model.Photo
import com.nicolas.picstream.data.model.Topic
import com.nicolas.picstream.utils.showToast
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateOptions: () -> Unit,
    onNavigateNotifications: () -> Unit
) {

    val state by viewModel.photoState.collectAsStateWithLifecycle()
    val topics by viewModel.photoTopics.collectAsStateWithLifecycle()
    val searchPhotoPagingItems = viewModel.photoQueryState.collectAsLazyPagingItems()
    val photosPagingItems = viewModel.photoLocalPagingFlow.collectAsLazyPagingItems()
    val networkStatus = viewModel.networkConnectivityState.collectAsState()
    val topicPhotos = viewModel.photoTopicFilter.collectAsLazyPagingItems()
    val hideKeyboard by viewModel.hideKeyboard.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.getTopics() }

    HomeScreen(
        modifier = modifier,
        state = state,
        onSearch = viewModel::onSearch,
        searchPhotoPagingItems = searchPhotoPagingItems,
        photosPagingItems = photosPagingItems,
        networkStatus = networkStatus,
        topics = topics,
        onTopicSelect = viewModel::onTopic,
        topicPhotos = topicPhotos,
        hideKeyboard = hideKeyboard,
        onNavigateOptions = onNavigateOptions,
        onNavigateNotifications = onNavigateNotifications
    )
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeUiState,
    onSearch: (String) -> Unit,
    searchPhotoPagingItems: LazyPagingItems<Photo>,
    photosPagingItems: LazyPagingItems<Photo>,
    networkStatus: State<NetworkStatus>,
    topics: List<Topic>,
    onTopicSelect: (slug: String) -> Unit,
    topicPhotos: LazyPagingItems<Photo>,
    hideKeyboard: Boolean,
    onNavigateOptions: () -> Unit,
    onNavigateNotifications: () -> Unit
) {

    ReportDrawnWhen {
        networkStatus.value == NetworkStatus.Connected && state is HomeUiState.Success
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (state) {
            HomeUiState.Loading -> {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) { CircularProgressIndicator() }
            }

            HomeUiState.Error -> ErrorView(
                title = stringResource(R.string.error_view_title),
                subtitle = stringResource(R.string.error_view_subtitle)
            )

            is HomeUiState.Success -> {
                SectionDefaultPhoto(
                    modifier = modifier,
                    onSearch = onSearch,
                    photos = photosPagingItems,
                    searchPhotoPagingItems = searchPhotoPagingItems,
                    networkStatus = networkStatus,
                    topics = topics,
                    onTopicSelect = onTopicSelect,
                    topicPhotos = topicPhotos,
                    hideKeyboard = hideKeyboard,
                    onNavigateOptions = onNavigateOptions,
                    onNavigateNotifications = onNavigateNotifications
                )
            }
        }
    }
}

@Composable
fun SectionDefaultPhoto(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,
    photos: LazyPagingItems<Photo>,
    searchPhotoPagingItems: LazyPagingItems<Photo>,
    networkStatus: State<NetworkStatus>,
    topics: List<Topic>,
    onTopicSelect: (slug: String) -> Unit,
    topicPhotos: LazyPagingItems<Photo>,
    hideKeyboard: Boolean,
    onNavigateOptions: () -> Unit,
    onNavigateNotifications: () -> Unit
) {

    val context = LocalContext.current
    var text by remember { mutableStateOf("") }
    val selectedChip = remember { mutableIntStateOf(-1) }
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(hideKeyboard) { if (hideKeyboard) keyboardController?.hide() }

    Column(modifier = modifier) {
        Column(modifier = Modifier.padding(horizontal = 15.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Light,
                            fontStyle = FontStyle.Italic,
                            fontSize = MaterialTheme.typography.headlineSmall.fontSize
                        )
                    ) {
                        append(stringResource(R.string.one_message_title))
                    }
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium,
                            fontStyle = FontStyle.Italic,
                            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        )
                    ) {
                        append(stringResource(R.string.two_message_title))
                    }
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Light,
                            fontStyle = FontStyle.Italic,
                            fontSize = MaterialTheme.typography.headlineSmall.fontSize
                        )
                    ) {
                        append(stringResource(R.string.three_message_title))
                    }
                }
            )
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    modifier = Modifier.weight(1f),
                    value = text,
                    onValueChange = {
                        text = it
                        selectedChip.intValue = -1
                        onSearch.invoke(text)
                    },
                    placeholder = { Text(text = stringResource(R.string.label_search_input)) },
                    shape = MaterialTheme.shapes.medium,
                    leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                    singleLine = true,
                    trailingIcon = {
                        Icon(
                            Icons.Rounded.Clear,
                            contentDescription = "clear_icon",
                            modifier = Modifier.clickable {
                                text = ""
                            })
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    )
                )
                Icon(
                    modifier = Modifier
                        .size(26.dp)
                        .clickable { onNavigateNotifications.invoke() },
                    imageVector = Icons.Rounded.Notifications,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Icon(
                    modifier = Modifier
                        .size(26.dp)
                        .clickable { onNavigateOptions.invoke() },
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Text(
            stringResource(R.string.label_info_download),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .padding(top = 8.dp)
        )
        Spacer(modifier = Modifier.height(18.dp))
        SectionPhotoTopics(
            modifier = modifier,
            topics = topics,
            onSelectChip = { index, slug ->

                if (networkStatus.value == NetworkStatus.Disconnected) {
                    showToast(
                        context,
                        context.getString(R.string.label_cannot_download)
                    )
                    return@SectionPhotoTopics
                }

                selectedChip.intValue =
                    if (selectedChip.intValue == index) {
                        -1
                    } else {
                        onTopicSelect.invoke(slug)
                        index
                    }

            },
            selectedChip = selectedChip.intValue,
            isTopicsVisible = networkStatus.value === NetworkStatus.Connected
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
    StaggeredListView(
        modifier = modifier,
        photos = if (selectedChip.intValue != -1) {
            topicPhotos
        } else if (text.isBlank()) photos else searchPhotoPagingItems,
        networkStatus = networkStatus,
    )
}

@Composable
fun SectionPhotoTopics(
    modifier: Modifier = Modifier,
    topics: List<Topic>,
    onSelectChip: (index: Int, slug: String) -> Unit,
    selectedChip: Int,
    isTopicsVisible: Boolean
) {
    AnimatedVisibility(isTopicsVisible, label = "") {
        Column(modifier = modifier) {
            Text(
                stringResource(R.string.label_category),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 15.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(start = 15.dp, end = 15.dp)
            ) {
                itemsIndexed(topics) { index, topic ->
                    InputChip(
                        selected = selectedChip == index,
                        onClick = { onSelectChip.invoke(index, topic.slug) },
                        label = { Text(topic.title) },
                        shape = MaterialTheme.shapes.medium
                    )
                }
            }
        }
    }
}
