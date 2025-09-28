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

package com.stoyanvuchev.magicmessage.presentation.main.deleted_screen

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
import com.stoyanvuchev.magicmessage.core.ui.components.grid.GridDeletedCreationItemDetailsLayout
import com.stoyanvuchev.magicmessage.core.ui.components.grid.GridOfCreationItems
import com.stoyanvuchev.magicmessage.core.ui.components.grid.gridOfCreationItemsSection
import com.stoyanvuchev.magicmessage.core.ui.components.top_bar.TopBar
import com.stoyanvuchev.magicmessage.core.ui.event.NavigationEvent
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme
import com.stoyanvuchev.magicmessage.domain.model.CreationModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DeletedScreen(
    state: DeletedScreenState,
    onUIAction: (DeletedScreenUIAction) -> Unit,
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
                title = { Text(text = stringResource(R.string.deleted_screen_label)) },
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

        }
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
                        modifier = Modifier.size(80.dp),
                        painter = painterResource(R.drawable.delete),
                        contentDescription = null
                    )

                    Text(
                        text = stringResource(R.string.deleted_screen_empty_text),
                        style = Theme.typefaces.bodyLarge,
                        color = Theme.colors.onSurfaceElevationLow.copy(.5f)
                    )

                }

            }

            var canvasSize by remember { mutableStateOf(IntSize.Zero) }

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
                hazeSourceKey = "deleted_screen_grid_key"
            ) {

                gridOfCreationItemsSection(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    label = R.string.drafts_label,
                    items = state.draftedCreationsList,
                    categoryKey = "deleted_drafted_items",
                    sharedCreation = sharedCreation,
                    onCreationClick = onCreationLongClickLambda,
                    onCreationLongClick = onCreationLongClickLambda
                )

                gridOfCreationItemsSection(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    label = R.string.exported_label,
                    items = state.exportedCreationsList,
                    categoryKey = "deleted_exported_items",
                    sharedCreation = sharedCreation,
                    onCreationClick = onCreationLongClickLambda,
                    onCreationLongClick = onCreationLongClickLambda
                )

            }

            GridDeletedCreationItemDetailsLayout(
                innerPadding = innerPadding,
                creation = sharedCreation,
                canvasSize = canvasSize,
                onDismiss = remember { { sharedCreation = null } },
                onRestore = remember {
                    { onUIAction(DeletedScreenUIAction.RestoreCreation(it)) }
                },
                onPermanentlyDelete = remember {
                    { onUIAction(DeletedScreenUIAction.PermanentlyDeleteCreation(it)) }
                }
            )

        }

    }

}