package com.example.presentation.di

import com.example.core_fcm.IntentProvider
import com.example.core_fcm.di.MAIN
import com.example.presentation.util.MainIntentProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MainAcitivityModule {
    @Binds
    @MAIN
    @Singleton
    abstract fun bindsIntentProvider(intentProvider: MainIntentProvider):IntentProvider
}