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

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.core.ui.components.EmptyBottomSpacer
import com.stoyanvuchev.magicmessage.core.ui.effect.defaultLightWeightBlur
import com.stoyanvuchev.magicmessage.core.ui.ext.LocalHazeState
import dev.chrisbanes.haze.hazeSource

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GridOfCreationItems(
    isLightWeightBlurApplied: Boolean,
    lazyGridState: LazyGridState,
    innerPadding: PaddingValues,
    hazeSourceKey: String,
    content: LazyGridScope.() -> Unit
) = SharedTransitionLayout {

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .hazeSource(
                state = LocalHazeState.current,
                key = hazeSourceKey
            )
            .defaultLightWeightBlur(isApplied = isLightWeightBlurApplied)
            .padding(horizontal = 24.dp),
        contentPadding = innerPadding,
        state = lazyGridState,
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {

        content()

        item(
            span = { GridItemSpan(this.maxLineSpan) }
        ) {
            EmptyBottomSpacer()
        }

    }

}