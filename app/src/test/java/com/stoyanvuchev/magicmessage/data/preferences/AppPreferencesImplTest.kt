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
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.File

class AppPreferencesImplTest {

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var appPreferences: AppPreferencesImpl
    private lateinit var testFile: File

    @Before
    fun setup() {
        testFile = File.createTempFile("datastore_test", ".preferences_pb")
        dataStore = PreferenceDataStoreFactory.create(produceFile = { testFile })
        appPreferences = AppPreferencesImpl(dataStore)
    }

    @After
    fun cleanup() {
        testFile.delete()
    }

    @Test
    fun `default getIsBoardingComplete returns false`() = runTest {
        val result = appPreferences.getIsBoardingComplete()
        assertEquals(false, result)
    }

    @Test
    fun `setIsBoardingComplete saves true`() = runTest {
        appPreferences.setIsBoardingComplete(true)
        val result = appPreferences.getIsBoardingComplete()
        assertEquals(true, result)
    }

    @Test
    fun `setIsBoardingComplete saves false`() = runTest {
        appPreferences.setIsBoardingComplete(false)
        val result = appPreferences.getIsBoardingComplete()
        assertEquals(false, result)
    }

}
