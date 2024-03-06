package com.example.apiassistant.ui.screen.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.apiassistant.R
import com.example.apiassistant.model.auth.AuthState
import com.example.apiassistant.ui.common.animation.AnimatedDestination
import com.example.apiassistant.ui.common.components.AnimatedImage
import com.example.apiassistant.ui.common.components.ButtonApply
import com.example.apiassistant.ui.common.components.PasswordTextField
import com.example.apiassistant.ui.screen.destinations.AuthScreenDestination
import com.example.apiassistant.ui.screen.destinations.MainScreenDestination
import com.example.apiassistant.ui.theme.PINLightTheme
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@RootNavGraph(start = true)
@AnimatedDestination
@Composable
fun AuthScreen(viewModel: AuthViewModel = viewModel(),
               navigator: DestinationsNavigator
) {
    val viewModel: AuthViewModel = viewModel()
    val state = viewModel.state.value

    observeStateUI(state, navigator)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedImagePin()
            PinTextField(
                text = state.pin,
                onValueChange = { newValue ->
                    viewModel.setPin(newValue)
                }
            )
            ButtonAuth(viewModel)
        }
    }
}

@Composable
private fun AnimatedImagePin() {
    AnimatedImage(
        modifier = Modifier.size(dimensionResource(id = R.dimen.size_gif_pin)),
        idResources = R.drawable.pin_anim,
        contentDescription = stringResource(id = R.string.description_gif_pin)
    )
}

@Composable
private fun PinTextField(text: String = "",
                 onValueChange: (String) -> Unit) {
    PasswordTextField(
        modifier = Modifier,
        text = text,
        onValueChange = onValueChange,
        label = stringResource(id = R.string.pin)
    )
}

@Composable
private fun ButtonAuth(viewModel: AuthViewModel) {
    ButtonApply(
        modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_top_button_auth)),
        onClick = { viewModel.onAction(AuthViewModel.Action.OnClickButtonAuth) },
        text = stringResource(id = R.string.activate_pin)
    )
}

@Preview(showBackground = true)
@Composable
private fun AuthScreenPreview() {
    PINLightTheme {
        AuthScreen(navigator = EmptyDestinationsNavigator)
    }
}

private fun observeStateUI(
    state: AuthState,
    navigator: DestinationsNavigator
) {
    if (state.isCorrectPin) {
        navigator.navigate(MainScreenDestination()) {
           popUpTo(AuthScreenDestination.route) {
               inclusive = true
           }
        }
    }
}