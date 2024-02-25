package br.dev.singular.overview.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.LiveData
import br.dev.singular.overview.di.IsOnline
import br.dev.singular.overview.ui.navigation.NavController
import br.dev.singular.overview.ui.theme.AppTheme
import br.dev.singular.overview.ui.theme.PrimaryBackground
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    @IsOnline
    lateinit var isOnline: LiveData<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    OverviewApp(isOnline = isOnline.observeAsState(initial = true).value)
                }
            }
        }
    }
}

@Composable
fun OverviewApp(isOnline: Boolean) {
    Box(Modifier.fillMaxSize().background(PrimaryBackground)) {
        NavController()
        Column(Modifier.fillMaxWidth().align(Alignment.BottomCenter)) {
            // OfflineSnackBar(isNotOnline = isOnline.not())
        }
    }
}
