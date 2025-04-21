import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import br.dev.singular.overview.presentation.ui.theme.PrimaryBackground
import org.junit.Rule

abstract class BaseSnapshotTest() {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5
    )

    protected fun snapshot(sutComposable: @Composable () -> Unit) {
        paparazzi.snapshot {
            Box(modifier = Modifier.fillMaxSize().background(PrimaryBackground)) {
                sutComposable()
            }
        }
    }
}
