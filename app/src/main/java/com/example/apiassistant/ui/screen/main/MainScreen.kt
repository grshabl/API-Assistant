package com.example.apiassistant.ui.screen.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.apiassistant.R
import com.example.apiassistant.ui.common.animation.AnimatedDestination
import com.example.apiassistant.ui.common.components.ButtonApply
import com.example.apiassistant.ui.common.components.LoadingComponent
import com.example.apiassistant.ui.common.components.ToolbarApp
import com.example.apiassistant.ui.screen.destinations.AddApiScreenDestination
import com.example.apiassistant.ui.screen.destinations.TestApiScreenDestination
import com.example.apiassistant.ui.theme.ApiAssistantTheme
import com.example.domain.api.model.RequestApi
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@AnimatedDestination
@Composable
fun MainScreen(viewModel: MainScreenViewModel = hiltViewModel(),
               navigator: DestinationsNavigator
) {
    viewModel.updateState() // may be optimize it
    observeEffect(
        effect = viewModel.effect.value,
        navigator = navigator,
        actionAfterObserveEffect = {
            viewModel.onAction(MainScreenViewModel.Action.NavigateToOtherScreen)
        }
    )

    LoadingComponent(isLoading = viewModel.state.value.isLoading)

    val onClickApi = { requestApi: RequestApi ->
        viewModel.onAction(MainScreenViewModel.Action.OnClickApi(requestApi))
    }
    val onClickLikeApi = { requestApi: RequestApi ->
        viewModel.onAction(MainScreenViewModel.Action.OnClickLikeApi(requestApi))
    }
    val onClickDeleteApi = { requestApi: RequestApi ->
        viewModel.onAction(MainScreenViewModel.Action.OnClickDeleteApi(requestApi))
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ToolbarApp(
            onClickAddApi = { viewModel.onAction(MainScreenViewModel.Action.OnClickAddApi) }
        )
        VoiceRecognitionComponent(
            actionAfterRecognize = { command -> viewModel.onAction(MainScreenViewModel.Action.RecognizeVoiceCommand(command))}
        )
        ColumnOneCategoryApi(
            titleCategory = stringResource(id = R.string.likes_api),
            listApi = viewModel.getLikesApi(
                listApi = viewModel.state.value.listApi,
            ),
            onClickApi = onClickApi,
            onClickLikeApi = onClickLikeApi,
            onClickDeleteApi = onClickDeleteApi
        )
        ColumnOneCategoryApi(
            titleCategory = stringResource(id = R.string.all_api),
            listApi = viewModel.state.value.listApi,
            onClickApi = onClickApi,
            onClickLikeApi = onClickLikeApi,
            onClickDeleteApi = onClickDeleteApi
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

    ButtonApply(
        text = stringResource(id = R.string.voice_command),
        onClick = {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    )
}

@Composable
fun ColumnOneCategoryApi(
    titleCategory: String,
    listApi: List<RequestApi>,
    onClickApi: (RequestApi) -> Unit,
    onClickLikeApi: (RequestApi) -> Unit,
    onClickDeleteApi: (RequestApi) -> Unit
) {
    LazyColumn {
        item {
            TextCategoryApi(titleCategory)
        }
        items(listApi) { requestApi ->
            CardApi(
                requestApi = requestApi,
                onClickApi = { onClickApi(requestApi) },
                onClickLikeApi = { onClickLikeApi(requestApi) },
                onClickDeleteApi = { onClickDeleteApi(requestApi) },
            )
        }
    }
}

@Composable
fun TextCategoryApi(title: String) {
    Text(text = title)
}

@Composable
fun CardApi(
    requestApi: RequestApi,
    onClickApi: (RequestApi) -> Unit,
    onClickLikeApi: (RequestApi) -> Unit,
    onClickDeleteApi: (RequestApi) -> Unit
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            Log.d("test", "onClickApi")
            onClickApi(requestApi)
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = requestApi.method.name)
            Text(text = requestApi.url)
            Icon(
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size_icon))
                    .clickable(onClick = {
                        onClickDeleteApi(requestApi)
                    }),
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(id = R.string.delete_api),
                tint = MaterialTheme.colorScheme.primary
            )
            Icon(
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size_icon))
                    .clickable(onClick = {
                        onClickLikeApi(requestApi)
                    }),
                imageVector = if (requestApi.isLike) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = stringResource(id = R.string.delete_api),
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

private fun observeEffect(
    effect: MainScreenViewModel.Effect?,
    navigator: DestinationsNavigator,
    actionAfterObserveEffect: (() -> Unit)? = null
) {
    Log.d("test", "observeEffect")
    effect?.let {
        when (it) {
            is MainScreenViewModel.Effect.NavigateToAddApiScreen -> {
                navigator.navigate(AddApiScreenDestination)
            }
            is MainScreenViewModel.Effect.NavigateToTestApiScreen -> {
                navigator.navigate(TestApiScreenDestination(requestApi = it.requestApi))
            }
        }

        actionAfterObserveEffect?.let { function ->
            function()
        }
    }
}

private fun observeVoiceCommand(
    voiceCommand: String?,
    action: ((String) -> Unit)
) {
    voiceCommand?.let { command ->
        action(command)
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ApiAssistantTheme {
//        MainScreen(navigator = EmptyDestinationsNavigator)
    }
}