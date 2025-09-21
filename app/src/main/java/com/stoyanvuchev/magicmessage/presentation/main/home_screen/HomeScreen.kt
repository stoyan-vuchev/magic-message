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

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.R
import com.stoyanvuchev.magicmessage.core.ui.components.AuraButton
import com.stoyanvuchev.magicmessage.core.ui.components.bottom_nav_bar.BottomNavBarTokens.NavigationBarHeight
import com.stoyanvuchev.magicmessage.core.ui.components.top_bar.TopBar
import com.stoyanvuchev.magicmessage.core.ui.effect.defaultHazeEffect
import com.stoyanvuchev.magicmessage.core.ui.event.NavigationEvent
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme
import com.stoyanvuchev.magicmessage.core.ui.transition.defaultBoundsTransformation
import com.stoyanvuchev.magicmessage.domain.model.CreationModel
import com.stoyanvuchev.magicmessage.presentation.main.MainScreen
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
    var sharedCreation by remember { mutableStateOf<CreationModel?>(null) }
    val boundsTransform = remember { defaultBoundsTransformation }
    val aspectRatio = remember { 3f / 4f }

    BackHandler(enabled = sharedCreation != null) { sharedCreation = null }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Theme.colors.surfaceElevationLow,
        contentColor = Theme.colors.onSurfaceElevationLow,
        topBar = {

            TopBar(
                modifier = Modifier.defaultHazeEffect(hazeState = hazeState),
                title = { Text(text = stringResource(R.string.home_screen_label)) }
            )

        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {

            if (sharedCreation == null) {

                AuraButton(
                    onClick = remember {
                        {
                            onNavigationEvent(
                                NavigationEvent.NavigateTo(
                                    MainScreen.Draw(null)
                                )
                            )
                        }
                    },
                    hazeState = hazeState,
                    isGlowVisible = true,
                    size = 56.dp
                ) {

                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.pencil_icon),
                        contentDescription = null
                    )

                }

            }

        },
        bottomBar = {

            Spacer(
                modifier = Modifier
                    .navigationBarsPadding()
                    .height(NavigationBarHeight)
            )

        }
    ) { innerPadding ->

        SharedTransitionLayout {

            var canvasSize by remember { mutableStateOf(IntSize.Zero) }

            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .hazeSource(
                        state = hazeState,
                        key = "home_items_source"
                    ),
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
                                .padding(top = 24.dp),
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
                                .aspectRatio(aspectRatio),
                            visible = visible,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {

                            HomeScreenCreationItem(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .sharedElement(
                                        sharedContentState = rememberSharedContentState(
                                            key = creation.id ?: 0
                                        ),
                                        animatedVisibilityScope = this@AnimatedVisibility,
                                        boundsTransform = boundsTransform,
                                    ),
                                creation = creation,
                                onNavigationEvent = onNavigationEvent,
                                onLongClick = remember {
                                    {
                                        if (canvasSize == IntSize.Zero) canvasSize = it
                                        sharedCreation = creation
                                    }
                                }
                            )

                        }

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
                                .aspectRatio(aspectRatio),
                            visible = visible,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {

                            HomeScreenCreationItem(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .sharedElement(
                                        sharedContentState = rememberSharedContentState(
                                            key = creation.id ?: 0
                                        ),
                                        animatedVisibilityScope = this@AnimatedVisibility,
                                        boundsTransform = boundsTransform,
                                    ),
                                creation = creation,
                                onNavigationEvent = onNavigationEvent,
                                onLongClick = remember {
                                    {
                                        if (canvasSize == IntSize.Zero) canvasSize = it
                                        sharedCreation = creation
                                    }
                                }
                            )

                        }
                    }

                }

                item(
                    span = { GridItemSpan(this.maxLineSpan) }
                ) {
                    Spacer(modifier = Modifier.height(164.dp))
                }

            }

            HomeScreenItemDetails(
                modifier = Modifier
                    .padding(bottom = NavigationBarHeight)
                    .fillMaxSize(),
                innerPadding = innerPadding,
                hazeState = hazeState,
                boundsTransform = boundsTransform,
                creation = sharedCreation,
                canvasSize = canvasSize,
                onDismiss = remember { { sharedCreation = null } },
                onUIAction = onUIAction,
                onNavigationEvent = onNavigationEvent
            )

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