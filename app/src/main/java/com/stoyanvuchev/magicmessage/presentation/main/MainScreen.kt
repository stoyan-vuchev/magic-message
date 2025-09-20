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

package com.stoyanvuchev.magicmessage.presentation.main

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.stoyanvuchev.magicmessage.R
import com.stoyanvuchev.magicmessage.core.ui.navigation.NavigationScreen
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
sealed class MainScreen(
    @param:StringRes val label: Int,
    @param:DrawableRes val icon: Int
) : Parcelable, NavigationScreen() {

    @Serializable
    data object Navigation : MainScreen(
        label = R.string.home_screen_label,
        icon = R.drawable.home_outlined
    )

    @Serializable
    data object Home : MainScreen(
        label = R.string.home_screen_label,
        icon = R.drawable.home_outlined
    )

    @Serializable
    data object Favorite : MainScreen(
        label = R.string.favorite_screen_label,
        icon = R.drawable.favorite_outlined
    )

    @Serializable
    data object Menu : MainScreen(
        label = R.string.menu_screen_label,
        icon = R.drawable.menu_outlined
    )

    @Serializable
    data class Draw(val messageId: Long) : MainScreen(
        label = R.string.draw_screen_label,
        icon = R.drawable.logo_icon
    )

}

val mainScreenNavDestinations: List<MainScreen> = listOf(
    MainScreen.Home,
    MainScreen.Favorite,
    MainScreen.Menu
)