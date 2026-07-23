package br.dev.singular.overview.presentation.ui.utils

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

/**
 * Custom multi-preview annotation for full screens and large components.
 */

private const val BACKGROUND = 0xFF000000

@Preview(
    name = "Small Screen (Nexus 5)",
    device = Devices.NEXUS_5,
    showBackground = true,
    backgroundColor = BACKGROUND
)
@Preview(
    name = "Standard Device (Pixel 3A XL)",
    device = Devices.PIXEL_3A_XL,
    showBackground = true,
    backgroundColor = BACKGROUND
)
@Preview(
    name = "Standard Device (Pixel Fold)",
    device = Devices.PIXEL_FOLD,
    showBackground = true,
    backgroundColor = BACKGROUND
)
@Preview(
    name = "Tablet (Pixel Tablet)",
    device = Devices.PIXEL_TABLET,
    showBackground = true,
    backgroundColor = BACKGROUND
)
annotation class UiScreenPreview

/**
 * Custom preview annotation for smaller UI components.
 */
@Preview(
    name = "Component Preview",
    showBackground = true,
    backgroundColor = 0xFF000000
)
annotation class UiComponentPreview
