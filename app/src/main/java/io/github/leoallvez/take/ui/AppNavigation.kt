package io.github.leoallvez.take.ui

sealed class Screen(val route: String, val name: String) {
    object Splash: Screen(route = "splash_screen", name = "SplashScreen")
    object Home: Screen(route = "home_screen", name = "HomeScreen")
}