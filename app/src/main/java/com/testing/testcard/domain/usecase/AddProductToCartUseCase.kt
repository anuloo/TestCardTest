package com.testing.testcard.domain.usecase

import com.testing.testcard.domain.model.Product
import com.testing.testcard.domain.repository.CartRepository

interface AddProductToCartUseCase {
    fun execute(product: Product, quantity: Int)
}

class AddProductToCartUseCaseImpl(
    private val cartRepository: CartRepository
) : AddProductToCartUseCase {
    override fun execute(product: Product, quantity: Int) {

        if (quantity <= 0) {
            throw IllegalArgumentException("Quantity must be greater than zero")
        }

        cartRepository.addProduct(product, quantity)
    }
}