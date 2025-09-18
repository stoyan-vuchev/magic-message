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

package com.stoyanvuchev.magicmessage.core.ui.components.interaction

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun rememberRipple(
    color: Color = Theme.colors.primary
): Ripple {
    val ripple by rememberUpdatedState(Ripple(color))
    return ripple
}

@Stable
class Ripple(
    private val color: Color
) : IndicationNodeFactory {

    override fun create(
        interactionSource: InteractionSource
    ): DelegatableNode = ScaleIndicationNode(
        interactionSource = interactionSource,
        color = color
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is Ripple) return false
        if (color != other.color) return false
        return true
    }

    override fun hashCode(): Int = color.hashCode()

    private class ScaleIndicationNode(
        private val interactionSource: InteractionSource,
        private val color: Color
    ) : Modifier.Node(), DrawModifierNode {

        var drawSize = Size.Zero
        var currentPressPositionX = Animatable(drawSize.center.x)
        var currentPressPositionY = Animatable(drawSize.center.y)

        var alpha = Animatable(0f)
        var alphaFactor = Animatable(.1f)

        private suspend fun animateToPressed(
            pressPosition: Offset
        ) = coroutineScope {
            launch { currentPressPositionX.animateTo(pressPosition.x) }
            launch { currentPressPositionY.animateTo(pressPosition.y) }
            launch {
                alpha.animateTo(
                    .4f,
                    spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
            launch {
                alphaFactor.animateTo(
                    .5f,
                    spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
        }

        private suspend fun animateToHovered() = coroutineScope {
            launch { currentPressPositionX.animateTo(drawSize.center.x) }
            launch { currentPressPositionY.animateTo(drawSize.center.y) }
            launch {
                alpha.animateTo(
                    .075f,
                    spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
            launch {
                alphaFactor.animateTo(
                    .67f,
                    spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
        }

        private suspend fun animateToResting() = coroutineScope {
            launch {
                alpha.animateTo(
                    0f,
                    spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
            launch {
                alphaFactor.animateTo(
                    .1f,
                    spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
        }

        override fun onAttach() {
            coroutineScope.launch {
                interactionSource.interactions.collectLatest { interaction ->
                    when (interaction) {
                        is PressInteraction.Press -> animateToPressed(interaction.pressPosition)
                        is PressInteraction.Release -> animateToResting()
                        is PressInteraction.Cancel -> animateToResting()
                        is HoverInteraction.Exit -> animateToResting()
                        is HoverInteraction.Enter -> animateToHovered()
                    }
                }
            }
        }

        override fun ContentDrawScope.draw() {
            this@draw.drawContent()
            drawSize = lazy { this@draw.size }.value
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        color.copy(alpha.value),
                        color.copy(0f)
                    ),
                    center = Offset(
                        currentPressPositionX.value,
                        currentPressPositionY.value
                    ),
                    radius = this.size.maxDimension * alphaFactor.value
                ),
                radius = this.size.maxDimension,
                center = Offset(
                    currentPressPositionX.value,
                    currentPressPositionY.value
                )
            )
        }

    }

}