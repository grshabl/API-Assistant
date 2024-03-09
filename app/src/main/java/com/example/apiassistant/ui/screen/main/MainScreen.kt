package com.example.apiassistant.ui.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.apiassistant.ui.common.animation.AnimatedDestination
import com.example.apiassistant.ui.theme.ApiAssistantTheme
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@AnimatedDestination
@Composable
fun MainScreen(viewModel: MainScreenViewModel = hiltViewModel(),
               navigator: DestinationsNavigator
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Главная страница")
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