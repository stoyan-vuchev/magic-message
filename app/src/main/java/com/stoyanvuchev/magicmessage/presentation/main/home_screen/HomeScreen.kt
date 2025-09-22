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
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import com.stoyanvuchev.magicmessage.core.ui.components.list.ListOptionItem
import com.stoyanvuchev.magicmessage.core.ui.components.top_bar.TopBar
import com.stoyanvuchev.magicmessage.core.ui.etc.UIString
import com.stoyanvuchev.magicmessage.core.ui.event.NavigationEvent
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme
import com.stoyanvuchev.magicmessage.core.ui.transition.defaultBoundsTransformation
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
    val boundsTransform = remember { defaultBoundsTransformation }

    BackHandler(enabled = sharedCreation != null) { sharedCreation = null }

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
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.pencil_icon),
                        contentDescription = null
                    )

                }

            }

        },
        bottomBar = { EmptyBottomSpacer() }
    ) { innerPadding ->

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = state.exportedCreationsList.isEmpty()
                    && state.draftedCreationsList.isEmpty(),
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

        SharedTransitionLayout {

            var canvasSize by remember { mutableStateOf(IntSize.Zero) }

            GridOfCreationItems(
                lazyGridState = lazyGridState,
                innerPadding = innerPadding,
                hazeSourceKey = "home_screen_grid_key"
            ) {

                if (state.exportedCreationsList.isNotEmpty()) {

                    gridOfCreationItemsSection(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        label = UIString.Resource(resId = R.string.drafts_label),
                        items = state.draftedCreationsList,
                        categoryKey = "home_drafted_items",
                        sharedCreation = sharedCreation,
                        boundsTransform = boundsTransform,
                        onCreationClick = { creation ->
                            onNavigationEvent(
                                NavigationEvent.NavigateTo(
                                    MainScreen.Draw(creation.id)
                                )
                            )
                        },
                        onCreationLongClick = { creation, newCanvasSize ->
                            if (canvasSize == IntSize.Zero) canvasSize = newCanvasSize
                            sharedCreation = creation
                        }
                    )

                }

                if (state.exportedCreationsList.isNotEmpty()) {

                    gridOfCreationItemsSection(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        label = UIString.Resource(resId = R.string.exported_label),
                        items = state.exportedCreationsList,
                        categoryKey = "home_exported_items",
                        sharedCreation = sharedCreation,
                        boundsTransform = boundsTransform,
                        onCreationClick = { creation ->
                            onNavigationEvent(
                                NavigationEvent.NavigateTo(
                                    MainScreen.Draw(creation.id)
                                )
                            )
                        },
                        onCreationLongClick = { creation, newCanvasSize ->
                            if (canvasSize == IntSize.Zero) canvasSize = newCanvasSize
                            sharedCreation = creation
                        }
                    )

                }

            }

            GridCreationItemDetailsLayout(
                innerPadding = innerPadding,
                boundsTransform = boundsTransform,
                creation = sharedCreation,
                canvasSize = canvasSize,
                onDismiss = remember { { sharedCreation = null } },
                onCreationClick = remember {
                    {
                        onNavigationEvent(
                            NavigationEvent.NavigateTo(
                                MainScreen.Draw(sharedCreation?.id)
                            )
                        )
                    }
                }
            ) {

                if (sharedCreation?.isDraft == true) {

                    ListOptionItem(
                        modifier = Modifier.padding(horizontal = 32.dp),
                        onClick = remember {
                            {
                                sharedCreation?.let {
                                    onUIAction(HomeScreenUIAction.ExportGif(it))
                                    sharedCreation = null
                                }
                            }
                        },
                        label = { Text(text = "Export GIF") },
                        icon = {

                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(R.drawable.export),
                                contentDescription = null
                            )

                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                }

                var isFavorite by remember {
                    mutableStateOf(sharedCreation?.isFavorite == true)
                }

                ListOptionItem(
                    modifier = Modifier.padding(horizontal = 32.dp),
                    onClick = remember(isFavorite) {
                        {
                            onUIAction(
                                if (!isFavorite) {
                                    isFavorite = true
                                    HomeScreenUIAction.AddToFavorite(sharedCreation?.id)
                                } else {
                                    isFavorite = false
                                    HomeScreenUIAction.RemoveFromFavorite(sharedCreation?.id)
                                }
                            )
                        }
                    },
                    label = {

                        Text(
                            modifier = Modifier.animateContentSize(
                                alignment = Alignment.Center
                            ),
                            text = stringResource(
                                if (!isFavorite) R.string.add_to_favorite
                                else R.string.remove_from_favorite
                            )
                        )

                    },
                    icon = {

                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(
                                if (isFavorite) R.drawable.favorite_filled
                                else R.drawable.favorite_outlined
                            ),
                            contentDescription = null
                        )

                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                ListOptionItem(
                    modifier = Modifier.padding(horizontal = 32.dp),
                    onClick = remember { {} },
                    label = { Text(text = stringResource(R.string.move_to_trash)) },
                    icon = {

                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.delete),
                            contentDescription = null
                        )

                    }
                )

            }

        }

    }

}