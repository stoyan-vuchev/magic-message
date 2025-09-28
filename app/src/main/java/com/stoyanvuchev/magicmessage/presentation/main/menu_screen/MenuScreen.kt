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

package com.stoyanvuchev.magicmessage.presentation.main.menu_screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.R
import com.stoyanvuchev.magicmessage.core.ui.components.EmptyBottomSpacer
import com.stoyanvuchev.magicmessage.core.ui.components.RowSelectionItem
import com.stoyanvuchev.magicmessage.core.ui.components.list.ListOfOptionItems
import com.stoyanvuchev.magicmessage.core.ui.components.list.ListOptionItemDetailsLayout
import com.stoyanvuchev.magicmessage.core.ui.components.top_bar.TopBar
import com.stoyanvuchev.magicmessage.core.ui.event.NavigationEvent
import com.stoyanvuchev.magicmessage.core.ui.theme.LocalThemeMode
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme
import com.stoyanvuchev.magicmessage.core.ui.theme.ThemeMode
import com.stoyanvuchev.magicmessage.core.ui.theme.color.ColorScheme
import com.stoyanvuchev.magicmessage.core.ui.theme.color.LocalColorPalette
import com.stoyanvuchev.magicmessage.core.ui.theme.color.label
import com.stoyanvuchev.magicmessage.core.ui.theme.color.toColorPalette
import com.stoyanvuchev.magicmessage.core.ui.theme.icon
import com.stoyanvuchev.magicmessage.core.ui.theme.isInDarkThemeMode
import com.stoyanvuchev.magicmessage.core.ui.theme.label
import com.stoyanvuchev.magicmessage.core.ui.transition.defaultBoundsTransformation

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MenuScreen(
    onUIAction: (MenuScreenUIAction) -> Unit,
    onNavigationEvent: (NavigationEvent) -> Unit
) {

    val lazyListState = rememberLazyListState()
    val boundsTransform = remember { defaultBoundsTransformation }
    var sharedKey by remember { mutableStateOf<String?>(null) }
    val onSharedKeyLambda = remember<(String?) -> Unit> { { sharedKey = it } }

    BackHandler(enabled = sharedKey != null) { sharedKey = null }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Theme.colors.surfaceElevationLow,
        contentColor = Theme.colors.onSurfaceElevationLow,
        topBar = {

            TopBar(
                title = { Text(text = stringResource(R.string.menu_screen_label)) },
                actions = {

                    AnimatedVisibility(
                        visible = sharedKey != null,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {

                        IconButton(
                            onClick = remember { { sharedKey = null } }
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
        bottomBar = { EmptyBottomSpacer() }
    ) { innerPadding ->

        SharedTransitionLayout {

            val isLightWeightBlueApplied by rememberUpdatedState(
                sharedKey != null
            )

            ListOfOptionItems(
                isLightWeightBlueApplied = isLightWeightBlueApplied,
                lazyListState = lazyListState,
                innerPadding = innerPadding,
                hazeSourceKey = "menu_screen_list_key"
            ) {

                menuScreenPersonalizationSection(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    sharedKey = sharedKey,
                    boundsTransform = boundsTransform,
                    onSharedKey = onSharedKeyLambda
                )

                menuScreenCreationSection(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    sharedKey = sharedKey,
                    boundsTransform = boundsTransform,
                    onNavigationEvent = onNavigationEvent
                )

                menuScreenOtherSection(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    sharedKey = sharedKey,
                    boundsTransform = boundsTransform,
                    onNavigationEvent = onNavigationEvent
                )

            }

            val label by rememberUpdatedState(
                when (sharedKey) {

                    "theme_mode" -> {
                        stringResource(R.string.theme_mode) +
                                ": " + LocalThemeMode.current.label()
                    }

                    "color_scheme" -> {
                        stringResource(R.string.color_scheme) +
                                ": " + LocalColorPalette.current.scheme.label()
                    }

                    "trash" -> stringResource(R.string.trash_label)
                    "about" -> stringResource(R.string.about_label)
                    else -> stringResource(R.string.app_name)

                }
            )

            val painter by rememberUpdatedState(
                when (sharedKey) {
                    "theme_mode" -> LocalThemeMode.current.icon()
                    "color_scheme" -> painterResource(R.drawable.color_scheme_outlined)
                    "trash" -> painterResource(R.drawable.delete)
                    "about" -> painterResource(R.drawable.info_outlined)
                    else -> painterResource(R.drawable.logo_icon)
                }
            )

            ListOptionItemDetailsLayout(
                label = label,
                painter = painter,
                sharedKey = sharedKey,
                innerPadding = innerPadding,
                boundsTransform = boundsTransform,
                onSharedKey = onSharedKeyLambda
            ) {

                when (sharedKey) {

                    "theme_mode" -> {

                        ThemeMode.entries.forEach { themeMode ->

                            RowSelectionItem(
                                onClick = remember {
                                    {
                                        onUIAction(
                                            MenuScreenUIAction.SetThemeMode(themeMode)
                                        )
                                    }
                                },
                                selected = themeMode == LocalThemeMode.current,
                                label = { Text(text = themeMode.label()) },
                                icon = {

                                    Icon(
                                        painter = themeMode.icon(),
                                        contentDescription = null
                                    )

                                }
                            )

                        }

                    }

                    "color_scheme" -> {

                        ColorScheme.entries.forEach { colorScheme ->

                            val darkTheme = isInDarkThemeMode()
                            val color by rememberUpdatedState(
                                colorScheme.toColorPalette(darkTheme).primary
                            )

                            RowSelectionItem(
                                onClick = remember {
                                    {
                                        onUIAction(
                                            MenuScreenUIAction.SetColorScheme(colorScheme)
                                        )
                                    }
                                },
                                selected = colorScheme == LocalColorPalette.current.scheme,
                                label = { Text(text = colorScheme.label()) },
                                icon = {

                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .border(
                                                width = 1.dp,
                                                color = Theme.colors.onSurfaceElevationLow
                                                    .copy(.5f),
                                                shape = CircleShape
                                            )
                                            .padding(3.dp)
                                            .background(
                                                color = color,
                                                shape = CircleShape
                                            )
                                    )

                                }
                            )

                        }

                    }

                }

            }

        }

    }

}