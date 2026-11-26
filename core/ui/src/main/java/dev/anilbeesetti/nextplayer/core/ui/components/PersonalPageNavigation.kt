package dev.anilbeesetti.nextplayer.core.ui.components

import androidx.compose.runtime.compositionLocalOf

/** Fork-only page-switch hook; unknown keys are ignored by the application. */
val LocalTopLevelPageSwitcher = compositionLocalOf<(String) -> Unit> { {} }
