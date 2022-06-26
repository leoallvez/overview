package io.github.leoallvez.take.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.lifecycle.LiveData
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import io.github.leoallvez.take.di.IsOnline
import io.github.leoallvez.take.ui.home.HomeScreen
import io.github.leoallvez.take.ui.splash.SplashScreen
import io.github.leoallvez.take.ui.theme.TakeTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.leoallvez.take.R
import io.github.leoallvez.take.AnalyticsLogger
import io.github.leoallvez.take.Logger
import javax.inject.Inject

@ExperimentalPagerApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    @IsOnline
    lateinit var isOnline: LiveData<Boolean>

    @Inject
    lateinit var analyticsLog: AnalyticsLogger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TakeTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    TakeApp(
                        isOnline = isOnline.observeAsState(true).value,
                        analyticsLog = analyticsLog
                    )
                }
            }
        }
    }
}

@ExperimentalPagerApi
@Composable
fun TakeApp(isOnline: Boolean, analyticsLog: AnalyticsLogger) {
    Box {
        NavController(analyticsLog)
        OfflineSnackbar(
            isNotOnline = isOnline.not(),
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}

@ExperimentalPagerApi
@Composable
fun NavController(logger: Logger) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(nav = navController, logger)
        }
        composable(route = Screen.Home.route) {
            HomeScreen(logger = logger)
        }
    }
}

@Composable
fun OfflineSnackbar(isNotOnline: Boolean, modifier :Modifier) {
    if (isNotOnline) {
        Snackbar(
            modifier = modifier.padding(8.dp)
        ) {
            Text(text = stringResource(id = R.string.app_offline_msg))
        }
    }
}
