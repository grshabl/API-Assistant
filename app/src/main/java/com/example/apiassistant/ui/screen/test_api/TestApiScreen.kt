package com.example.apiassistant.ui.screen.test_api

import android.os.Parcelable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.apiassistant.R
import com.example.apiassistant.ui.common.animation.AnimatedDestinationStyle
import com.example.apiassistant.ui.common.components.ButtonApply
import com.example.apiassistant.ui.common.components.LoadingComponent
import com.example.apiassistant.ui.common.components.StandartToolbar
import com.example.apiassistant.ui.common.components.TitleText
import com.example.apiassistant.ui.screen.add_api.BodyJsonComponent
import com.example.apiassistant.ui.screen.add_api.PathVariableComponent
import com.example.apiassistant.ui.screen.add_api.UrlApiField
import com.example.apiassistant.ui.theme.ApiAssistantTheme
import com.example.domain.api.enums.MethodRequest
import com.example.domain.api.model.RequestApi
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.android.parcel.Parcelize

@Destination(
    style = AnimatedDestinationStyle::class,
    navArgsDelegate = TestApiNavArgs::class
)
@Composable
fun TestApiScreen(
    navigator: DestinationsNavigator,
    navArgs: TestApiNavArgs
) {
    val viewModel : TestApiViewModel = hiltViewModel<TestApiViewModel, TestApiViewModel.TestApiViewModelFactory> { factory ->
        factory.create(navArgs.requestApi)
    }

    LoadingComponent(isLoading = viewModel.state.value.isLoading)

    LazyColumn {
        item {
            StandartToolbar(
                title = stringResource(id = R.string.test_api),
                clickBack = { navigator.popBackStack() } )
        }
        item {
            UrlApiField(
                url = viewModel.state.value.url,
                methodsRequest = viewModel.getMethodsRequest(),
                onValueChange = { newValue: String ->
                    viewModel.onAction(TestApiViewModel.Action.SetUrl(newValue))
                },
                onMethodChange = { method: MethodRequest ->
                    viewModel.onAction(TestApiViewModel.Action.ChangeMethodRequest(method))
                }
            )
        }
        item {
            TitleText(text = stringResource(id = R.string.path_variables))
        }
        viewModel.state.value.pathParams?.let {
            itemsIndexed(it) { index, item ->
                PathVariableComponent(
                    pathParam = item,
                    onNameChange = {},
                    onValueChange = { newValue ->
                        viewModel.onAction(
                            TestApiViewModel.Action.UpdateValuePathVariable(
                            index = index,
                            value = newValue
                        ))
                    },
                    labelFirstColumn = stringResource(id = R.string.name_variable),
                    labelSecondColumn = stringResource(id = R.string.value_variable)
                )
            }
        }
        item {
            TitleText(text = stringResource(id = R.string.body_json))
        }
        item {
            BodyJsonComponent(
                textBody = viewModel.state.value.body ?: "",
                onValueChange = { newValue ->
                    viewModel.onAction(TestApiViewModel.Action.UpdateBodyJson(newValue))
                }
            )
        }
        item {
            if (viewModel.getResponseText(viewModel.state.value.response).isNotEmpty()) {
                ResponseField(response = viewModel.getResponseText(viewModel.state.value.response))
            }
        }
        item {
            ButtonApply(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 16.dp, horizontal = dimensionResource(
                            id = R.dimen.common_start_padding
                        )
                    ),
                text = stringResource(id = R.string.request),
                onClick = {
                    viewModel.onAction(TestApiViewModel.Action.RequestApi)
                }
            )
        }
    }
}

@Composable
fun ResponseField(
    response: String
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.common_start_padding))
            .border(width = 1.dp, color = MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        text = response,
        color = MaterialTheme.colorScheme.onPrimary,
        fontSize = 11.sp
    )
}

@Preview(showBackground = true)
@Composable
fun TestApiScreenPreview() {
    ApiAssistantTheme {
        TestApiScreen(
            navigator = EmptyDestinationsNavigator,
            navArgs = TestApiNavArgs(
                requestApi = RequestApi(
                    method = MethodRequest.GET,
                    url = "www.leningrad.spb.ru"
                )
            )
        )
    }
}

@Parcelize
data class TestApiNavArgs(val requestApi: RequestApi): Parcelable