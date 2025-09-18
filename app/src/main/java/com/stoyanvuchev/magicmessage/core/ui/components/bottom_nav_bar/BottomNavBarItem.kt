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

package com.stoyanvuchev.magicmessage.core.ui.components.bottom_nav_bar

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.core.ui.components.bottom_nav_bar.BottomNavBarTokens.NavigationBarHeight
import com.stoyanvuchev.magicmessage.core.ui.components.interaction.rememberRipple
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme

@Composable
fun RowScope.BottomNavBarItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable (iconOffsetY: Dp) -> Unit,
    label: @Composable () -> Unit
) {

    val transition = updateTransition(
        targetState = selected,
        label = "BottomNavBarItemTransition"
    )

    val tintColor by transition.animateColor(label = "TintColor") {
        Theme.colors.onSurfaceElevationLow.copy(if (it) 1f else 0.75f)
    }

    val indicatorColor by transition.animateColor(label = "IndicatorColor") {
        Theme.colors.onSurfaceElevationLow.copy(if (it) 1f else .5f)
    }

    val bumpHeight by transition.animateDp(
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        },
        label = "BumpHeight"
    ) { isSelected ->
        if (isSelected) 8.dp else 0.dp
    }

    val iconOffsetY by transition.animateDp(
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        },
        label = "IconOffsetY"
    ) { isSelected ->
        if (isSelected) 4.dp else 0.dp
    }

    Column(
        modifier
            .selectable(
                selected = selected,
                onClick = onClick,
                indication = rememberRipple(),
                interactionSource = remember { MutableInteractionSource() },
                role = Role.Tab
            )
            .defaultMinSize(minHeight = NavigationBarHeight)
            .weight(1f)
            .drawBehind {

                val bumpWidth = 32.dp.toPx()
                val bumpH = bumpHeight.toPx().coerceAtLeast(0f)
                val centerX = size.width / 2f

                drawPath(
                    path = bumpPath(
                        centerX = centerX,
                        width = bumpWidth,
                        height = bumpH,
                        totalWidth = size.width
                    ),
                    color = indicatorColor
                )

            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {

        CompositionLocalProvider(
            LocalTextStyle provides Theme.typefaces.bodySmall,
            LocalContentColor provides tintColor,
            content = {
                icon(iconOffsetY)
                label()
            }
        )

    }

}