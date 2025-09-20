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

package com.stoyanvuchev.magicmessage.data.local

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.stoyanvuchev.magicmessage.domain.brush.BrushEffect
import com.stoyanvuchev.magicmessage.domain.model.StrokeModel
import com.stoyanvuchev.magicmessage.domain.model.TimedPoint
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreationDatabaseTest {

    private lateinit var db: CreationDatabase
    private lateinit var dao: CreationDao

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CreationDatabase::class.java
        ).apply { addTypeConverter(StrokesConverter()) }.build()
        dao = db.creationDao()
    }

    @After
    fun tearDown() {
        if (::db.isInitialized) {
            db.close()
        }
    }

    @Test
    fun insertAndGetLatestDraft() = runTest {
        val strokes = listOf(
            StrokeModel(
                points = listOf(
                    TimedPoint(offset = Offset(x = 0f, y = 0f), timestamp = 0L),
                    TimedPoint(offset = Offset(x = 10f, y = 10f), timestamp = 16L)
                ),
                color = Color.Red,
                width = 5f,
                effect = BrushEffect.NONE
            )
        )

        val creation = CreationEntity(
            id = 0, // auto-gen
            strokes = strokes,
            createdAt = System.currentTimeMillis(),
            isDraft = true,
            isFavorite = true,
            previewUri = null
        )

        val id = dao.insert(creation)
        val draft = dao.getById(id)

        assertThat(draft).isNotNull()
        assertThat(draft?.id).isEqualTo(id)
        assertThat(draft?.strokes?.first()?.points?.size).isEqualTo(2)

    }

    @Test
    fun markDraftAsFinished() = runTest {
        val creation = CreationEntity(
            id = 0,
            strokes = emptyList(),
            createdAt = System.currentTimeMillis(),
            isDraft = true,
            isFavorite = true,
            previewUri = null
        )

        val id = dao.insert(creation)
        dao.update(creation.copy(isDraft = false, previewUri = "content://preview"))
        val draft = dao.getById(id)

        assertThat(draft?.isDraft).isEqualTo(false)

    }

    @Test
    fun deleteCreation() = runTest {
        val id = dao.insert(
            CreationEntity(
                id = 0,
                strokes = emptyList(),
                createdAt = System.currentTimeMillis(),
                isDraft = true,
                isFavorite = true,
                previewUri = null
            )
        )
        val creation = dao.getById(id)
        dao.delete(creation!!)
        val actual = dao.getById(id)
        assertThat(actual).isNull()
    }

    @Test
    fun deleteAllDrafts() = runTest {

        (0L..2L).forEach { id ->
            dao.insert(
                CreationEntity(
                    id = id,
                    strokes = emptyList(),
                    createdAt = System.currentTimeMillis(),
                    isDraft = true,
                    isFavorite = true,
                    previewUri = null
                )
            )
        }

        dao.deleteAllDrafts()

        val drafts = dao.getAllDrafts()
        assertThat(drafts).isEqualTo(emptyList())

    }

    @Test
    fun deleteAllCreations() = runTest {

        (0L..2L).forEach { id ->
            dao.insert(
                CreationEntity(
                    id = id,
                    strokes = emptyList(),
                    createdAt = System.currentTimeMillis(),
                    isDraft = false,
                    isFavorite = true,
                    previewUri = null
                )
            )
        }

        dao.deleteAllCreations()

        val drafts = dao.getAll()
        assertThat(drafts).isEqualTo(emptyList())

    }

}