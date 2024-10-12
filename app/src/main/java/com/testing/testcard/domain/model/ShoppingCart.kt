package com.testing.testcard.domain.model

data class ShoppingCart(
    val items: List<CartItem> = mutableListOf(),
    val appliedDiscounts: List<Discount> = mutableListOf()
)
