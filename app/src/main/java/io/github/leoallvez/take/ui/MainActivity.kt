package io.github.leoallvez.take.ui

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
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import io.github.leoallvez.take.AnalyticsLogger
import io.github.leoallvez.take.di.IsOnline
import io.github.leoallvez.take.ui.navigation.NavController
import io.github.leoallvez.take.ui.theme.TakeTheme
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
        OfflineSnackBar(
            isNotOnline = isOnline.not(),
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
