package br.com.deepbyte.overview.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.LiveData
import br.com.deepbyte.overview.di.IsOnline
import br.com.deepbyte.overview.ui.navigation.NavController
import br.com.deepbyte.overview.ui.theme.TakeTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalPagerApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    @IsOnline
    lateinit var isOnline: LiveData<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TakeTheme {
                Surface(color = MaterialTheme.colors.background) {
                    TakeApp(isOnline = isOnline.observeAsState(initial = true).value)
                }
            }
        }
    }
}

@ExperimentalPagerApi
@Composable
fun TakeApp(isOnline: Boolean) {
    Box {
        NavController()
        OfflineSnackBar(
            isNotOnline = isOnline.not(),
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
