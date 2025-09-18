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

package com.stoyanvuchev.magicmessage.core.ui.components.top_bar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.core.ui.components.top_bar.TopBarTokens.TopBarActionItemsHorizontalPadding
import com.stoyanvuchev.magicmessage.core.ui.components.top_bar.TopBarTokens.TopBarHeight
import com.stoyanvuchev.magicmessage.core.ui.components.top_bar.TopBarTokens.TopBarActionHorizontalPadding
import com.stoyanvuchev.magicmessage.core.ui.components.top_bar.TopBarTokens.TopBarTitleHorizontalPadding
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    navigationAction: @Composable (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit)? = null,
    windowInsets: WindowInsets = WindowInsets.statusBars
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clipToBounds()
            .then(modifier)
    ) {

        Row(
            modifier = Modifier
                .windowInsetsPadding(windowInsets)
                .fillMaxWidth()
                .height(TopBarHeight),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (navigationAction != null) {
                Spacer(modifier = Modifier.width(TopBarActionHorizontalPadding))
                navigationAction()
                Spacer(modifier = Modifier.width(TopBarActionHorizontalPadding))
            } else {
                Spacer(modifier = Modifier.width(TopBarTitleHorizontalPadding))
            }

            CompositionLocalProvider(
                LocalTextStyle provides Theme.typefaces.titleSmall,
                content = title
            )

            Spacer(modifier = Modifier.width(TopBarTitleHorizontalPadding))

            if (actions != null) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement
                        .spacedBy(
                            TopBarActionItemsHorizontalPadding,
                            Alignment.End
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    content = actions
                )
                Spacer(modifier = Modifier.width(TopBarActionHorizontalPadding))
            }

        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = Theme.colors.outline,
            thickness = 1.dp
        )

    }

}