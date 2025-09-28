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

package com.stoyanvuchev.magicmessage.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.stoyanvuchev.magicmessage.core.ui.theme.ThemeMode
import com.stoyanvuchev.magicmessage.core.ui.theme.color.ColorScheme
import com.stoyanvuchev.magicmessage.domain.preferences.AppPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

class AppPreferencesImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : AppPreferences {

    companion object {
        private val IS_BOARDING_COMPLETE_KEY = booleanPreferencesKey("is_boarding_complete")
        private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
        private val COLOR_SCHEME_KEY = stringPreferencesKey("color_scheme")
    }

    // Boarding

    override fun getIsBoardingComplete(): Flow<Boolean?> {
        return dataStore.data.map { it[IS_BOARDING_COMPLETE_KEY] ?: false }
    }

    override suspend fun setIsBoardingComplete(isComplete: Boolean) {
        dataStore.edit { it[IS_BOARDING_COMPLETE_KEY] = isComplete }
    }

    // Theme Mode

    override fun getThemeMode(): Flow<ThemeMode> {
        return dataStore.data.map {
            it[THEME_MODE_KEY]?.let { mode ->
                Json.decodeFromString<ThemeMode?>(mode)
            } ?: ThemeMode.SYSTEM
        }
    }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        val json = Json.encodeToString(themeMode)
        dataStore.edit { it[THEME_MODE_KEY] = json }
    }

    // Color Scheme

    override fun getColorScheme(): Flow<ColorScheme> {
        return dataStore.data.map {
            it[COLOR_SCHEME_KEY]?.let { scheme ->
                Json.decodeFromString<ColorScheme?>(scheme)
            } ?: ColorScheme.DYNAMIC
        }
    }

    override suspend fun setColorScheme(colorScheme: ColorScheme) {
        val json = Json.encodeToString(colorScheme)
        dataStore.edit { it[COLOR_SCHEME_KEY] = json }
    }

}