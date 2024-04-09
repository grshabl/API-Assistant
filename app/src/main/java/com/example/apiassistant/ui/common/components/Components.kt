package com.example.apiassistant.ui.common.components

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.apiassistant.R
import com.example.apiassistant.ui.theme.robotoMedium

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
    label: String = "",
    enabled: Boolean= true
) {
    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        maxLines = 1,
        enabled = enabled
    )
}

@Composable
fun VerticalLine() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.primaryContainer)
    )
}

@Composable
fun HorizontalLine() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.secondaryContainer
                    )
                )
            )
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
        Text(
            text = text,
            color = MaterialTheme.colorScheme.secondary
        )
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
    onClickAddApi: () -> Unit,
    actionAfterRecognize: (String?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(
                vertical = 4.dp,
                horizontal = dimensionResource(id = R.dimen.common_start_padding)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            color = MaterialTheme.colorScheme.secondary,
            text = stringResource(id = R.string.app_name),
            fontSize = 20.sp,
            fontFamily = robotoMedium
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
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
           VoiceRecognitionComponent(
               actionAfterRecognize = actionAfterRecognize
           )
        }
    }
}

@Composable
fun StandartToolbar(
    title: String,
    imageVector: ImageVector = Icons.Default.KeyboardArrowUp,
    clickBack : () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(
                vertical = 4.dp,
                horizontal = dimensionResource(id = R.dimen.common_start_padding)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            color = MaterialTheme.colorScheme.secondary,
            text = title,
            fontSize = 20.sp
        )
        Icon(
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.size_icon))
                .clickable {
                    clickBack()
                },
            imageVector = imageVector,
            contentDescription = stringResource(id = R.string.add_api),
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun VoiceRecognitionComponent(
    actionAfterRecognize: (String?) -> Unit
) {
    var recognizedText : String? by remember { mutableStateOf(null) }
    val context = LocalContext.current

    val hintSpeak = stringResource(id = R.string.hint_speak)
    val textToastPermission = stringResource(id = R.string.permission_voice)

    val resultLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val textResults = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            recognizedText = textResults?.get(0)

            actionAfterRecognize(recognizedText)
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, hintSpeak)

            resultLauncher.launch(intent)
        } else {
            Toast.makeText(context, textToastPermission, Toast.LENGTH_SHORT).show()
        }
    }

    Image(
        modifier = Modifier
            .size(38.dp)
            .clickable(onClick = {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }),
        painter = painterResource(R.drawable.ic_mic),
        contentDescription = stringResource(id = R.string.add_api),
    )
}

@Composable
fun TitleText(text: String) {
    Text(
        modifier = Modifier
            .padding(horizontal = dimensionResource(id = R.dimen.common_start_padding), vertical = 8.dp),
        text = text,
        color = MaterialTheme.colorScheme.onPrimary
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

    Box(modifier = Modifier.padding(top=4.dp)) {
        Text(
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp)
                .shadow(6.dp, shape = RoundedCornerShape(16.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 1.dp, vertical = 1.dp)
                .background(MaterialTheme.colorScheme.background)
                .padding(vertical = 6.dp, horizontal = 12.dp)
                .clickable { expanded = true },
            text = items[selectedIndex].toString(),
            color = MaterialTheme.colorScheme.onPrimary
        )

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