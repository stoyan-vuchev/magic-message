package com.stoyanvuchev.magicmessage.core.ui.components.grid

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.core.ui.components.bottom_nav_bar.BottomNavBarTokens.NavigationBarHeight
import com.stoyanvuchev.magicmessage.core.ui.effect.defaultHazeEffect
import com.stoyanvuchev.magicmessage.domain.model.CreationModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.GridCreationItemDetailsLayout(
    creation: CreationModel?,
    innerPadding: PaddingValues,
    boundsTransform: (Rect, Rect) -> SpringSpec<Rect>,
    canvasSize: IntSize,
    onDismiss: () -> Unit,
    onCreationClick: (CreationModel) -> Unit,
    actions: @Composable ColumnScope.() -> Unit
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
                .defaultHazeEffect()
                .padding(top = innerPadding.calculateTopPadding())
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

                GridCreationItem(
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
                            animatedVisibilityScope = this@AnimatedContent,
                            boundsTransform = boundsTransform,
                        )
                ) {

                    actions()

                }

            }

        }

    }

}