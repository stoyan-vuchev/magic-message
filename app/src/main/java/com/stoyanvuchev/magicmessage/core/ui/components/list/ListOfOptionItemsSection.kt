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

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.core.ui.etc.UIString
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme

@OptIn(ExperimentalSharedTransitionApi::class)
fun LazyListScope.listOfOptionItemsSelection(
    sharedTransitionScope: SharedTransitionScope,
    label: UIString,
    categoryKey: String,
    boundsTransform: (Rect, Rect) -> SpringSpec<Rect>,
    content: LazyListScope.(String, SharedTransitionScope, (Rect, Rect) -> SpringSpec<Rect>) -> Unit
) = with(sharedTransitionScope) {

    item(key = "${categoryKey}_label") {

        Spacer(modifier = Modifier
            .animateItem()
            .height(0.dp))

        Text(
            modifier = Modifier
                .animateItem()
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp),
            text = label.asString(),
            style = Theme.typefaces.bodySmall,
            color = Theme.colors.onSurfaceElevationLow.copy(.5f)
        )

        Spacer(modifier = Modifier
            .animateItem()
            .height(0.dp))

    }

    content(
        categoryKey,
        sharedTransitionScope,
        boundsTransform
    )

}