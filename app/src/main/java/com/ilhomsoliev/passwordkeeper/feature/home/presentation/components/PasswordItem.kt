package com.ilhomsoliev.passwordkeeper.feature.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ilhomsoliev.passwordkeeper.feature.shared.SCachedImage

@Composable
fun PasswordItem(
    modifier: Modifier = Modifier,
    image: String,
    title: String,
    password: String,
    isShown: Boolean,
    onShowClick: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable {
                onClick()
            }
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            SCachedImage(
                url = image, modifier = Modifier
                    .size(36.dp)
                    .padding(end = 8.dp)
            )
            Column(modifier = Modifier) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = if (isShown) password else "•••••••••••",
                    style = MaterialTheme.typography.bodyLarge
                )

            }
        }
        IconButton(onClick = {
            onShowClick()
        }) {
            Icon(
                imageVector = if (!isShown) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                contentDescription = null
            )
        }
    }

}