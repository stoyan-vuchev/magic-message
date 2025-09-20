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

package com.stoyanvuchev.magicmessage.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.stoyanvuchev.magicmessage.data.local.CreationDao
import com.stoyanvuchev.magicmessage.data.local.CreationDatabase
import com.stoyanvuchev.magicmessage.data.local.StrokesConverter
import com.stoyanvuchev.magicmessage.domain.model.StrokeModel
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreationRepositoryTest {

    private lateinit var db: CreationDatabase
    private lateinit var dao: CreationDao
    private lateinit var repository: CreationRepositoryImpl

    @Before
    fun setUp() {

        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CreationDatabase::class.java
        ).apply { addTypeConverter(StrokesConverter()) }.build()

        dao = db.creationDao()
        repository = CreationRepositoryImpl(dao)

    }

    @After
    fun tearDown() {
        if (::db.isInitialized) {
            db.close()
        }
    }

    @Test
    fun saveOrUpdateDraft_inserts_whenNew() = runTest {
        val id = repository.saveOrUpdateDraft(null, emptyList())
        val loaded = repository.getById(id)

        assertThat(loaded).isNotNull()
        assertThat(loaded!!.id).isEqualTo(id)
        assertThat(loaded.isDraft).isEqualTo(true)
    }

    @Test
    fun saveOrUpdateDraft_updates_whenExisting() = runTest {
        val id = repository.saveOrUpdateDraft(null, emptyList())
        val updatedStrokes = listOf<StrokeModel>() // could populate later

        val sameId = repository.saveOrUpdateDraft(id, updatedStrokes)
        val loaded = repository.getById(sameId)

        assertThat(sameId).isEqualTo(id)
        assertThat(loaded!!.strokes).isEqualTo(updatedStrokes)
    }

    @Test
    fun markAsFinished_updatesDraftToFinished() = runTest {
        val id = repository.saveOrUpdateDraft(null, emptyList())
        repository.markAsFinished(id, "preview.png")

        val loaded = repository.getById(id)

        assertThat(loaded!!.isDraft).isEqualTo(false)
        assertThat(loaded.previewUri).isEqualTo("preview.png")
    }

    @Test
    fun deleteById_removesEntity() = runTest {
        val id = repository.saveOrUpdateDraft(null, emptyList())
        repository.deleteById(id)

        val loaded = repository.getById(id)
        assertThat(loaded).isNull()
    }

}