package br.dev.singular.overview.presentation.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavBackStackEntry

private typealias AnimatedTransition = AnimatedContentTransitionScope<NavBackStackEntry>

object AnimationDurations {
    const val SMALL = 375
    const val LONG = 750
}

fun AnimatedTransition.rightExitTransition(duration: Int = AnimationDurations.SMALL) =
    slideExit(SlideDirection.End, duration)

fun slideInFromBottom(duration: Int = AnimationDurations.LONG) =
    slideInVertically(
        initialOffsetY = { fullHeight -> fullHeight },
        animationSpec = tween(duration)
    )

fun slideOutToBottom(duration: Int = AnimationDurations.LONG) =
    slideOutVertically(
        targetOffsetY = { fullHeight -> fullHeight },
        animationSpec = tween(duration)
    )

fun fadeInTransition(duration: Int = AnimationDurations.LONG) =
    fadeIn(animationSpec = tween(duration))

private fun AnimatedTransition.slideExit(
    direction: SlideDirection,
    duration: Int = AnimationDurations.SMALL
) = slideOutOfContainer(direction, tween(duration))

private fun AnimatedTransition.slideEnter(
    direction: SlideDirection,
    duration: Int = AnimationDurations.SMALL
) = slideIntoContainer(direction, tween(duration))
