package com.example.apiassistant.ui.screen.main

import android.util.Log
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

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
        // LoadingComponent
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

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ApiAssistantTheme {
        MainScreen(navigator = EmptyDestinationsNavigator)
    }
}