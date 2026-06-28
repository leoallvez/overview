package br.dev.singular.overview.presentation.ui.components.navigation.bottom

import androidx.navigation.NavOptionsBuilder
import br.dev.singular.overview.presentation.NavigationWrapperMock
import br.dev.singular.overview.presentation.ui.navigation.Destination
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UiBottomNavigationStateTest {

    private lateinit var navigation: NavigationWrapperMock
    private lateinit var sut: UiBottomNavigationState

    @Before
    fun setup() {
        navigation = NavigationWrapperMock()
        sut = UiBottomNavigationState(navigation)
    }

    @Test
    fun `navigationItems should contain Home, Search and Favorites`() {
        val items = sut.navItems
        assertEquals(3, items.size)
        assertTrue(items.contains(UiBottomNavItem.Home))
        assertTrue(items.contains(UiBottomNavItem.Search))
        assertTrue(items.contains(UiBottomNavItem.Favorites))
    }

    @Test
    fun `onSelectItem should configure NavOptions correctly`() {
        sut.onSelectItem(UiBottomNavItem.Search)

        assertEquals(Destination.Search.route, navigation.activeRoute)
        
        val builder = NavOptionsBuilder()
        val lambda = navigation.lastNavOptionsBuilder
        assertNotNull(lambda)
        lambda?.invoke(builder)
        
        assertTrue(builder.launchSingleTop)
        assertTrue(builder.restoreState)
    }

    @Test
    fun `onNavItemClick should navigate with correct route for each item`() {
        sut.onSelectItem(UiBottomNavItem.Home)
        assertEquals(Destination.SelectCatalog.route, navigation.activeRoute)

        sut.onSelectItem(UiBottomNavItem.Search)
        assertEquals(Destination.Search.route, navigation.activeRoute)

        sut.onSelectItem(UiBottomNavItem.Favorites)
        assertEquals(Destination.Favorites.route, navigation.activeRoute)
    }

    @Test
    fun `onSelectItem should ignore navigation when item is already active`() {
        navigation.activeRoute = Destination.Search.route
        navigation.wasNavigateCalled = false

        sut.onSelectItem(UiBottomNavItem.Search)

        assertFalse(navigation.wasNavigateCalled)
    }
}
