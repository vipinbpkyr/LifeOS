package com.lifeos.core.network.di

import com.lifeos.core.network.AiNetworkDataSource
import com.lifeos.core.network.OfflineFirstAiNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindAiNetworkDataSource(
        impl: OfflineFirstAiNetworkDataSource
    ): AiNetworkDataSource
}
