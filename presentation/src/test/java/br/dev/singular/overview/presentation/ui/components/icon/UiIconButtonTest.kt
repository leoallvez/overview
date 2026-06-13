package br.dev.singular.overview.presentation.ui.components.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconSource
import br.dev.singular.overview.presentation.ui.components.icon.style.UiIconStyle
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiIconButtonTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `UiIconButton should call onClick when clicked`() {
        val onClick: () -> Unit = mockk(relaxed = true)
        
        rule.setContent {
            UiIconButton(
                iconStyle = UiIconStyle(
                    source = UiIconSource.vector(Icons.Default.Favorite),
                ),
                onClick = onClick
            )
        }
        
        rule.onNode(hasClickAction()).performClick()
        verify { onClick() }
    }
}
