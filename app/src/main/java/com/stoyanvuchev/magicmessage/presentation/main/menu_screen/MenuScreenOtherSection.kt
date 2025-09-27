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
import com.stoyanvuchev.magicmessage.core.ui.event.NavigationEvent
import com.stoyanvuchev.magicmessage.presentation.main.MainScreen

@OptIn(ExperimentalSharedTransitionApi::class)
fun LazyListScope.menuScreenOtherSection(
    sharedTransitionScope: SharedTransitionScope,
    sharedKey: String?,
    boundsTransform: (Rect, Rect) -> SpringSpec<Rect>,
    onNavigationEvent: (NavigationEvent) -> Unit
) = listOfOptionItemsSelection(
    sharedTransitionScope = sharedTransitionScope,
    label = UIString.Resource(resId = R.string.menu_screen_other_label),
    categoryKey = "other_item",
    boundsTransform = boundsTransform
) { categoryKey, scope, bounds ->

    with(scope) {

        item(key = "${categoryKey}_about") {

            val aboutKey = remember { "about" }
            val onSharedKeyLambda = remember {
                { onNavigationEvent(NavigationEvent.NavigateTo(MainScreen.About)) }
            }

            AnimatedVisibility(
                modifier = Modifier
                    .animateItem()
                    .fillMaxWidth(),
                visible = sharedKey != aboutKey,
                enter = fadeIn(),
                exit = fadeOut()
            ) {

                Box(
                    modifier = Modifier
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "$aboutKey-bounds"),
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
                                    sharedContentState = rememberSharedContentState(key = "$aboutKey-label"),
                                    animatedVisibilityScope = this@AnimatedVisibility,
                                    boundsTransform = boundsTransform,
                                ),
                                text = stringResource(R.string.about_label)
                            )

                        },
                        icon = {

                            Icon(
                                modifier = Modifier.sharedElement(
                                    sharedContentState = rememberSharedContentState(key = "$aboutKey-icon"),
                                    animatedVisibilityScope = this@AnimatedVisibility,
                                    boundsTransform = boundsTransform,
                                ),
                                painter = painterResource(R.drawable.info_outlined),
                                contentDescription = null
                            )

                        }
                    )

                }
            }

        }

    }

}