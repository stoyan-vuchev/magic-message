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

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.core.ui.components.bottom_nav_bar.BottomNavBarTokens.NavigationBarHeight
import com.stoyanvuchev.magicmessage.core.ui.effect.defaultHazeEffect
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ListOptionItemDetailsLayout(
    sharedKey: String?,
    label: String,
    painter: Painter,
    innerPadding: PaddingValues,
    boundsTransform: (Rect, Rect) -> SpringSpec<Rect>,
    onSharedKey: (String?) -> Unit,
    content: @Composable RowScope.() -> Unit
) {

    val isBackgroundVisible by rememberUpdatedState(sharedKey != null)

    AnimatedVisibility(
        modifier = Modifier.fillMaxSize(),
        visible = isBackgroundVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(bottom = NavigationBarHeight)
                .defaultHazeEffect()
                .padding(top = innerPadding.calculateTopPadding())
        )

    }

    AnimatedContent(
        modifier = Modifier
            .padding(bottom = NavigationBarHeight)
            .fillMaxSize(),
        targetState = sharedKey,
        transitionSpec = remember { { fadeIn() togetherWith fadeOut() } },
        label = "ListOptionItemDetailsLayoutAnimatedContent"
    ) { key ->

        key?.let {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .padding(top = innerPadding.calculateTopPadding())
                    .clickable(
                        onClick = remember { { onSharedKey(null) } },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Icon(
                        modifier = Modifier.sharedElement(
                            sharedContentState = rememberSharedContentState(key = "$it-icon"),
                            animatedVisibilityScope = this@AnimatedContent,
                            boundsTransform = boundsTransform,
                        ),
                        painter = painter,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        modifier = Modifier
                            .animateContentSize()
                            .sharedElement(
                                sharedContentState = rememberSharedContentState(key = "$it-label"),
                                animatedVisibilityScope = this@AnimatedContent,
                                boundsTransform = boundsTransform,
                            ),
                        text = label,
                        style = Theme.typefaces.bodyLarge,
                        color = Theme.colors.onSurfaceElevationLow,
                        textAlign = TextAlign.Center
                    )

                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "$it-bounds"),
                            animatedVisibilityScope = this@AnimatedContent,
                            boundsTransform = boundsTransform,
                        )
                        .padding(horizontal = 32.dp)
                        .fillMaxWidth()
                        .background(
                            color = Theme.colors.surfaceElevationHigh.copy(.5f),
                            shape = Theme.shapes.mediumShape
                        )
                        .border(
                            width = 1.dp,
                            color = Theme.colors.outline,
                            shape = Theme.shapes.mediumShape
                        )
                        .clip(shape = Theme.shapes.mediumShape)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    content = content
                )

            }

        }

    }

}