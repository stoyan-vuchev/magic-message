/*
 * MIT License
 *
 * Copyright (c) 2025 Stoyan Vuchev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES, OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.stoyanvuchev.magicmessage.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.core.ui.components.interaction.rememberRipple
import com.stoyanvuchev.magicmessage.core.ui.effect.defaultHazeEffect
import com.stoyanvuchev.magicmessage.core.ui.effect.innerAuraGlow
import com.stoyanvuchev.magicmessage.core.ui.effect.outerAuraGlow
import com.stoyanvuchev.magicmessage.core.ui.ext.LocalHazeState
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme
import dev.chrisbanes.haze.HazeState

@Composable
fun AuraButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    hazeState: HazeState = LocalHazeState.current,
    isGlowVisible: Boolean = true,
    glowColor: Color = Theme.colors.primary,
    onGlowColor: Color = Theme.colors.onPrimary,
    inactiveColor: Color = Theme.colors.surfaceElevationMedium,
    onInactiveColor: Color = Theme.colors.onSurfaceElevationMedium,
    size: Dp = 48.dp,
    content: @Composable (() -> Unit)
) {

    var centerOffset by remember { mutableStateOf(IntOffset.Zero) }
    val transition = rememberInfiniteTransition(
        label = "aura-button-transition"
    )

    val glowOffsetX1 by transition.animateValue(
        initialValue = 0.dp,
        targetValue = 0.dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                (-2).dp at 0
                6.dp at 1000
                durationMillis = 2000
            },
            repeatMode = RepeatMode.Reverse
        )

    )

    val glowOffsetX2 by transition.animateValue(
        initialValue = 0.dp,
        targetValue = 0.dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                6.dp at 0
                (-2).dp at 1000
                durationMillis = 2000
            },
            repeatMode = RepeatMode.Reverse
        )

    )

    val alpha by animateFloatAsState(
        targetValue = if (isGlowVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 360)
    )

    val containerColor by animateColorAsState(
        targetValue = if (isGlowVisible) glowColor
        else inactiveColor
    )

    val contentColor by animateColorAsState(
        targetValue = if (isGlowVisible) onGlowColor
        else onInactiveColor
    )

    Box(
        modifier = Modifier
            .size(size)
            .onSizeChanged { centerOffset = it.center }
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                role = Role.Button
            )
            .outerAuraGlow(
                color = glowColor,
                centerOffset = centerOffset,
                glowOffset1 = glowOffsetX1,
                glowOffset2 = glowOffsetX2,
                alpha = alpha
            )
            .clip(CircleShape)
            .defaultHazeEffect(hazeState = hazeState)
            .background(
                color = containerColor,
                shape = CircleShape
            )
            .border(
                width = 1.dp,
                color = Theme.colors.outline,
                shape = CircleShape
            )
            .innerAuraGlow(
                color = glowColor,
                centerOffset = centerOffset,
                alpha = alpha
            )
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {

        CompositionLocalProvider(
            LocalContentColor provides contentColor,
            content = content
        )

    }

}