package com.testing.testcard.domain.model

data class ShoppingCart(
    val items: MutableList<CartItem> = mutableListOf(),
    val appliedDiscounts: MutableList<Discount> = mutableListOf()
)
