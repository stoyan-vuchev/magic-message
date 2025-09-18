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

package com.stoyanvuchev.magicmessage.presentation.boarding.get_started

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.R
import com.stoyanvuchev.magicmessage.core.ui.components.CheckboxField
import com.stoyanvuchev.magicmessage.core.ui.event.NavigationEvent
import com.stoyanvuchev.magicmessage.presentation.main.MainScreen
import com.stoyanvuchev.systemuibarstweaker.LocalSystemUIBarsTweaker
import com.stoyanvuchev.systemuibarstweaker.ScrimStyle

@Composable
fun GetStartedScreen(
    state: GetStartedScreenState,
    onUIAction: (GetStartedScreenUIAction) -> Unit,
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

    DisposableEffect(tweaker) {
        tweaker.tweakSystemBarsStyle(
            statusBarStyle = tweaker.statusBarStyle.copy(darkIcons = false),
            navigationBarStyle = tweaker.navigationBarStyle.copy(
                darkIcons = false,
                scrimStyle = ScrimStyle.None
            )
        )
        onDispose {}
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            modifier = Modifier
                .padding(24.dp)
                .size(200.dp),
            painter = painterResource(R.drawable.logo_icon),
            contentDescription = stringResource(R.string.app_name),
            tint = Color.White
        )

        val annotatedString = buildAnnotatedString {

            withStyle(
                SpanStyle(
                    color = Color.White,
                    fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
                    fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )
            ) {
                append("${stringResource(R.string.get_started_screen_title_one)} ")
            }

            withStyle(
                SpanStyle(
                    color = Color.White,
                    fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append("${stringResource(R.string.get_started_screen_title_two)} ")
            }

        }

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = annotatedString,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.weight(1f))

        CheckboxField(
            modifier = Modifier.padding(horizontal = 24.dp),
            tickColor = gradientEnd,
            label = buildAnnotatedString {

                withStyle(
                    SpanStyle(
                        color = Color.White,
                        fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                        fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
                    )
                ) {
                    append(stringResource(R.string.get_started_screen_agreement_one))
                    append("\n")
                    append(stringResource(R.string.get_started_screen_agreement_two))
                    append(" ")
                }

                withLink(
                    LinkAnnotation.Clickable(
                        tag = "privacy_policy",
                        linkInteractionListener = {
                            onUIAction(GetStartedScreenUIAction.ViewPrivacyPolicy())
                        }
                    )
                ) {

                    withStyle(
                        SpanStyle(
                            color = Color.White,
                            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append(stringResource(R.string.get_started_screen_privacy_policy))
                    }

                }

            },
            checked = state.isPrivacyPolicyChecked,
            onCheckedChange = remember {
                { onUIAction(GetStartedScreenUIAction.TogglePrivacyPolicy) }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        CheckboxField(
            modifier = Modifier.padding(horizontal = 24.dp),
            tickColor = gradientEnd,
            label = buildAnnotatedString {

                withStyle(
                    SpanStyle(
                        color = Color.White,
                        fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                        fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
                    )
                ) {
                    append(stringResource(R.string.get_started_screen_agreement_one))
                    append("\n")
                    append(stringResource(R.string.get_started_screen_agreement_two))
                    append(" ")
                }

                withLink(
                    LinkAnnotation.Clickable(
                        tag = "tos",
                        linkInteractionListener = {
                            onUIAction(GetStartedScreenUIAction.ViewTermsOfService())
                        }
                    )
                ) {

                    withStyle(
                        SpanStyle(
                            color = Color.White,
                            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append(stringResource(R.string.get_started_screen_tos))
                    }

                }

            },
            checked = state.isTermsOfServiceChecked,
            onCheckedChange = remember {
                { onUIAction(GetStartedScreenUIAction.ToggleTermsOfService) }
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            Button(
                onClick = remember {
                    {
                        onNavigationEvent(
                            NavigationEvent.NavigateTo(
                                MainScreen.Home
                            )
                        )
                    }
                },
                enabled = state.isPrivacyPolicyChecked && state.isTermsOfServiceChecked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    disabledContainerColor = Color.White.copy(.5f),
                    contentColor = gradientEnd,
                    disabledContentColor = gradientEnd
                ),
                shape = MaterialTheme.shapes.small
            ) {

                Row(
                    modifier = Modifier.animateContentSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Text(
                        text = stringResource(R.string.get_started_screen_btn_label),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    AnimatedVisibility(
                        visible = state.isPrivacyPolicyChecked && state.isTermsOfServiceChecked,
                        enter = fadeIn() + scaleIn(initialScale = .8f),
                        exit = fadeOut() + scaleOut(targetScale = .8f)
                    ) {

                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = null
                        )

                    }

                }

            }

        }

        Spacer(modifier = Modifier.height(32.dp))

    }

}