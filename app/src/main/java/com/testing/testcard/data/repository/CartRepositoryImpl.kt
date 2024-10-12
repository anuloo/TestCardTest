package com.testing.testcard.data.repository

import com.testing.testcard.domain.model.Discount
import com.testing.testcard.domain.model.Product
import com.testing.testcard.domain.model.ShoppingCart
import com.testing.testcard.domain.repository.CartRepository

class CartRepositoryImpl:CartRepository {
    override fun addProduct(product: Product, quantity: Int) {
        TODO("Not yet implemented")
    }

    override fun applyDiscount(cart: ShoppingCart, discount: Discount) {
        TODO("Not yet implemented")
    }

    override fun getCart(): ShoppingCart {
        TODO("Not yet implemented")
    }
}