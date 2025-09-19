package com.stoyanvuchev.magicmessage.di

import android.app.Application
import com.stoyanvuchev.magicmessage.framework.Exporter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExporterModule {

    @Provides
    @Singleton
    fun provideExporter(
        app: Application
    ): Exporter {
        return Exporter(context = app.applicationContext)
    }

}