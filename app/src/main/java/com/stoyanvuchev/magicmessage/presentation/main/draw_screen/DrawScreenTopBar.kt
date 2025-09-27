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

package com.stoyanvuchev.magicmessage.presentation.main.draw_screen

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.R
import com.stoyanvuchev.magicmessage.core.ui.DrawingController
import com.stoyanvuchev.magicmessage.core.ui.components.AuraButton
import com.stoyanvuchev.magicmessage.core.ui.components.top_bar.TopBar
import com.stoyanvuchev.magicmessage.core.ui.event.NavigationEvent

@Composable
fun DrawScreenTopBar(
    state: DrawScreenState,
    drawingController: DrawingController,
    onUIAction: (DrawScreenUIAction) -> Unit,
    onNavigationEvent: (NavigationEvent) -> Unit
) = TopBar(
    title = remember { {} },
    navigationAction = {

        IconButton(
            onClick = remember { { onNavigationEvent(NavigationEvent.NavigateUp) } }
        ) {
            Icon(
                modifier = Modifier.size(28.dp),
                painter = painterResource(R.drawable.arrow_left_outlined),
                contentDescription = stringResource(R.string.navigate_back)
            )
        }

    },
    actions = {

        IconButton(
            onClick = remember { { onUIAction(DrawScreenUIAction.Undo) } },
        ) {
            Icon(
                painter = painterResource(R.drawable.undo),
                contentDescription = stringResource(R.string.action_undo)
            )
        }

        IconButton(
            onClick = remember { { onUIAction(DrawScreenUIAction.Redo) } }
        ) {
            Icon(
                painter = painterResource(R.drawable.redo),
                contentDescription = stringResource(R.string.action_redo)
            )
        }

        val isGlowVisible by rememberUpdatedState(
            drawingController.totalPointCount
                    == DrawingController.MAX_POINTS
        )

        AuraButton(
            size = 40.dp,
            onClick = { onUIAction(DrawScreenUIAction.Export(messageId = state.messageId)) },
            isGlowVisible = isGlowVisible,
            glowColor = state.drawConfiguration.color
        ) {

            Icon(
                painter = painterResource(R.drawable.export),
                contentDescription = stringResource(R.string.export_gif)
            )

        }

    }
)