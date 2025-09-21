package com.stoyanvuchev.magicmessage.presentation.main.home_screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.R
import com.stoyanvuchev.magicmessage.core.ui.components.bottom_nav_bar.BottomNavBarTokens.NavigationBarHeight
import com.stoyanvuchev.magicmessage.core.ui.components.list.ListItemOption
import com.stoyanvuchev.magicmessage.core.ui.effect.defaultHazeEffect
import com.stoyanvuchev.magicmessage.core.ui.event.NavigationEvent
import com.stoyanvuchev.magicmessage.domain.model.CreationModel
import dev.chrisbanes.haze.HazeState

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeScreenItemDetails(
    modifier: Modifier = Modifier,
    creation: CreationModel?,
    innerPadding: PaddingValues,
    hazeState: HazeState,
    boundsTransform: (Rect, Rect) -> SpringSpec<Rect>,
    canvasSize: IntSize,
    onDismiss: () -> Unit,
    onUIAction: (HomeScreenUIAction) -> Unit,
    onNavigationEvent: (NavigationEvent) -> Unit
) {

    val isBackgroundVisible by rememberUpdatedState(creation != null)

    AnimatedVisibility(
        modifier = Modifier.fillMaxSize(),
        visible = isBackgroundVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(bottom = NavigationBarHeight)
                .defaultHazeEffect(hazeState = hazeState)
                .padding(top = innerPadding.calculateTopPadding())
        )

    }

    AnimatedContent(
        modifier = modifier,
        targetState = creation,
        transitionSpec = remember { { fadeIn() togetherWith fadeOut() } },
        label = "HomeScreenItemDetails"
    ) { sharedCreation ->

        if (sharedCreation != null) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .padding(top = innerPadding.calculateTopPadding())
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

                HomeScreenCreationItem(
                    modifier = Modifier
                        .size(dpSize)
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(
                                key = sharedCreation.id ?: 0
                            ),
                            animatedVisibilityScope = this@AnimatedContent,
                            boundsTransform = boundsTransform,
                        ),
                    creation = sharedCreation,
                    onNavigationEvent = onNavigationEvent,
                    onLongClick = remember { {} }
                )

                Spacer(modifier = Modifier.height(64.dp))

                if (sharedCreation.isDraft) {

                    ListItemOption(
                        modifier = Modifier.padding(horizontal = 32.dp),
                        onClick = remember {
                            {
                                onUIAction(HomeScreenUIAction.ExportGif(sharedCreation))
                                onDismiss()
                            }
                        },
                        hazeState = hazeState,
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

                var isFavorite by remember { mutableStateOf(sharedCreation.isFavorite) }

                ListItemOption(
                    modifier = Modifier.padding(horizontal = 32.dp),
                    onClick = remember { { isFavorite = !isFavorite } },
                    hazeState = hazeState,
                    label = {

                        Text(
                            modifier = Modifier.animateContentSize(
                                alignment = Alignment.Center
                            ),
                            text = if (isFavorite) "Remove from Favorite"
                            else "Add to Favorite"
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

                ListItemOption(
                    modifier = Modifier.padding(horizontal = 32.dp),
                    onClick = remember { {} },
                    hazeState = hazeState,
                    label = { Text(text = "Move to Trash") },
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