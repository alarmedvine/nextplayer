package dev.anilbeesetti.nextplayer.feature.videopicker.screens.mediapicker

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import dev.anilbeesetti.nextplayer.core.ui.R
import dev.anilbeesetti.nextplayer.core.ui.components.LocalTopLevelPageSwitcher
import dev.anilbeesetti.nextplayer.core.ui.components.NextTopAppBar
import dev.anilbeesetti.nextplayer.core.ui.components.TopLevelFabKey

/** Keep the personal chip row outside the upstream screen's frequently changing toolbar. */
@Composable
internal fun PersonalMediaTopAppBar(
    showPageChips: Boolean,
    title: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
) {
    Column {
        NextTopAppBar(
            title = title,
            navigationIcon = navigationIcon,
            actions = actions,
        )
        if (showPageChips) {
            PersonalPageChips()
        }
    }
}

private val personalPages = listOf(
    TopLevelFabKey.MEDIA to R.string.home,
    TopLevelFabKey.PLAYLISTS to R.string.playlists,
    TopLevelFabKey.NETWORK to R.string.network,
    TopLevelFabKey.MORE to R.string.more,
)

@Composable
private fun PersonalPageChips() {
    val switchPage = LocalTopLevelPageSwitcher.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(
                horizontal = 12.dp,
                vertical = 4.dp,
            ),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        personalPages.forEach { (key, labelResId) ->
            val label = stringResource(id = labelResId)
            FilterChip(
                selected = key == TopLevelFabKey.MEDIA,
                onClick = { if (key != TopLevelFabKey.MEDIA) switchPage(key) },
                label = { Text(text = label) },
                modifier = Modifier.semantics { contentDescription = label },
            )
        }
    }
}
