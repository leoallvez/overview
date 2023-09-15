package dev.com.singular.overview.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import dev.com.singular.overview.ui.theme.AppTheme
import br.dev.singular.overview.BuildConfig
import dagger.hilt.android.AndroidEntryPoint
import dev.com.singular.overview.di.IsOnline
import dev.com.singular.overview.ui.navigation.NavController
import dev.com.singular.overview.ui.theme.Gray
import dev.com.singular.overview.ui.theme.PrimaryBackground
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
                Surface(color = MaterialTheme.colors.background) {
                    OverviewApp(isOnline = isOnline.observeAsState(initial = true).value)
                }
            }
        }
    }
}

@Composable
fun OverviewApp(isOnline: Boolean) {
    Box {
        NavController()
        Column(Modifier.align(Alignment.BottomCenter)) {
            OfflineSnackBar(isNotOnline = isOnline.not())
            AppVersion()
        }
    }
}

@Composable
fun AppVersion() {
    Text(
        text = "v${BuildConfig.VERSION_NAME}",
        color = Gray,
        textAlign = TextAlign.Center,
        fontSize = 12.sp,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = PrimaryBackground)
            .padding(2.dp)
    )
}
