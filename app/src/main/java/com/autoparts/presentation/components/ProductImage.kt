package com.autoparts.presentation.components

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ProductImage(
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    placeholderSize: androidx.compose.ui.unit.Dp = 48.dp
) {
    var isError by remember { mutableStateOf(false) }
    val decodedBitmap = remember(imageUrl) { decodeBase64Image(imageUrl) }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        when {
            decodedBitmap != null -> {
                Image(
                    bitmap = decodedBitmap.asImageBitmap(),
                    contentDescription = contentDescription,
                    modifier = Modifier.matchParentSize(),
                    contentScale = contentScale
                )
            }
            shouldShowAsyncImage(imageUrl) -> {
                if (!isError) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = contentDescription,
                        modifier = Modifier.matchParentSize(),
                        contentScale = contentScale,
                        onError = { isError = true }
                    )
                } else {
                    PlaceholderIcon(placeholderSize)
                }
            }
            else -> PlaceholderIcon(placeholderSize)
        }
    }
}

private fun decodeBase64Image(imageUrl: String?): android.graphics.Bitmap? {
    return imageUrl?.takeIf { it.startsWith("data:image") }?.let { url ->
        try {
            val base64String = if (url.contains(",")) url.split(",")[1] else url
            val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        } catch (e: Exception) {
            android.util.Log.e("ProductImage", "Error decodificando Base64: ${e.message}")
            null
        }
    }
}

private fun shouldShowAsyncImage(imageUrl: String?): Boolean {
    return !imageUrl.isNullOrBlank() && !imageUrl.startsWith("data:image")
}

@Composable
private fun PlaceholderIcon(size: androidx.compose.ui.unit.Dp) {
    Icon(
        imageVector = Icons.Default.ShoppingCart,
        contentDescription = null,
        modifier = Modifier.size(size),
        tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
    )
}