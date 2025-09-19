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

package com.stoyanvuchev.magicmessage.presentation.main.draw_screen.dialog

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.core.ui.components.bottom_bar.BottomBarTokens.BottomBarHeight
import com.stoyanvuchev.magicmessage.core.ui.components.interaction.rememberRipple
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme
import com.stoyanvuchev.magicmessage.core.ui.theme.isInDarkThemeMode

@Composable
fun RowScope.DrawScreenDialogItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    selected: Boolean,
    label: @Composable (() -> Unit)? = null,
    icon: @Composable () -> Unit,
) {

    val bgColor by animateColorAsState(
        targetValue = Theme.colors.surfaceElevationHigh.copy(
            alpha = if (selected) .75f else 0f
        )
    )

    val borderColor by animateColorAsState(
        targetValue = Theme.colors.outline.copy(
            alpha = if (selected) (if (isInDarkThemeMode()) .04f else 1f) else 0f
        )
    )

    Column(
        modifier
            .clickable(
                onClick = onClick,
                indication = rememberRipple(),
                interactionSource = remember { MutableInteractionSource() },
                role = Role.Tab
            )
            .defaultMinSize(minHeight = BottomBarHeight)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = Theme.shapes.smallShape
            )
            .background(
                color = bgColor,
                shape = Theme.shapes.smallShape
            )
            .weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {

        CompositionLocalProvider(
            LocalTextStyle provides Theme.typefaces.bodySmall,
            LocalContentColor provides Theme.colors.onSurfaceElevationLow,
            content = {
                icon()
                if (label != null) label()
            }
        )

    }

}