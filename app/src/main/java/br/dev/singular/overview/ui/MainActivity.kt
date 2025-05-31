package br.dev.singular.overview.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import br.dev.singular.overview.remote.RemoteConfig
import br.dev.singular.overview.ui.navigation.NavController
import br.dev.singular.overview.ui.theme.AppTheme
import br.dev.singular.overview.ui.theme.PrimaryBackground
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var adsManager: RemoteConfig<Boolean>
    private val showAds: Boolean by lazy { adsManager.execute() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppTheme {
                Scaffold(
                    containerColor = PrimaryBackground,
                    contentWindowInsets = WindowInsets(0),
                    bottomBar = {
                        BottomNavigationBar(navController, showAds)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(PrimaryBackground)
                        .padding(WindowInsets.systemBars.asPaddingValues()),
                ) { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
                        NavController(navController)
                    }
                }
            }
        }
    }
}
