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

package com.stoyanvuchev.magicmessage.core.ui.components.grid

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.core.ui.etc.CANVAS_ASPECT_RATIO
import com.stoyanvuchev.magicmessage.core.ui.etc.UIString
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme
import com.stoyanvuchev.magicmessage.domain.model.CreationModel

@OptIn(ExperimentalSharedTransitionApi::class)
fun LazyGridScope.gridOfCreationItemsSection(
    sharedTransitionScope: SharedTransitionScope,
    label: UIString,
    items: List<CreationModel>,
    categoryKey: String,
    sharedCreation: CreationModel?,
    boundsTransform: (Rect, Rect) -> SpringSpec<Rect>,
    onCreationClick: (CreationModel) -> Unit,
    onCreationLongClick: (CreationModel, IntSize) -> Unit
) = with(sharedTransitionScope) {

    item(
        span = { GridItemSpan(this.maxLineSpan) }
    ) {

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp),
            text = label.asString(),
            style = Theme.typefaces.bodySmall,
            color = Theme.colors.onSurfaceElevationLow.copy(.5f)
        )

        Spacer(modifier = Modifier.height(0.dp))

    }

    itemsIndexed(
        items = items,
        key = { _, creation -> "${categoryKey}_${creation.createdAt}" }
    ) { i, creation ->

        val startPadding by remember(i) {
            derivedStateOf { if (i % 2 == 0) 24.dp else 0.dp }
        }

        val endPadding by remember(i) {
            derivedStateOf { if (i % 2 == 0) 0.dp else 24.dp }
        }

        val visible by rememberUpdatedState(
            sharedCreation?.id != creation.id
        )

        AnimatedVisibility(
            modifier = Modifier
                .animateItem()
                .padding(
                    start = startPadding,
                    end = endPadding
                )
                .fillMaxWidth()
                .aspectRatio(ratio = CANVAS_ASPECT_RATIO),
            visible = visible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {

            Box(
                modifier = Modifier
                    .sharedBounds(
                        sharedContentState = rememberSharedContentState(
                            key = "${creation.id ?: 0}-bounds"
                        ),
                        animatedVisibilityScope = this@AnimatedVisibility,
                        boundsTransform = boundsTransform,
                    )
                    .fillMaxSize()
            ) {

                GridCreationItem(
                    modifier = Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(
                                key = creation.id ?: 0
                            ),
                            animatedVisibilityScope = this@AnimatedVisibility,
                            boundsTransform = boundsTransform,
                        )
                        .fillMaxSize(),
                    creation = creation,
                    onCreationClick = onCreationClick,
                    onCreationLongClick = onCreationLongClick
                )

            }

        }

    }

}