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

package com.stoyanvuchev.magicmessage.presentation.main.about_screen

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.BuildConfig
import com.stoyanvuchev.magicmessage.R
import com.stoyanvuchev.magicmessage.core.ui.components.top_bar.TopBar
import com.stoyanvuchev.magicmessage.core.ui.event.NavigationEvent
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme
import com.stoyanvuchev.magicmessage.core.ui.theme.isInDarkThemeMode
import com.stoyanvuchev.systemuibarstweaker.LocalSystemUIBarsTweaker
import com.stoyanvuchev.systemuibarstweaker.ScrimStyle

@Composable
fun AboutScreen(
    onUIAction: (AboutScreenUIAction) -> Unit,
    onNavigationEvent: (NavigationEvent) -> Unit
) {

    val infiniteTransition = rememberInfiniteTransition(label = "")

    val gradientStart by infiniteTransition.animateColor(
        initialValue = Color.Black,
        targetValue = Color.Black,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                Color(0xFF1A80E5) at 0
                Color(0xFF009688) at 3000
                Color(0xFFFF9800) at 6000
                Color(0xFFE91E63) at 12000
                durationMillis = 12000
            },
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    val gradientEnd by infiniteTransition.animateColor(
        initialValue = Color.Black,
        targetValue = Color.Black,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                Color(0xFF3C1AE5) at 0
                Color(0xFF1A80E5) at 3000
                Color(0xFF009688) at 6000
                durationMillis = 6000
            },
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    val background by rememberUpdatedState(
        Brush.verticalGradient(
            colors = listOf(gradientStart, gradientEnd)
        )
    )

    val tweaker = LocalSystemUIBarsTweaker.current
    val isInDarkTheme = isInDarkThemeMode()

    DisposableEffect(tweaker, isInDarkTheme) {
        tweaker.tweakSystemBarsStyle(
            statusBarStyle = tweaker.statusBarStyle.copy(darkIcons = false),
            navigationBarStyle = tweaker.navigationBarStyle.copy(
                darkIcons = false,
                scrimStyle = ScrimStyle.None
            )
        )
        onDispose {
            tweaker.tweakSystemBarsStyle(
                statusBarStyle = tweaker.statusBarStyle.copy(darkIcons = !isInDarkTheme),
                navigationBarStyle = tweaker.navigationBarStyle.copy(
                    darkIcons = !isInDarkTheme,
                    scrimStyle = ScrimStyle.None
                )
            )
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(background),
        containerColor = Color.Transparent,
        contentColor = Color.Transparent,
        topBar = {

            TopBar(
                title = {

                    Text(
                        text = stringResource(R.string.about_screen_label),
                        color = Color.White
                    )

                },
                navigationAction = {

                    IconButton(
                        onClick = remember { { onNavigationEvent(NavigationEvent.NavigateUp) } }
                    ) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            painter = painterResource(R.drawable.arrow_left_outlined),
                            contentDescription = stringResource(R.string.navigate_back),
                            tint = Color.White
                        )
                    }

                },
                enableBlur = false,
                dividerColor = Color.White.copy(.08f)
            )

        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                modifier = Modifier.size(100.dp),
                painter = painterResource(R.drawable.logo_icon),
                contentDescription = stringResource(R.string.app_name),
                tint = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.app_name),
                color = Color.White,
                style = Theme.typefaces.titleSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = remember { BuildConfig.VERSION_NAME },
                color = Color.White,
                style = Theme.typefaces.bodyLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            OutlinedButton(
                onClick = remember { { onUIAction(AboutScreenUIAction.PrivacyPolicy) } },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.White.copy(.5f)
                ),
                shape = Theme.shapes.largeShape
            ) {

                Text(
                    text = stringResource(R.string.privacy_policy),
                    style = Theme.typefaces.bodyLarge,
                    color = Color.White
                )

            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = remember { { onUIAction(AboutScreenUIAction.TermsOfService) } },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.White.copy(.5f)
                ),
                shape = Theme.shapes.largeShape
            ) {

                Text(
                    text = stringResource(R.string.terms_of_service),
                    style = Theme.typefaces.bodyLarge,
                    color = Color.White
                )

            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = remember { { onUIAction(AboutScreenUIAction.GitHub) } },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.White.copy(.5f)
                ),
                shape = Theme.shapes.largeShape
            ) {

                Text(
                    text = stringResource(R.string.source_code),
                    style = Theme.typefaces.bodyLarge,
                    color = Color.White
                )

            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.developed_by),
                style = Theme.typefaces.bodySmall,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.developer_name),
                style = Theme.typefaces.bodyLarge,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = remember {
                        { onUIAction(AboutScreenUIAction.DevGitHub) }
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.White
                    )
                ) {

                    Icon(
                        painter = painterResource(R.drawable.github_icon),
                        contentDescription = null
                    )

                }

                IconButton(
                    onClick = remember {
                        { onUIAction(AboutScreenUIAction.DevLinkedIn) }
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.White
                    )
                ) {

                    Icon(
                        painter = painterResource(R.drawable.linkedin_icon),
                        contentDescription = null
                    )

                }

                IconButton(
                    onClick = remember {
                        { onUIAction(AboutScreenUIAction.DevX) }
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.White
                    )
                ) {

                    Icon(
                        painter = painterResource(R.drawable.x_social_icon),
                        contentDescription = null
                    )

                }

            }

            Spacer(modifier = Modifier.weight(1f))

        }

    }

}