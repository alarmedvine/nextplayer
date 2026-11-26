package dev.anilbeesetti.nextplayer.feature.videopicker.screens.mediapicker

import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.anilbeesetti.nextplayer.core.ui.components.LocalTopLevelPageSwitcher
import dev.anilbeesetti.nextplayer.core.ui.components.TopLevelFabKey
import dev.anilbeesetti.nextplayer.core.ui.theme.NextPlayerTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class PersonalMediaTopAppBarTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun pageChipsAreBelowTheToolbarInsteadOfOverlappingIt() {
        showToolbar()

        val title = composeRule.onNodeWithText(text = "Toolbar title")
            .assertIsDisplayed().fetchSemanticsNode().boundsInRoot
        val home = composeRule.onNodeWithContentDescription(label = "Home")
            .assertIsDisplayed().fetchSemanticsNode().boundsInRoot
        assertTrue(home.top >= title.bottom)
    }

    @Test
    fun eachSecondaryChipUsesTheExistingPageSwitchHook() {
        val switches = mutableListOf<String>()
        showToolbar(onSwitchPage = switches::add)

        composeRule.onNodeWithContentDescription(label = "Home").performClick()
        composeRule.onNodeWithContentDescription(label = "Playlists").performClick()
        composeRule.onNodeWithContentDescription(label = "Network").performClick()
        composeRule.onNodeWithContentDescription(label = "More").performClick()

        composeRule.runOnIdle {
            assertEquals(
                listOf(
                    TopLevelFabKey.PLAYLISTS,
                    TopLevelFabKey.NETWORK,
                    TopLevelFabKey.MORE,
                ),
                switches,
            )
        }
    }

    @Test
    fun hidingChipsPreservesTheUpstreamToolbar() {
        var showChips by mutableStateOf(value = true)
        composeRule.setContent {
            NextPlayerTheme {
                PersonalMediaTopAppBar(
                    showPageChips = showChips,
                    title = { Text(text = "Toolbar title") },
                    navigationIcon = {},
                    actions = {},
                )
            }
        }
        composeRule.onNodeWithContentDescription(label = "Network").assertIsDisplayed()

        composeRule.runOnIdle { showChips = false }

        composeRule.onNodeWithText(text = "Toolbar title").assertIsDisplayed()
        listOf(
            "Home",
            "Playlists",
            "Network",
            "More",
        ).forEach { label ->
            composeRule.onNodeWithContentDescription(label = label).assertDoesNotExist()
        }
    }

    private fun showToolbar(onSwitchPage: (String) -> Unit = {}) {
        composeRule.setContent {
            CompositionLocalProvider(value = LocalTopLevelPageSwitcher provides onSwitchPage) {
                NextPlayerTheme {
                    PersonalMediaTopAppBar(
                        showPageChips = true,
                        title = { Text(text = "Toolbar title") },
                        navigationIcon = {},
                        actions = {},
                    )
                }
            }
        }
    }
}
