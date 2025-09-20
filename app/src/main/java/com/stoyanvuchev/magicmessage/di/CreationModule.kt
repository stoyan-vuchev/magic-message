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

package com.stoyanvuchev.magicmessage.di

import android.app.Application
import androidx.room.Room
import com.stoyanvuchev.magicmessage.data.local.CreationDatabase
import com.stoyanvuchev.magicmessage.data.local.CreationTypeConverters
import com.stoyanvuchev.magicmessage.data.repository.CreationRepositoryImpl
import com.stoyanvuchev.magicmessage.domain.repository.CreationRepository
import com.stoyanvuchev.magicmessage.domain.usecase.CreationGetByIdUseCase
import com.stoyanvuchev.magicmessage.domain.usecase.CreationSaveOrUpdateUseCase
import com.stoyanvuchev.magicmessage.domain.usecase.CreationUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CreationModule {

    @Provides
    @Singleton
    fun provideStrokesConverter(): CreationTypeConverters = CreationTypeConverters()

    @Provides
    @Singleton
    fun provideCreationDatabase(
        app: Application,
        creationTypeConverters: CreationTypeConverters
    ): CreationDatabase {
        return Room.databaseBuilder(
            app.applicationContext,
            CreationDatabase::class.java,
            "creation_db"
        ).apply { addTypeConverter(creationTypeConverters) }.build()
    }

    @Provides
    @Singleton
    fun provideCreationRepository(
        db: CreationDatabase
    ): CreationRepository {
        return CreationRepositoryImpl(db.creationDao())
    }

    @Provides
    @Singleton
    fun provideCreationUseCases(
        repository: CreationRepository
    ): CreationUseCases {
        return CreationUseCases(
            saveOrUpdateUseCase = CreationSaveOrUpdateUseCase(repository),
            getByIdUseCase = CreationGetByIdUseCase(repository)
        )
    }

}