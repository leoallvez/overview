package br.dev.singular.overview.presentation.ui.components.navigation.bottom

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.singular.overview.presentation.NavigationWrapperMock
import br.dev.singular.overview.presentation.ui.navigation.Destination
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class UiBottomNavigationTest {

    @get:Rule
    val rule = createComposeRule()

    lateinit var navMock: NavigationWrapperMock
    lateinit var stateMock: IUiBottomNavigationState

    @Before
    fun setup() {
        navMock = NavigationWrapperMock()
        stateMock = UiBottomNavigationState(navigation = navMock)
    }

    @Test
    fun `when splash route is active should hide navigation bar`() {
        val tag = "bottom_nav"
        navMock.activeRoute = Destination.Splash.route

        rule.setContent {
            UiBottomNavigation(
                state = stateMock,
                modifier = Modifier.testTag(tag)
            )
        }

        rule.onNodeWithTag(tag).assertDoesNotExist()
    }

    @Test
    fun `when player route is active should hide navigation bar`() {

        val tag = "bottom_nav"
        navMock.activeRoute = Destination.YouTubePlayer.editRoute(videoKey = "A113")

        rule.setContent {
            UiBottomNavigation(
                state = stateMock,
                modifier = Modifier.testTag(tag)
            )
        }

        rule.onNodeWithTag(tag).assertDoesNotExist()
    }

    @Test
    fun `when current route is null should show navigation bar`() {

        val tag = "bottom_nav"
        navMock.activeRoute = null

        rule.setContent {
            UiBottomNavigation(
                state = stateMock,
                modifier = Modifier.testTag(tag)
            )
        }

        rule.onNodeWithTag(tag).assertExists()
    }

    @Test
    fun `when regular route is active should show all navigation items`() {

        navMock.activeRoute = Destination.SelectCatalog.route

        rule.setContent {
            UiBottomNavigation(state = stateMock)
        }

        rule.onNodeWithText("Home").assertIsDisplayed()
        rule.onNodeWithText("Search").assertIsDisplayed()
        rule.onNodeWithText("Favorites").assertIsDisplayed()
    }

    @Test
    fun `when inactive item is clicked should trigger navigation`() {

        navMock.activeRoute = Destination.SelectCatalog.route
        rule.setContent {
            UiBottomNavigation(state = stateMock)
        }

        rule.onNodeWithText("Search").performClick()

        assert(navMock.activeRoute == Destination.Search.route)
    }

    @Test
    fun `when active item is clicked should ignore navigation`() {

        navMock.activeRoute = Destination.SelectCatalog.route
        navMock.wasNavigateCalled = false

        rule.setContent {
            UiBottomNavigation(state = stateMock)
        }

        rule.onNodeWithText("Home").performClick()

        assert(!navMock.wasNavigateCalled)
    }
}
