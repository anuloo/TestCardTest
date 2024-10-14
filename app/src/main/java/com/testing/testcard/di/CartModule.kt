package com.testing.testcard.di

import com.testing.testcard.data.repository.CartRepositoryImpl
import com.testing.testcard.domain.repository.CartRepository
import com.testing.testcard.domain.service.CartItemFinder
import com.testing.testcard.domain.service.DiscountValidator
import com.testing.testcard.domain.usecase.AddProductToCartUseCase
import com.testing.testcard.domain.usecase.AddProductToCartUseCaseImpl
import com.testing.testcard.domain.usecase.ApplyDiscountUseCase
import com.testing.testcard.domain.usecase.ApplyDiscountUseCaseImpl
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

    @Provides
    @Singleton
    fun provideAddProductToCartUseCase(
        cartRepository: CartRepository
    ): AddProductToCartUseCase {
        return AddProductToCartUseCaseImpl(cartRepository)
    }

    @Provides
    @Singleton
    fun provideCartItemFinder(): CartItemFinder {
        return CartItemFinder()
    }

    @Provides
    @Singleton
    fun provideDiscountValidator(): DiscountValidator {
        return DiscountValidator()
    }

    @Provides
    @Singleton
    fun provideApplyDiscountUseCase(
        cartRepository: CartRepository,
        discountValidator: DiscountValidator,
        cartItemFinder: CartItemFinder
    ): ApplyDiscountUseCase {
        return ApplyDiscountUseCaseImpl(cartRepository, discountValidator, cartItemFinder)
    }
}
