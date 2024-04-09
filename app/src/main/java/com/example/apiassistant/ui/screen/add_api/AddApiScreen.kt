package com.example.apiassistant.ui.screen.add_api

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.apiassistant.R
import com.example.apiassistant.ui.common.animation.AnimatedDestination
import com.example.apiassistant.ui.common.components.ButtonApply
import com.example.apiassistant.ui.common.components.DropdownMenuInput
import com.example.apiassistant.ui.common.components.InputTextField
import com.example.apiassistant.ui.common.components.LoadingComponent
import com.example.apiassistant.ui.common.components.StandartToolbar
import com.example.apiassistant.ui.common.components.TitleText
import com.example.apiassistant.ui.screen.main.TextCategoryApi
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

    observeEffect(
        effect = viewModel.effect.value,
        navigator = navigator,
        actionAfterObserveEffect = { viewModel.setDefaultEffect() }
    )
    
    LoadingComponent(isLoading = viewModel.state.value.isLoading)

    LazyColumn {
        item {
            StandartToolbar(
                title = stringResource(id = R.string.add_api),
                clickBack = { navigator.popBackStack() }
            )
        }
        item {
            TextCategoryApi(title = stringResource(id = R.string.title_parse_api))
            UrlSwaggerField(
                onClickParse = { urlSwagger ->
                    viewModel.onAction(AddApiViewModel.Action.ParseSwaggerApi(urlSwagger))
                }
            )
        }
        item {
            TextCategoryApi(title = stringResource(id = R.string.title_input_api))
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
                    },
                    labelFirstColumn = stringResource(id = R.string.name_variable),
                    labelSecondColumn = stringResource(id = R.string.type_variable)
                )
//                DoubleTextField(
//                    label1 = stringResource(id = R.string.name_variable),
//                    label2 = stringResource(id = R.string.type_variable),
//                    onValueChange1 = { newValue ->
//                        viewModel.onAction(AddApiViewModel.Action.UpdateNamePathVariable(
//                            index = index,
//                            name = newValue
//                        ))
//                    },
//                    onValueChange2 = { newValue ->
//                        viewModel.onAction(AddApiViewModel.Action.UpdateTypePathVariable(
//                            index = index,
//                            value = newValue
//                        ))
//                    }
//                )
            }
        }
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 1.dp, vertical = 1.dp)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = 6.dp, horizontal = 12.dp)
                    .clickable { viewModel.onAction(AddApiViewModel.Action.AddPathVariable) },
                text = stringResource(id = R.string.add_path_variable),
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
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
            TitleText(text = stringResource(id = R.string.voice_command))
        }
        item {
            InputTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.common_start_padding)),
                text = viewModel.state.value.voiceString ?: "",
                onValueChange = { newValue ->
                    viewModel.onAction(AddApiViewModel.Action.UpdateVoiceCommand(newValue))
                },
                label = stringResource(id = R.string.voice_command)
            )
        }
        item {
            ButtonApply(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.common_start_padding),
                vertical = 6.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val urlSwagger = remember { mutableStateOf("") }

        InputTextField(
            modifier = Modifier
                .wrapContentWidth(),
            text = urlSwagger.value,
            onValueChange = { newValue ->
                urlSwagger.value = newValue
            },
            label = stringResource(id = R.string.hint_swagger)
        )
        Icon(
            modifier = Modifier
                .size(32.dp)
                .clickable {
                    onClickParse(urlSwagger.value)
                },
            imageVector = Icons.Default.Send,
            contentDescription = stringResource(id = R.string.parse_swagger),
            tint = MaterialTheme.colorScheme.primary
        )
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
            modifier = Modifier
                .wrapContentWidth()
                .padding(end = 16.dp),
            text = url,
            onValueChange = onValueChange,
            label = stringResource(id = R.string.hint_url)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoubleTextField(
    modifier: Modifier = Modifier,
    label1: String,
    label2: String,
    onValueChange1: (String) -> Unit,
    onValueChange2: (String) -> Unit
) {
    val borderColor = Color.Gray
    val dividerColor = Color.LightGray

    Column(modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical=8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, borderColor),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextField(
                modifier = Modifier.weight(1f),
                value = "",
                onValueChange = onValueChange1,
                label = { Text(label1) },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .height(30.dp)
                    .background(dividerColor)
            )

            TextField(
                modifier = Modifier.weight(1f).background(MaterialTheme.colorScheme.background),
                value = "",
                onValueChange = onValueChange2,
                label = { Text(label2) },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }
    }
}
@Composable
fun PathVariableComponent(
    pathParam: RequestPathParam,
    labelFirstColumn: String,
    labelSecondColumn: String,
    onNameChange: (newValue: String) -> Unit,
    onValueChange: (newValue: String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.common_start_padding)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        InputTextField(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(end = 8.dp),
            text = pathParam.name,
            onValueChange = onNameChange,
            label = labelFirstColumn
        )
        InputTextField(
            modifier = Modifier.wrapContentWidth(),
            text = pathParam.type,
            label = labelSecondColumn,
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.common_start_padding)),
        text = textBody,
        label = stringResource(id = R.string.body_json),
        onValueChange = onValueChange
    )
}

@Composable
private fun observeEffect(
    effect: AddApiViewModel.Effect?,
    navigator: DestinationsNavigator,
    actionAfterObserveEffect: (() -> Unit)? = null
) {
    Log.d("test", "observeEffect")
    effect?.let {
        when (it) {
            AddApiViewModel.Effect.GoBack -> { navigator.popBackStack() }
            AddApiViewModel.Effect.ShowErrorParse -> {
                Toast.makeText(
                    LocalContext.current,
                    stringResource(id = R.string.error_parse_swagger),
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
fun AddApiScreenPreview() {
    ApiAssistantTheme {
        AddApiScreen(navigator = EmptyDestinationsNavigator)
    }
}