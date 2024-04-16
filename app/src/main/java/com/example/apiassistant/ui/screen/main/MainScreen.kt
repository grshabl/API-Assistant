package com.example.apiassistant.ui.screen.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.apiassistant.R
import com.example.apiassistant.ui.common.animation.AnimatedDestination
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
            onClickAddApi = { viewModel.onAction(MainScreenViewModel.Action.OnClickAddApi) },
            actionAfterRecognize = { command -> viewModel.onAction(MainScreenViewModel.Action.RecognizeVoiceCommand(command))}
        )
        LazyColumn {
            item {
                key ("likes api") {
                    val likesApi = viewModel.getLikesApi(
                        listApi = viewModel.state.value.listApi,
                    )
                    if (likesApi.isNotEmpty()) {
                        ColumnOneCategoryApi(
                            titleCategory = stringResource(id = R.string.likes_api),
                            listApi = viewModel.getLikesApi(
                                listApi = viewModel.state.value.listApi,
                            ),
                            onClickApi = onClickApi,
                            onClickLikeApi = onClickLikeApi,
                            onClickDeleteApi = onClickDeleteApi
                        )
                    }
                }
            }
            item {
                key("all api") {
                    ColumnOneCategoryApi(
                        titleCategory = stringResource(id = R.string.all_api),
                        listApi = viewModel.state.value.listApi,
                        onClickApi = onClickApi,
                        onClickLikeApi = onClickLikeApi,
                        onClickDeleteApi = onClickDeleteApi
                    )
                }
            }
        }
    }
    LoadingComponent(isLoading = viewModel.state.value.isLoading)
}

@Composable
fun ColumnOneCategoryApi(
    titleCategory: String,
    listApi: List<RequestApi>,
    onClickApi: (RequestApi) -> Unit,
    onClickLikeApi: (RequestApi) -> Unit,
    onClickDeleteApi: (RequestApi) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .shadow(8.dp, shape = RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer, shape = RoundedCornerShape(8.dp))
//            .padding(horizontal = 1.dp, vertical = 1.dp)
//            .background(MaterialTheme.colorScheme.background)
    ) {
        TextCategoryApi(titleCategory)
        listApi.forEach { requestApi ->
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
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.secondaryContainer
                    ),
                )
            )
            .padding(
                horizontal = dimensionResource(id = R.dimen.common_start_padding),
                vertical = 10.dp
            ),
        text = title,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onPrimary
    )
}

@Composable
fun CardApi(
    requestApi: RequestApi,
    onClickApi: (RequestApi) -> Unit,
    onClickLikeApi: (RequestApi) -> Unit,
    onClickDeleteApi: (RequestApi) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.secondaryContainer,
                    ),
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .shadow(4.dp, shape = RoundedCornerShape(8.dp))
//            .border(width = 1.dp, color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 1.dp, vertical = 1.dp)
            .background(MaterialTheme.colorScheme.onBackground)
            .padding(vertical = 6.dp)
            .clickable {
                Log.d("test", "onClickApi")
                onClickApi(requestApi)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.2f)
                    .padding(8.dp),
                text = requestApi.method.name,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(4.dp),
                text = requestApi.url,
                color = MaterialTheme.colorScheme.onPrimary,
                maxLines = 1,
                fontSize = 13.sp,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
            )
        }
        Row(
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(36.dp)
                    .clickable(onClick = {
                        onClickDeleteApi(requestApi)
                    }),
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(id = R.string.delete_api),
                tint = MaterialTheme.colorScheme.primary
            )
            Icon(
                modifier = Modifier
                    .size(36.dp)
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
                navigator.navigate(AddApiScreenDestination(requestApi = null))
            }
            is MainScreenViewModel.Effect.NavigateToTestApiScreen -> {
                navigator.navigate(TestApiScreenDestination(
                    requestApi = it.requestApi,
                    detectedVoiceCommand = it.detectedVoiceCommand
                ))
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