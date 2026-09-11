package br.dev.singular.overview.presentation.ui.components

import androidx.compose.runtime.Composable
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.HtmlReportWriter
import app.cash.paparazzi.Paparazzi
import app.cash.paparazzi.SnapshotHandler
import app.cash.paparazzi.SnapshotVerifier
import br.dev.singular.overview.presentation.ui.theme.AppTheme
import org.junit.Rule
import java.io.File

abstract class UiSnapshotTest(snapshotPackage: String) {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        snapshotHandler = determineHandler(snapshotPackage)
    )

    fun snapshot(
        name: String? = null,
        locale: String? = null,
        deviceConfig: DeviceConfig? = null,
        sut: @Composable () -> Unit
    ) {
        val baseConfig = deviceConfig ?: DeviceConfig.PIXEL_5
        val finalConfig = if (locale != null) baseConfig.copy(locale = locale) else baseConfig

        paparazzi.unsafeUpdateConfig(deviceConfig = finalConfig)

        paparazzi.snapshot(name = name) {
            AppTheme {
                sut()
            }
        }
    }

    private fun determineHandler(
        snapshotPackage: String,
        maxPercentDifference: Double = 0.1
    ): SnapshotHandler {
        val isVerify = System.getProperty("paparazzi.test.verify")?.toBoolean() == true
        val root = File("src/test/snapshots/$snapshotPackage")

        return if (isVerify) {
            SnapshotVerifier(
                maxPercentDifference = maxPercentDifference,
                rootDirectory = root
            )
        } else {
            HtmlReportWriter(
                maxPercentDifference = maxPercentDifference,
                snapshotRootDirectory = root
            )
        }
    }
}

abstract class UiScreenSnapshotTest(
    snapshotPackage: String
) : UiSnapshotTest(snapshotPackage) {

    private val screenConfigs by lazy {
        listOf(
            "Small_Screen_Nexus_5" to DeviceConfig.NEXUS_5,
            "Standard_Pixel_3A_XL" to DeviceConfig.PIXEL_3A_XL,
            "Fold_Pixel_Fold" to DeviceConfig.PIXEL_FOLD,
            "Tablet_Pixel_Tablet" to DeviceConfig.PIXEL_TABLET
        )
    }

    fun snapshot(sut: @Composable () -> Unit) {
        screenConfigs.forEach { (configName, config) ->
            snapshot(
                name = configName,
                deviceConfig = config,
                sut = sut
            )
        }
    }
}
