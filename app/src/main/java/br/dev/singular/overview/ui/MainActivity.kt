package br.dev.singular.overview.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import br.dev.singular.overview.di.DisplayAds
import br.dev.singular.overview.remote.RemoteConfig
import br.dev.singular.overview.ui.navigation.NavController
import br.dev.singular.overview.ui.theme.AppTheme
import br.dev.singular.overview.ui.theme.PrimaryBackground
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    @DisplayAds
    lateinit var adsManager: RemoteConfig<Boolean>
    private val showAds: Boolean by lazy { adsManager.execute() }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppTheme {

                var edgeToEdge by rememberSaveable { mutableStateOf(false) }

                LaunchedEffect(edgeToEdge) {
                    if (edgeToEdge) {
                        enableEdgeToEdge()
                    }
                }

                val setEdgeToEdge: (Boolean) -> Unit = { enabled ->
                    edgeToEdge = enabled
                }

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
                    NavController(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        setEdgeToEdge = setEdgeToEdge,
                        showAds =  showAds
                    )
                }
            }
        }
    }
}
