package br.dev.singular.overview.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.tween
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

fun AnimatedTransition.upExitTransition(duration: Int = AnimationDurations.SMALL) =
    slideExit(SlideDirection.Up, duration)

fun AnimatedTransition.upEnterTransition(duration: Int = AnimationDurations.SMALL) =
    slideEnter(SlideDirection.Up, duration)

fun AnimatedTransition.downEnterTransition(duration: Int = AnimationDurations.SMALL) =
    slideEnter(SlideDirection.Down, duration)

fun AnimatedTransition.downExitTransition(duration: Int = AnimationDurations.SMALL) =
    slideExit(SlideDirection.Down, duration)

fun AnimatedTransition.slideInFromBottom(duration: Int = AnimationDurations.LONG) =
    slideInVertically(
        initialOffsetY = { fullHeight -> fullHeight },
        animationSpec = tween(duration)
    )

fun AnimatedTransition.slideOutToBottom(duration: Int = AnimationDurations.LONG) =
    slideOutVertically(
        targetOffsetY = { fullHeight -> fullHeight },
        animationSpec = tween(duration)
    )

private fun AnimatedTransition.slideExit(
    direction: SlideDirection,
    duration: Int = AnimationDurations.SMALL
) = slideOutOfContainer(direction, tween(duration))

private fun AnimatedTransition.slideEnter(
    direction: SlideDirection,
    duration: Int = AnimationDurations.SMALL
) = slideIntoContainer(direction, tween(duration))
