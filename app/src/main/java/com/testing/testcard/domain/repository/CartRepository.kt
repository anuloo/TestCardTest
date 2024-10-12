package com.testing.testcard.domain.repository

import com.testing.testcard.domain.model.Discount
import com.testing.testcard.domain.model.Product
import com.testing.testcard.domain.model.ShoppingCart

interface CartRepository {
    fun addProduct(product: Product, quantity: Int)
    fun applyDiscount(cart: ShoppingCart, discount: Discount)
    fun getCart(): ShoppingCart
}