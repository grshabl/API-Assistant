package com.example.apiassistant.ui.common.components

import android.os.Build.VERSION.SDK_INT
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.apiassistant.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    text: String = "",
    onValueChange: (String) -> Unit = {},
    label: String = ""
) {
    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputTextField(
    modifier: Modifier = Modifier,
    text: String = "",
    onValueChange: (String) -> Unit = {},
    label: String = ""
) {
    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = onValueChange,
        label = { Text(label) }
    )
}

@Composable
fun ButtonApply(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = {}
) {
    Button(
        modifier = modifier,
        onClick = onClick)
    {
        Text(text = text)
    }
}

@Composable
fun ButtonCard(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = {}
) {
    Button(
        modifier = modifier,
        onClick = onClick
    ) {
        Card {
            Text(text = text)
        }
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

@Composable
fun ToolbarApp(
    onClickAddApi: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.common_start_padding)), 
            text = stringResource(id = R.string.app_name))
        Icon(
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.size_icon))
                .clickable(onClick = {
                    onClickAddApi()
                }),
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(id = R.string.add_api),
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun TitleText(text: String) {
    Text(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.common_start_padding)),
        text = text
    )
}

@Composable
fun LoadingComponent(isLoading: Boolean) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(enabled = false, onClick = {})
        ) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun <T> DropdownMenuInput(
    items: Array<T>,
    onClickItem: (item: T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }

    Box {
        Button(
            onClick = { expanded = true }
        ) {
            Text(text = items[selectedIndex].toString())
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = { Text(text = item.toString()) },
                    onClick = {
                        selectedIndex = index
                        expanded = false
                        onClickItem(item)
                    }
                )
            }
        }
    }
}

@Composable
fun PermissionComponent(
    permission: String,
    onClick: () -> Unit,
    content: @Composable() (RowScope.() -> Unit)
) {
    var isPermissionGranted by rememberSaveable { mutableStateOf(false) }
    val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        isPermissionGranted = isGranted
        if (isGranted) {
            onClick()
        }
    }
//    DisposableEffect(key1 = permission) {
//        requestPermissionLauncher.launch(permission)
//        onDispose {  }
//    }

    Row(
        modifier = Modifier.clickable {
            requestPermissionLauncher.launch(permission)
        },
        content = content
    )
}