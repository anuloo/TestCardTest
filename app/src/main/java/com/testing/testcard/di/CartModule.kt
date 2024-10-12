package com.testing.testcard.di

import com.testing.testcard.data.repository.CartRepositoryImpl
import com.testing.testcard.domain.repository.CartRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CartModule {

    @Provides
    @Singleton
    fun provideCartRepository(): CartRepository {
        return CartRepositoryImpl()
    }
}