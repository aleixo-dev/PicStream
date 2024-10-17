package com.nicolas.picstream.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun NetworkInfoView(
    modifier: Modifier = Modifier,
    color: Color,
    icon: ImageVector,
    title: String,
    style : TextStyle = MaterialTheme.typography.titleSmall
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(modifier = modifier.size(30.dp),
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = title, color = MaterialTheme.colorScheme.onPrimary,
            style = style
        )
    }
}