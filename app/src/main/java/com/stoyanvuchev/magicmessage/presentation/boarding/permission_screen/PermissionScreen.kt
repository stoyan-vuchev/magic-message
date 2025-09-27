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

package com.stoyanvuchev.magicmessage.presentation.boarding.permission_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.stoyanvuchev.magicmessage.R
import com.stoyanvuchev.magicmessage.core.ui.theme.Theme

@Composable
fun PermissionScreen(
    isPermissionRevoked: Boolean,
    onUIAction: (PermissionScreenUIAction) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.surfaceElevationLow)
            .systemBarsPadding()
            .padding(
                horizontal = 48.dp,
                vertical = 32.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            modifier = Modifier.size(64.dp),
            painter = painterResource(R.drawable.permissions_outlined),
            contentDescription = null,
            tint = Theme.colors.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.permission_screen_label),
            style = Theme.typefaces.titleMedium,
            color = Theme.colors.onSurfaceElevationLow
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.permission_screen_description),
            style = Theme.typefaces.bodyMedium,
            color = Theme.colors.onSurfaceElevationLow
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(R.drawable.alerts_outlined),
                contentDescription = null,
                tint = Theme.colors.onSurfaceElevationLow
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.permission_screen_notifications_label),
                style = Theme.typefaces.bodyLarge,
                color = Theme.colors.onSurfaceElevationLow
            )

        }

        Spacer(modifier = Modifier.weight(1f))

        val uiAction by rememberUpdatedState(
            if (!isPermissionRevoked) PermissionScreenUIAction.RequestPermissions
            else PermissionScreenUIAction.OpenSettings
        )

        Button(
            onClick = remember(uiAction) { { onUIAction(uiAction) } },
            colors = ButtonDefaults.buttonColors(
                containerColor = Theme.colors.primary,
                contentColor = Theme.colors.onPrimary
            ),
            shape = Theme.shapes.largeShape
        ) {

            val text by rememberUpdatedState(
                stringResource(
                    if (!isPermissionRevoked) R.string.permission_screen_request_permission
                    else R.string.permission_screen_change_settings
                )
            )

            Text(
                text = text,
                style = Theme.typefaces.bodyLarge
            )

        }

        Spacer(modifier = Modifier.weight(.5f))

    }

}