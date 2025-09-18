package com.stoyanvuchev.magicmessage.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.stoyanvuchev.magicmessage.R

val Fixel = FontFamily(
    Font(R.font.fixel_text_regular, weight = FontWeight.Normal),
    Font(R.font.fixel_text_medium, weight = FontWeight.Medium),
    Font(R.font.fixel_display_regular, weight = FontWeight.Normal),
    Font(R.font.fixel_display_medium, weight = FontWeight.Medium)
)

val Caveat = FontFamily(
    Font(R.font.caveat_medium, weight = FontWeight.Medium)
)

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = Caveat,
        fontWeight = FontWeight.Medium,
        fontSize = 46.sp,
        lineHeight = 50.sp,
        letterSpacing = 0.0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = Fixel,
        fontWeight = FontWeight.Medium,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.0.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = Fixel,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.0.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = Fixel,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = Fixel,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.2.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = Fixel,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.3.sp,
    )
)