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

package com.stoyanvuchev.magicmessage.presentation.main.draw_screen.dialog

import android.content.Context
import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.stoyanvuchev.magicmessage.R
import com.stoyanvuchev.magicmessage.core.ui.effect.defaultHazeEffect
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme
import com.stoyanvuchev.magicmessage.framework.export.ExporterState
import com.stoyanvuchev.magicmessage.presentation.main.draw_screen.DrawScreenUIAction
import dev.chrisbanes.haze.HazeState

@Composable
fun DrawScreenExportDialog(
    exporterProgress: Int,
    exporterState: ExporterState,
    exportedUri: Uri?,
    hazeState: HazeState,
    onUIAction: (DrawScreenUIAction) -> Unit,
) {

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .padding(horizontal = 40.dp)
                    .fillMaxWidth()
                    .clip(Theme.shapes.mediumShape)
                    .defaultHazeEffect(hazeState = hazeState)
                    .border(
                        width = 1.dp,
                        color = Theme.colors.outline,
                        shape = Theme.shapes.mediumShape
                    )
                    .background(Theme.colors.surfaceElevationHigh.copy(.33f))
                    .padding(16.dp)
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        alignment = Alignment.Center
                    )
            ) {

                val title by rememberUpdatedState(
                    stringResource(
                        when (exporterState) {
                            ExporterState.IDLE -> R.string.exporter_label_preparing
                            ExporterState.PREPARING -> R.string.exporter_label_preparing
                            ExporterState.EXPORTING -> R.string.exporter_label_exporting
                            ExporterState.COMPLETED -> R.string.exporter_label_completed
                        }
                    )
                )

                Text(
                    text = title,
                    style = Theme.typefaces.titleSmall,
                    color = Theme.colors.onSurfaceElevationHigh
                )

                Spacer(modifier = Modifier.height(16.dp))

                when (exporterState) {

                    ExporterState.IDLE -> Unit

                    ExporterState.PREPARING -> {

                        Text(
                            text = stringResource(R.string.exporter_label_preparing_description),
                            style = Theme.typefaces.bodyMedium,
                            color = Theme.colors.onSurfaceElevationHigh.copy(.75f)
                        )

                    }

                    ExporterState.EXPORTING -> {

                        Text(
                            text = stringResource(R.string.exporter_label_exporting_description) + "  $exporterProgress%",
                            style = Theme.typefaces.bodyMedium,
                            color = Theme.colors.onSurfaceElevationHigh.copy(.75f)
                        )

                    }

                    ExporterState.COMPLETED -> {

                        Text(
                            text = stringResource(R.string.exporter_label_completed_description),
                            style = Theme.typefaces.bodyMedium,
                            color = Theme.colors.onSurfaceElevationHigh.copy(.75f)
                        )

                    }

                }

                when (exporterState) {

                    ExporterState.IDLE -> {

                        Spacer(modifier = Modifier.height(16.dp))

                        LinearProgressIndicator(
                            color = Theme.colors.primary,
                            trackColor = Theme.colors.outline
                        )

                    }

                    ExporterState.PREPARING -> {

                        Spacer(modifier = Modifier.height(16.dp))

                        LinearProgressIndicator(
                            color = Theme.colors.primary,
                            trackColor = Theme.colors.outline
                        )

                    }

                    ExporterState.EXPORTING -> {

                        Spacer(modifier = Modifier.height(16.dp))

                        LinearProgressIndicator(
                            progress = { exporterProgress / 100f },
                            color = Theme.colors.primary,
                            trackColor = Color.Transparent,
                            drawStopIndicator = {}
                        )

                    }

                    ExporterState.COMPLETED -> {

                        Spacer(modifier = Modifier.height(16.dp))

                        exportedUri?.let {

                            val context = LocalContext.current
                            val painter = rememberDrawablePainter(
                                drawable = createAnimatedImageDrawableFromImageDecoder(context, it)
                            )

                            Image(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(3f / 4f),
                                painter = painter,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {

                            val uriHandler = LocalUriHandler.current

                            TextButton(
                                onClick = {
                                    onUIAction(DrawScreenUIAction.DismissExporterDialog)
                                },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = Theme.colors.primary
                                )
                            ) {
                                Text(
                                    text = stringResource(R.string.exporter_label_cancel),
                                    style = Theme.typefaces.bodyLarge
                                )
                            }

                            TextButton(
                                onClick = {
                                    exportedUri?.let {
                                        uriHandler.openUri(it.toString())
                                        onUIAction(DrawScreenUIAction.DismissExporterDialog)
                                    }
                                },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = Theme.colors.primary
                                )
                            ) {
                                Text(
                                    text = stringResource(R.string.exporter_label_view),
                                    style = Theme.typefaces.bodyLarge
                                )
                            }

                        }

                    }

                }

            }

        }

    }

}

private fun createAnimatedImageDrawableFromImageDecoder(
    context: Context,
    uri: Uri
): AnimatedImageDrawable {
    val source = ImageDecoder.createSource(context.contentResolver, uri)
    val drawable = ImageDecoder.decodeDrawable(source)
    return drawable as AnimatedImageDrawable
}