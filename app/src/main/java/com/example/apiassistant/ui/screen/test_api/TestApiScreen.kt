package com.example.apiassistant.ui.screen.test_api

import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.apiassistant.R
import com.example.apiassistant.ui.common.animation.AnimatedDestinationStyle
import com.example.apiassistant.ui.common.components.ButtonApply
import com.example.apiassistant.ui.common.components.HorizontalLine
import com.example.apiassistant.ui.common.components.LoadingComponent
import com.example.apiassistant.ui.common.components.StandartToolbar
import com.example.apiassistant.ui.common.components.TitleText
import com.example.apiassistant.ui.screen.add_api.BodyJsonComponent
import com.example.apiassistant.ui.screen.add_api.PathVariableComponent
import com.example.apiassistant.ui.screen.add_api.UrlApiField
import com.example.apiassistant.ui.screen.destinations.AddApiScreenDestination
import com.example.apiassistant.ui.screen.main.TextCategoryApi
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
        factory.create(
            requestApi = navArgs.requestApi,
            detectedVoiceCommand = navArgs.detectedVoiceCommand
        )
    }

    observeEffect(
        effect = viewModel.effect.value,
        actionAfterObserveEffect = { viewModel.resetEffect() }
    )

    Log.d("test", "voice command = ${navArgs.detectedVoiceCommand}")

//    BackPressHandler(
//        onBackPressed = {
//            viewModel.updateRequestApi()
//            navigator.popBackStack()
//        }
//    )

    LazyColumn {
        item {
            StandartToolbar(
                title = stringResource(id = R.string.test_api),
                clickBack = { navigator.navigate(AddApiScreenDestination(requestApi = viewModel.requestApi)) },
                imageVector = Icons.Default.Create
            )
        }
        item {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .shadow(8.dp, shape = RoundedCornerShape(8.dp))
                    .background(
                        MaterialTheme.colorScheme.tertiaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(bottom = 9.dp)
            ) {
                TextCategoryApi(title = stringResource(id = R.string.title_test_api))
                UrlApiField(
                    url = viewModel.state.value.url,
                    methodsRequest = viewModel.getMethodsRequest(),
                    selectedItem = viewModel.state.value.method,
                    onValueChange = { newValue: String ->
                        viewModel.onAction(TestApiViewModel.Action.SetUrl(newValue))
                    },
                    onMethodChange = { method: MethodRequest ->
                        viewModel.onAction(TestApiViewModel.Action.ChangeMethodRequest(method))
                    }
                )
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp))
                HorizontalLine()

                if (!viewModel.state.value.pathParams.isNullOrEmpty()) {
                    val listVariables = viewModel.state.value.pathParams!!

                    TitleText(text = stringResource(id = R.string.path_variables))
                    listVariables.forEachIndexed { index, item ->
                        PathVariableComponent(
                            textFirstColumn = item.name,
                            textSecondColumn = item.value,
                            onNameChange = {},
                            onValueChange = { newValue ->
                                viewModel.onAction(
                                    TestApiViewModel.Action.UpdateValuePathVariable(
                                        index = index,
                                        value = newValue
                                    ))
                            },
                            labelFirstColumn = stringResource(id = R.string.name_variable),
                            labelSecondColumn = item.type,
                            enabledFirstColumn = false
                        )
                    }

                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp))
                    HorizontalLine()
                }

                TitleText(text = stringResource(id = R.string.body_json))
                BodyJsonComponent(
                    textBody = viewModel.state.value.body ?: "",
                    onValueChange = { newValue ->
                        viewModel.onAction(TestApiViewModel.Action.UpdateBodyJson(newValue))
                    }
                )
                if (viewModel.state.value.response != null) {
                    ResponseField(response = viewModel.getResponseText(viewModel.state.value.response))
                }
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
    LoadingComponent(isLoading = viewModel.state.value.isLoading)
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

@Composable
private fun observeEffect(
    effect: TestApiViewModel.Effect?,
    actionAfterObserveEffect: (() -> Unit)? = null
) {
    Log.d("test", "observeEffect")
    effect?.let {
        when (it) {
            TestApiViewModel.Effect.ShowToast -> {
                Toast.makeText(
                    LocalContext.current,
                    stringResource(id = R.string.error_recognize_command),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        actionAfterObserveEffect?.let { function ->
            function()
        }
    }
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
data class TestApiNavArgs(
    val requestApi: RequestApi,
    val detectedVoiceCommand: String? = null
): Parcelable