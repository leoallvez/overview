package io.github.leoallvez.take

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import dagger.hilt.android.AndroidEntryPoint
import io.github.leoallvez.firebase.RemoteConfigKey.*
import io.github.leoallvez.firebase.RemoteSource
import io.github.leoallvez.take.abtest.ExampleStrategy
import io.github.leoallvez.take.ui.theme.TakeTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var remote: RemoteSource
    @Inject
    lateinit var example: ExampleStrategy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TakeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Logo()
                }
            }
        }
        //val name = remote.getString(key = NAME_KEY)
        Log.i("my_model", "model before: ${example.valueToChange}")
        example.execute()
        Log.i("my_model", "model after : ${example.valueToChange}")
    }
}

@Composable
fun Logo() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Take",
            style = MaterialTheme.typography.overline,
            fontSize = 100.sp,
            //modifier = Modifier.padding(10.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TakeTheme {
        Logo()
    }
}