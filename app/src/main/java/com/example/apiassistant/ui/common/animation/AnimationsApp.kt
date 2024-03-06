package com.example.apiassistant.ui.common.animation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn

import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyle

@Destination(style = AnimatedDestinationStyle::class)
annotation class AnimatedDestination

object AnimatedDestinationStyle : DestinationStyle.Animated {
    override fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(): EnterTransition =
        AnimationsApp.enterTransition

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(): ExitTransition =
        AnimationsApp.exitTransition

}

private class AnimationsApp {
    companion object {
        // Navigation
        @JvmStatic
        val enterTransition = fadeIn(
            animationSpec = tween(
                300, easing = LinearEasing
            )
        )

        @JvmStatic
        val exitTransition = fadeOut(
            animationSpec = tween(
                300, easing = LinearEasing
            )
        )
    }
}