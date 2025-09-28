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
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.stoyanvuchev.magicmessage.core.ui.components.EmptyBottomSpacer
import com.stoyanvuchev.magicmessage.core.ui.components.grid.GridCreationItemDetailsLayout
import com.stoyanvuchev.magicmessage.core.ui.components.grid.GridOfCreationItems
import com.stoyanvuchev.magicmessage.core.ui.components.grid.gridOfCreationItemsSection
import com.stoyanvuchev.magicmessage.core.ui.components.top_bar.TopBar
import com.stoyanvuchev.magicmessage.core.ui.event.NavigationEvent
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme
import com.stoyanvuchev.magicmessage.domain.model.CreationModel
import com.stoyanvuchev.magicmessage.presentation.main.MainScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreen(
    state: HomeScreenState,
    onUIAction: (HomeScreenUIAction) -> Unit,
    onNavigationEvent: (NavigationEvent) -> Unit
) {

    val lazyGridState = rememberLazyGridState()
    var sharedCreation by remember { mutableStateOf<CreationModel?>(null) }

    BackHandler(
        enabled = remember(sharedCreation) { sharedCreation != null },
        onBack = remember { { sharedCreation = null } }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Theme.colors.surfaceElevationLow,
        contentColor = Theme.colors.onSurfaceElevationLow,
        topBar = {

            TopBar(
                title = { Text(text = stringResource(R.string.home_screen_label)) },
                actions = {

                    AnimatedVisibility(
                        visible = sharedCreation != null,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {

                        IconButton(
                            onClick = remember { { sharedCreation = null } }
                        ) {

                            Icon(
                                painter = painterResource(R.drawable.close_outlined),
                                contentDescription = stringResource(R.string.dismiss_item_details),
                                tint = Theme.colors.onSurfaceElevationLow
                            )

                        }

                    }

                }
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
                    isGlowVisible = true,
                    size = 56.dp
                ) {

                    Icon(
                        modifier = Modifier.size(26.dp),
                        painter = painterResource(R.drawable.pencil_icon),
                        contentDescription = null
                    )

                }

            }

        },
        bottomBar = { EmptyBottomSpacer() }
    ) { innerPadding ->

        SharedTransitionLayout {

            val isEmptyPlaceholderVisible by rememberUpdatedState(
                state.exportedCreationsList.isEmpty()
                        && state.draftedCreationsList.isEmpty()
            )

            AnimatedVisibility(
                modifier = Modifier.fillMaxSize(),
                visible = isEmptyPlaceholderVisible,
                enter = fadeIn(),
                exit = fadeOut(animationSpec = tween(100))
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
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

            var canvasSize by remember { mutableStateOf(IntSize.Zero) }

            val onCreationClickLambda = remember<(CreationModel, IntSize) -> Unit> {
                { creation, _ ->
                    onNavigationEvent(
                        NavigationEvent.NavigateTo(
                            MainScreen.Draw(creation.id)
                        )
                    )
                }
            }

            val onCreationLongClickLambda = remember<(CreationModel, IntSize) -> Unit> {
                { creation, newCanvasSize ->
                    if (canvasSize == IntSize.Zero) canvasSize = newCanvasSize
                    sharedCreation = creation
                }
            }

            val isLightWeightBlurApplied by rememberUpdatedState(
                sharedCreation != null
            )

            GridOfCreationItems(
                isLightWeightBlurApplied = isLightWeightBlurApplied,
                lazyGridState = lazyGridState,
                innerPadding = innerPadding,
                hazeSourceKey = "home_screen_grid_key"
            ) {

                gridOfCreationItemsSection(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    label = R.string.drafts_label,
                    items = state.draftedCreationsList,
                    categoryKey = "home_drafted_items",
                    sharedCreation = sharedCreation,
                    onCreationClick = onCreationClickLambda,
                    onCreationLongClick = onCreationLongClickLambda
                )

                gridOfCreationItemsSection(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    label = R.string.exported_label,
                    items = state.exportedCreationsList,
                    categoryKey = "home_exported_items",
                    sharedCreation = sharedCreation,
                    onCreationClick = onCreationClickLambda,
                    onCreationLongClick = onCreationLongClickLambda
                )

            }

            GridCreationItemDetailsLayout(
                innerPadding = innerPadding,
                creation = sharedCreation,
                canvasSize = canvasSize,
                onDismiss = remember { { sharedCreation = null } },
                onCreationClick = onCreationClickLambda,
                onExportGif = remember {
                    { onUIAction(HomeScreenUIAction.ExportGif(it)) }
                },
                onMarkAsFavorite = remember {
                    { onUIAction(HomeScreenUIAction.AddToFavorite(it)) }
                },
                onRemoveAsFavorite = remember {
                    { onUIAction(HomeScreenUIAction.RemoveFromFavorite(it)) }
                },
                onMoveToTrash = remember {
                    { onUIAction(HomeScreenUIAction.MoveToTrash(it)) }
                }
            )

        }

    }

}