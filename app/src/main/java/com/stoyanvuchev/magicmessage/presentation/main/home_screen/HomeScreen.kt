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

package com.stoyanvuchev.magicmessage.presentation.main.home_screen

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.R
import com.stoyanvuchev.magicmessage.core.ui.components.top_bar.TopBar
import com.stoyanvuchev.magicmessage.core.ui.effect.defaultHazeEffect
import com.stoyanvuchev.magicmessage.core.ui.event.NavigationEvent
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreen(
    state: HomeScreenState,
    onUIAction: (HomeScreenUIAction) -> Unit,
    onNavigationEvent: (NavigationEvent) -> Unit
) {

    val lazyGridState = rememberLazyGridState()
    val hazeState = rememberHazeState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = Theme.colors.surfaceElevationLow,
        contentColor = Theme.colors.onSurfaceElevationLow,
        topBar = {

            TopBar(
                modifier = Modifier.defaultHazeEffect(hazeState = hazeState),
                title = { Text(text = stringResource(R.string.home_screen_label)) }
            )

        }
    ) { innerPadding ->

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(state = hazeState),
            contentPadding = innerPadding,
            state = lazyGridState,
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            if (state.exportedCreationsList.isNotEmpty()) {

                item(
                    span = { GridItemSpan(this.maxLineSpan) }
                ) {

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .padding(top = 24.dp)
                            .animateItem(),
                        text = stringResource(R.string.home_screen_exported_label),
                        style = Theme.typefaces.bodySmall,
                        color = Theme.colors.onSurfaceElevationLow.copy(.5f)
                    )

                    Spacer(modifier = Modifier.height(0.dp))

                }

                itemsIndexed(
                    items = state.exportedCreationsList,
                    key = { _, creation -> "exported_item_${creation.createdAt}" }
                ) { i, creation ->

                    val startPadding by remember(i) {
                        derivedStateOf { if (i % 2 == 0) 24.dp else 0.dp }
                    }

                    val endPadding by remember(i) {
                        derivedStateOf { if (i % 2 == 0) 0.dp else 24.dp }
                    }

                    HomeScreenCreationItem(
                        modifier = Modifier
                            .padding(
                                start = startPadding,
                                end = endPadding
                            )
                            .animateItem(),
                        creation = creation,
                        onNavigationEvent = onNavigationEvent,
                        onLongClick = remember { {} }
                    )

                }

            }

            if (state.draftedCreationsList.isNotEmpty()) {

                item(
                    span = { GridItemSpan(this.maxLineSpan) }
                ) {

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .padding(top = 24.dp)
                            .animateItem(),
                        text = stringResource(R.string.home_screen_drafted_label),
                        style = Theme.typefaces.bodySmall,
                        color = Theme.colors.onSurfaceElevationLow.copy(.5f)
                    )

                    Spacer(modifier = Modifier.height(0.dp))

                }

                itemsIndexed(
                    items = state.draftedCreationsList,
                    key = { _, creation -> "drafted_item_${creation.createdAt}" }
                ) { i, creation ->

                    val startPadding by remember(i) {
                        derivedStateOf { if (i % 2 == 0) 24.dp else 0.dp }
                    }

                    val endPadding by remember(i) {
                        derivedStateOf { if (i % 2 == 0) 0.dp else 24.dp }
                    }

                    HomeScreenCreationItem(
                        modifier = Modifier
                            .padding(
                                start = startPadding,
                                end = endPadding
                            )
                            .animateItem(),
                        creation = creation,
                        onNavigationEvent = onNavigationEvent,
                        onLongClick = remember { {} }
                    )

                }

            }

            item(
                span = { GridItemSpan(this.maxLineSpan) }
            ) {
                Spacer(modifier = Modifier.height(164.dp))
            }

        }

    }

    if (state.exportedCreationsList.isEmpty() && state.draftedCreationsList.isEmpty()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
        ) {

            Icon(
                modifier = Modifier.size(100.dp),
                painter = painterResource(R.drawable.logo_icon),
                contentDescription = null
            )

            Text(
                text = stringResource(R.string.home_screen_empty_text),
                style = Theme.typefaces.bodyLarge,
                color = Theme.colors.onSurfaceElevationLow.copy(.5f)
            )

        }

    }

}