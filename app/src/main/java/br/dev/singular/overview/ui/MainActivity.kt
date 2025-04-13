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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.compose.rememberNavController
import br.dev.singular.overview.R
import br.dev.singular.overview.ui.navigation.NavController
import br.dev.singular.overview.ui.theme.AppTheme
import br.dev.singular.overview.ui.theme.PrimaryBackground
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppTheme {
                Scaffold(
                    contentWindowInsets = WindowInsets(0),
                    bottomBar = { BottomNavigationBar(navController) },
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
