package com.stoyanvuchev.magicmessage.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import com.stoyanvuchev.systemuibarstweaker.ProvideSystemUIBarsTweaker

@Composable
fun MagicMessageTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) = ProvideSystemUIBarsTweaker {

    val context = LocalContext.current
    val colorScheme by rememberUpdatedState(
        if (darkTheme) dynamicDarkColorScheme(context)
        else dynamicLightColorScheme(context)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )

}