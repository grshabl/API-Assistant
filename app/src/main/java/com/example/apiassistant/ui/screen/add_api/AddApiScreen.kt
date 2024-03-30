package com.example.apiassistant.ui.screen.add_api

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.apiassistant.R
import com.example.apiassistant.ui.common.animation.AnimatedDestination
import com.example.apiassistant.ui.common.components.ButtonApply
import com.example.apiassistant.ui.common.components.ButtonCard
import com.example.apiassistant.ui.common.components.DropdownMenuInput
import com.example.apiassistant.ui.common.components.InputTextField
import com.example.apiassistant.ui.common.components.TitleText
import com.example.apiassistant.ui.theme.ApiAssistantTheme
import com.example.domain.api.enums.MethodRequest
import com.example.domain.api.model.RequestPathParam
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@AnimatedDestination
@Composable
fun AddApiScreen(
    viewModel: AddApiViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    LazyColumn {
        item {
            UrlSwaggerField(
                onClickParse = { viewModel.onAction(AddApiViewModel.Action.ParseSwaggerApi) }
            )
        }
        item {
            UrlApiField(
                url = viewModel.state.value.url,
                methodsRequest = viewModel.getMethodsRequest(),
                onValueChange = { newValue: String ->
                    viewModel.onAction(AddApiViewModel.Action.SetUrl(newValue))
                },
                onMethodChange = { method: MethodRequest ->
                    viewModel.onAction(AddApiViewModel.Action.ChangeMethodRequest(method))
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
                    onNameChange = { newValue ->
                        viewModel.onAction(AddApiViewModel.Action.UpdateNamePathVariable(
                            index = index,
                            name = newValue
                        ))
                    },
                    onValueChange = { newValue ->
                        viewModel.onAction(AddApiViewModel.Action.UpdateTypePathVariable(
                            index = index,
                            value = newValue
                        ))
                    }
                )
            }
        }
        item {
            ButtonCard(
                text = stringResource(id = R.string.add_path_variable),
                onClick = { viewModel.onAction(AddApiViewModel.Action.AddPathVariable) }
            )
        }
        item {
            TitleText(text = stringResource(id = R.string.body_json))
        }
        item {
            BodyJsonComponent(
                textBody = viewModel.state.value.body ?: "",
                onValueChange = { newValue ->
                    viewModel.onAction(AddApiViewModel.Action.UpdateBodyJson(newValue))
                }
            )
        }
        item {
            InputTextField(
                text = viewModel.state.value.voiceString ?: "",
                onValueChange = { newValue ->
                    viewModel.onAction(AddApiViewModel.Action.UpdateVoiceCommand(newValue))
                },
                label = stringResource(id = R.string.voice_command)
            )
        }
        item {
            ButtonApply(
                text = stringResource(id = R.string.save),
                onClick = {
                    viewModel.onAction(AddApiViewModel.Action.SaveApi)
                }
            )
        }
    }
}

@Composable
fun UrlSwaggerField(
    onClickParse: (url: String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        val urlSwagger = remember { mutableStateOf("") }

        InputTextField(
            text = urlSwagger.value,
            onValueChange = { newValue ->
                urlSwagger.value = newValue
            },
            label = stringResource(id = R.string.hint_swagger)
        )
        Button(onClick = { onClickParse(urlSwagger.value) }) {
            Text(text = stringResource(id = R.string.parse_swagger))
        }
    }
}

@Composable
fun UrlApiField(
    methodsRequest: Array<MethodRequest>,
    url: String,
    onValueChange: (url: String) -> Unit,
    onMethodChange: (method: MethodRequest) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DropdownMenuInput(
            items = methodsRequest,
            onClickItem = onMethodChange
        )
        InputTextField(
            text = url,
            onValueChange = onValueChange
        )
    }
}

@Composable
fun PathVariableComponent(
    pathParam: RequestPathParam,
    onNameChange: (newValue: String) -> Unit,
    onValueChange: (newValue: String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        InputTextField(
            text = pathParam.name,
            onValueChange = onNameChange
        )
        InputTextField(
            text = pathParam.value,
            label = "${pathParam.name}: ${pathParam.type}",
            onValueChange = onValueChange
        )
    }
}

@Composable
fun BodyJsonComponent(
    textBody: String,
    onValueChange: (newValue: String) -> Unit
) {
    InputTextField(
        text = textBody,
        label = stringResource(id = R.string.body_json),
        onValueChange = onValueChange
    )
}

@Preview(showBackground = true)
@Composable
fun AddApiScreenPreview() {
    ApiAssistantTheme {
        AddApiScreen(navigator = EmptyDestinationsNavigator)
    }
}