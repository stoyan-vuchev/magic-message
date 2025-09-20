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

import com.stoyanvuchev.magicmessage.data.local.CreationDao
import com.stoyanvuchev.magicmessage.data.local.CreationEntity
import com.stoyanvuchev.magicmessage.domain.model.CreationModel
import com.stoyanvuchev.magicmessage.domain.model.DrawConfiguration
import com.stoyanvuchev.magicmessage.domain.model.DrawingSnapshot
import com.stoyanvuchev.magicmessage.domain.model.StrokeModel
import com.stoyanvuchev.magicmessage.domain.repository.CreationRepository
import com.stoyanvuchev.magicmessage.mappers.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CreationRepositoryImpl @Inject constructor(
    private val dao: CreationDao
) : CreationRepository {

    override suspend fun saveOrUpdateDraft(
        draftId: Long?,
        drawConfiguration: DrawConfiguration,
        drawingSnapshot: DrawingSnapshot
    ): Long {

        suspend fun insert() = dao.insert(
            CreationEntity(
                id = draftId,
                isDraft = true,
                isFavorite = false,
                createdAt = System.currentTimeMillis(),
                drawConfiguration = drawConfiguration,
                drawingSnapshot = drawingSnapshot
            )
        )

        return if (draftId != null) {
            val creation = dao.getById(draftId)
            if (creation != null) {
                dao.update(
                    creation.copy(
                        drawConfiguration = drawConfiguration,
                        drawingSnapshot = drawingSnapshot
                    )
                )
                draftId
            } else insert()
        } else insert()

    }

    override suspend fun markAsFinished(messageId: Long) {
        val creation = dao.getById(messageId) ?: return
        dao.update(creation.copy(isDraft = false))
    }

    override suspend fun markAsFavorite(id: Long) {
        val creation = dao.getById(id) ?: return
        dao.update(creation.copy(isFavorite = true))
    }

    override suspend fun removeAsFavorite(id: Long) {
        val creation = dao.getById(id) ?: return
        dao.update(creation.copy(isFavorite = false))
    }

    override fun getDrafts(): Flow<List<CreationModel>> {
        return dao.getAllDrafts().map { it.map { e -> e.toModel() } }
    }

    override fun getFinished(): Flow<List<CreationModel>> {
        return dao.getAll().map { it.map { e -> e.toModel() } }
    }

    override suspend fun getById(id: Long): CreationModel? {
        return dao.getById(id)?.toModel()
    }

    override suspend fun deleteAllDrafts() {
        dao.deleteAllDrafts()
    }

    override suspend fun deleteAllCreations() {
        dao.deleteAllCreations()
    }

    override suspend fun deleteById(id: Long) {
        dao.delete(dao.getById(id) ?: return)
    }

}