package com.testing.testcard.data.model

data class ShoppingCartDto(
    val items: List<CartItemDto> = mutableListOf(),
    val appliedDiscounts: List<DiscountDto> = mutableListOf()
)
