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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.stoyanvuchev.magicmessage.R
import com.stoyanvuchev.magicmessage.core.ui.components.list.ListOptionItem
import com.stoyanvuchev.magicmessage.core.ui.components.list.listOfOptionItemsSelection
import com.stoyanvuchev.magicmessage.core.ui.etc.UIString
import com.stoyanvuchev.magicmessage.core.ui.theme.LocalThemeMode
import com.stoyanvuchev.magicmessage.core.ui.theme.icon
import com.stoyanvuchev.magicmessage.core.ui.theme.label

@OptIn(ExperimentalSharedTransitionApi::class)
fun LazyListScope.menuScreenPersonalizationSection(
    sharedTransitionScope: SharedTransitionScope,
    sharedKey: String?,
    boundsTransform: (Rect, Rect) -> SpringSpec<Rect>,
    onSharedKey: (String?) -> Unit
) = listOfOptionItemsSelection(
    sharedTransitionScope = sharedTransitionScope,
    label = UIString.Resource(resId = R.string.menu_screen_personalization_label),
    categoryKey = "personalization_item",
    boundsTransform = boundsTransform
) { categoryKey, scope, bounds ->

    with(scope) {

        item(key = "${categoryKey}_theme_mode") {

            val themeModeKey = remember { "theme_mode" }
            val onSharedKeyLambda = remember { { onSharedKey(themeModeKey) } }
            val themeMode = LocalThemeMode.current

            AnimatedVisibility(
                modifier = Modifier
                    .animateItem()
                    .fillMaxWidth(),
                visible = sharedKey != themeModeKey,
                enter = fadeIn(),
                exit = fadeOut()
            ) {

                Box(
                    modifier = Modifier
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "$themeModeKey-bounds"),
                            animatedVisibilityScope = this@AnimatedVisibility,
                            boundsTransform = boundsTransform,
                        )
                ) {

                    ListOptionItem(
                        modifier = Modifier,
                        onClick = onSharedKeyLambda,
                        onLongClick = onSharedKeyLambda,
                        label = {

                            Text(
                                modifier = Modifier.sharedElement(
                                    sharedContentState = rememberSharedContentState(key = "$themeModeKey-label"),
                                    animatedVisibilityScope = this@AnimatedVisibility,
                                    boundsTransform = boundsTransform,
                                ),
                                text = stringResource(R.string.theme_mode) + ": " + themeMode.label()
                            )

                        },
                        icon = {

                            Icon(
                                modifier = Modifier.sharedElement(
                                    sharedContentState = rememberSharedContentState(key = "$themeModeKey-icon"),
                                    animatedVisibilityScope = this@AnimatedVisibility,
                                    boundsTransform = boundsTransform,
                                ),
                                painter = themeMode.icon(),
                                contentDescription = null
                            )

                        }
                    )

                }
            }

        }

        item(key = "${categoryKey}_color_scheme") {

            val colorSchemeKey = remember { "color_scheme" }
            val onSharedKeyLambda = remember { { /*TODO*/ } }

            AnimatedVisibility(
                modifier = Modifier
                    .animateItem()
                    .fillMaxWidth(),
                visible = sharedKey != colorSchemeKey,
                enter = fadeIn(),
                exit = fadeOut()
            ) {

                Box(
                    modifier = Modifier
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "$colorSchemeKey-bounds"),
                            animatedVisibilityScope = this@AnimatedVisibility,
                            boundsTransform = boundsTransform,
                        )
                ) {

                    ListOptionItem(
                        modifier = Modifier,
                        onClick = onSharedKeyLambda,
                        onLongClick = onSharedKeyLambda,
                        label = {

                            Text(
                                modifier = Modifier.sharedElement(
                                    sharedContentState = rememberSharedContentState(key = "$colorSchemeKey-label"),
                                    animatedVisibilityScope = this@AnimatedVisibility,
                                    boundsTransform = boundsTransform,
                                ),
                                text = stringResource(R.string.color_scheme)
                            )

                        },
                        icon = {

                            Icon(
                                modifier = Modifier.sharedElement(
                                    sharedContentState = rememberSharedContentState(key = "$colorSchemeKey-icon"),
                                    animatedVisibilityScope = this@AnimatedVisibility,
                                    boundsTransform = boundsTransform,
                                ),
                                painter = painterResource(R.drawable.color_scheme_outlined),
                                contentDescription = null
                            )

                        }
                    )

                }
            }

        }

    }

}