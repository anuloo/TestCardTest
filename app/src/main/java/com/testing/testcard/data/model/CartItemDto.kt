package com.testing.testcard.data.model

import com.testing.testcard.domain.model.CartItem

data class CartItemDto(
    val product: ProductDto,
    var quantity: Int
)

fun CartItemDto.mapToDomain(): CartItem {
    return CartItem(product.mapToDomain(), quantity)
}

