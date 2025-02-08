package br.dev.singular.overview.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

private typealias AnimatedTransition = AnimatedContentTransitionScope<NavBackStackEntry>

object AnimationDurations {
    const val SMALL = 375
    const val LONG = 750
}

fun AnimatedTransition.rightExitTransition(duration: Int = AnimationDurations.SMALL) =
    slideExit(SlideDirection.End, duration)

fun AnimatedTransition.upExitTransition(duration: Int = AnimationDurations.SMALL) =
    slideExit(SlideDirection.Up, duration)

fun AnimatedTransition.downEnterTransition(duration: Int = AnimationDurations.SMALL) =
    slideEnter(SlideDirection.Down, duration)

private fun AnimatedTransition.slideExit(
    direction: SlideDirection,
    duration: Int = AnimationDurations.SMALL
) = slideOutOfContainer(direction, tween(duration))

private fun AnimatedTransition.slideEnter(
    direction: SlideDirection,
    duration: Int = AnimationDurations.SMALL
) = slideIntoContainer(direction, tween(duration))
