package io.github.leoallvez.take.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import io.github.leoallvez.take.ui.home.HomeScreen
import io.github.leoallvez.take.ui.home.HomeViewModel
import io.github.leoallvez.take.ui.splash.SplashScreen
import io.github.leoallvez.take.ui.splash.SplashViewModel
import io.github.leoallvez.take.ui.theme.TakeTheme

@ExperimentalPagerApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TakeTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    TakeApp()
                }
            }
        }
    }
}

@ExperimentalPagerApi
@Composable
fun TakeApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(route = Screen.Home.route) {
            HomeScreen()
        }
    }
}
