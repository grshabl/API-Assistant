package com.example.apiassistant.ui.common.components

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(modifier: Modifier = Modifier,
                      text: String = "",
                      onValueChange: (String) -> Unit = {},
                      label: String = "") {
    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun ButtonApply(modifier: Modifier = Modifier,
                text: String = "",
                onClick: () -> Unit = {}) {
    Button(
        modifier = modifier,
        onClick = onClick) {
        Text(text = text)
    }
}

@Composable
fun AnimatedImage(
    modifier: Modifier = Modifier,
    idResources: Int? = null,
    contentDescription: String? = null
    ) {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .crossfade(true)
        .build()

    Image(
        painter = rememberAsyncImagePainter(idResources, imageLoader),
        contentDescription = contentDescription,
        modifier = modifier
    )
}