package com.stoyanvuchev.magicmessage.core.ui.effect

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect

fun Modifier.defaultHazeEffect(
    hazeState: HazeState
): Modifier {
    return composed {
        hazeEffect(
            state = hazeState,
            style = HazeStyle(
                tint = HazeTint(color = Theme.colors.surfaceElevationLow.copy(.5f)),
                blurRadius = 16.dp,
                noiseFactor = -.5f,
                backgroundColor = Theme.colors.surfaceElevationLow

            )
        )
    }
}