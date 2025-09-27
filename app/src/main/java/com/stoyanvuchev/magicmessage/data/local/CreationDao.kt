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

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CreationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(creation: CreationEntity): Long

    @Update
    suspend fun update(creation: CreationEntity)

    @Delete
    suspend fun delete(creation: CreationEntity)

    @Query("DELETE FROM creations WHERE isDraft IS 1")
    suspend fun deleteAllDrafts()

    @Query("DELETE FROM creations")
    suspend fun deleteAllCreations()

    // Exported

    fun getAll(
        onlyFavorite: Boolean,
        deleted: Boolean
    ): Flow<List<CreationEntity>> {
        return if (onlyFavorite) getAllExportedFavorite(deleted)
        else getAllExported(deleted)
    }

    @Query("SELECT * FROM creations WHERE isDeleted = :deleted AND isDraft IS FALSE AND isFavorite IS TRUE ORDER BY createdAt DESC")
    fun getAllExportedFavorite(deleted: Boolean): Flow<List<CreationEntity>>

    @Query("SELECT * FROM creations WHERE isDeleted = :deleted AND isDraft IS FALSE ORDER BY createdAt DESC")
    fun getAllExported(deleted: Boolean): Flow<List<CreationEntity>>

    // Drafts

    fun getAllDrafts(
        onlyFavorite: Boolean,
        deleted: Boolean
    ): Flow<List<CreationEntity>> {
        return if (onlyFavorite) getAllFavoriteDrafts(deleted)
        else getDrafts(deleted)
    }

    @Query("SELECT * FROM creations WHERE isDeleted = :deleted AND isDraft IS TRUE ORDER BY createdAt DESC")
    fun getDrafts(deleted: Boolean): Flow<List<CreationEntity>>

    @Query("SELECT * FROM creations WHERE isDeleted = :deleted AND isDraft IS TRUE AND isFavorite IS TRUE ORDER BY createdAt DESC")
    fun getAllFavoriteDrafts(deleted: Boolean): Flow<List<CreationEntity>>

    // By ID

    @Query("SELECT * FROM creations WHERE id = :id")
    suspend fun getById(id: Long): CreationEntity?

}
