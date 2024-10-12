package com.testing.testcard.data.model

import com.testing.testcard.domain.model.ShoppingCart

data class ShoppingCartDto(
    val items: List<CartItemDto> = mutableListOf(),
    val appliedDiscounts: List<DiscountDto> = mutableListOf()
)

fun ShoppingCartDto.mapToDomain(): ShoppingCart {
    val items =
        this.items.map { it.mapToDomain() }.toMutableList()
    val appliedDiscounts = this.appliedDiscounts.map { it.mapToDomain() }
        .toMutableList()
    return ShoppingCart(items, appliedDiscounts)
}
