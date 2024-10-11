package com.nicolas.picstream.ui.option

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.ReportProblem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nicolas.picstream.ui.option.options.OptionHelper
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun OptionRoute(
    modifier: Modifier = Modifier,
    onBackScreen: () -> Unit,
    optionViewModel: OptionViewModel = koinViewModel()
) {

    val scope = rememberCoroutineScope()
    val notificationActive by optionViewModel.notificationActive.collectAsStateWithLifecycle()

    OptionScreen(
        modifier = modifier,
        onBackScreen = onBackScreen,
        active = notificationActive.toBoolean(),
        onCheckedChange = {
            scope.launch { optionViewModel.toggleNotification(it) }
        }
    )
}

@Composable
internal fun OptionScreen(
    modifier: Modifier = Modifier,
    onBackScreen: () -> Unit,
    active: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

    val context = LocalContext.current
    val optionHelper = remember { OptionHelper(context) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                modifier = Modifier.clickable { onBackScreen.invoke() },
                imageVector = Icons.Rounded.ArrowBackIosNew,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "Opções",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Icon(
                modifier = Modifier.clickable { onBackScreen.invoke() },
                imageVector = Icons.Rounded.ReportProblem,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        LazyColumn(
            contentPadding = PaddingValues(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(optionHelper.options, key = { it.id }) { option ->

                OptionItem(
                    title = option.title,
                    description = option.description,
                    enabled = active,
                    onCheckedChange = { onCheckedChange.invoke(it) }
                )
            }
        }
    }
}

@Composable
fun OptionItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(
                description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Switch(
            checked = enabled,
            onCheckedChange = { onCheckedChange.invoke(it) },
        )
    }
}