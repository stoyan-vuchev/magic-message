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

package com.stoyanvuchev.magicmessage.core.ui.components.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.core.ui.components.interaction.rememberRipple
import com.stoyanvuchev.magicmessage.core.ui.effect.defaultHazeEffect
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme
import dev.chrisbanes.haze.HazeState

@Composable
fun ListItemOption(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    hazeState: HazeState,
    label: @Composable () -> Unit,
    icon: @Composable (() -> Unit)? = null
) {

    CompositionLocalProvider(
        LocalContentColor provides Theme.colors.onSurfaceElevationHigh,
        LocalTextStyle provides Theme.typefaces.bodyLarge
    ) {

        Row(
            modifier = modifier
                .padding(horizontal = 24.dp)
                .clip(shape = Theme.shapes.verySmallShape)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(),
                    onClick = onClick
                )
                .defaultHazeEffect(hazeState = hazeState)
                .background(Theme.colors.surfaceElevationHigh.copy(.5f))
                .border(
                    width = 1.dp,
                    color = Theme.colors.outline,
                    shape = Theme.shapes.verySmallShape
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (icon != null) {
                icon()
                Spacer(modifier = Modifier.width(16.dp))
            }

            label()

            Spacer(modifier = Modifier.weight(1f))

        }

    }

}