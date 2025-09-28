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

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.R
import com.stoyanvuchev.magicmessage.core.ui.components.bottom_nav_bar.BottomNavBarTokens.NavigationBarHeight
import com.stoyanvuchev.magicmessage.core.ui.components.list.ListOptionItem
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme
import com.stoyanvuchev.magicmessage.domain.model.CreationModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.GridCreationItemDetailsLayout(
    creation: CreationModel?,
    innerPadding: PaddingValues,
    canvasSize: IntSize,
    onDismiss: () -> Unit,
    onCreationClick: (CreationModel, IntSize) -> Unit,
    onExportGif: (CreationModel) -> Unit,
    onMarkAsFavorite: (Long) -> Unit,
    onRemoveAsFavorite: (Long) -> Unit,
    onMoveToTrash: (Long) -> Unit
) {

    val isBackgroundVisible by rememberUpdatedState(creation != null)
    val topPadding by remember {
        derivedStateOf { innerPadding.calculateTopPadding() }
    }

    AnimatedVisibility(
        modifier = Modifier.fillMaxSize(),
        visible = isBackgroundVisible,
        enter = remember { fadeIn() },
        exit = remember { fadeOut() }
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(
                    top = topPadding,
                    bottom = NavigationBarHeight
                )
                .background(
                    color = Theme.colors.surfaceElevationLow.copy(.75f)
                )
        )

    }

    AnimatedContent(
        modifier = Modifier
            .padding(bottom = NavigationBarHeight)
            .fillMaxSize(),
        targetState = creation,
        transitionSpec = remember { { fadeIn() togetherWith fadeOut() } },
        label = "GridItemDetailsLayoutAnimatedContent"
    ) { sharedCreation ->

        if (sharedCreation != null) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .padding(top = topPadding)
                    .clickable(
                        onClick = onDismiss,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                val density = LocalDensity.current
                val dpSize by remember {
                    derivedStateOf {
                        with(density) {
                            DpSize(
                                width = canvasSize.width.toDp(),
                                height = canvasSize.height.toDp()
                            )
                        }
                    }
                }

                GridCreationItem(
                    modifier = Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(
                                key = sharedCreation.id ?: 0
                            ),
                            animatedVisibilityScope = this@AnimatedContent
                        )
                        .size(dpSize),
                    creation = sharedCreation,
                    onCreationClick = onCreationClick,
                    onCreationLongClick = remember { { _, _ -> } }
                )

                Spacer(modifier = Modifier.height(64.dp))

                Column(
                    modifier = Modifier
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(
                                key = "${sharedCreation.id ?: 0}-bounds"
                            ),
                            animatedVisibilityScope = this@AnimatedContent
                        )
                        .padding(horizontal = 32.dp)
                ) {

                    if (sharedCreation.isDraft) {

                        ListOptionItem(
                            onClick = remember { { onExportGif(sharedCreation); onDismiss() } },
                            label = { Text(text = stringResource(R.string.export_gif)) },
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

                    var isFavorite by remember { mutableStateOf(sharedCreation.isFavorite) }

                    val favoritePainter by rememberUpdatedState(
                        painterResource(
                            if (isFavorite) R.drawable.favorite_filled
                            else R.drawable.favorite_outlined
                        )
                    )

                    val favoriteTitle by rememberUpdatedState(
                        stringResource(
                            if (!isFavorite) R.string.add_to_favorite
                            else R.string.remove_from_favorite
                        )
                    )

                    val onIsFavoriteLambda = remember(isFavorite) {
                        {
                            if (!isFavorite) {
                                isFavorite = true
                                onMarkAsFavorite(sharedCreation.id!!)
                            } else {
                                isFavorite = false
                                onRemoveAsFavorite(sharedCreation.id!!)
                            }
                        }
                    }

                    ListOptionItem(
                        onClick = onIsFavoriteLambda,
                        label = {

                            Text(
                                modifier = Modifier.animateContentSize(alignment = Alignment.Center),
                                text = favoriteTitle
                            )

                        },
                        icon = {

                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = favoritePainter,
                                contentDescription = null
                            )

                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ListOptionItem(
                        onClick = remember { { onMoveToTrash(creation?.id!!); onDismiss() } },
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

}