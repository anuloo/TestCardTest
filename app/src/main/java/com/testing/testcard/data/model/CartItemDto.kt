package com.testing.testcard.data.model

import com.testing.testcard.domain.model.CartItem
import com.testing.testcard.domain.model.Discount

data class CartItemDto(
    var product: ProductDto,
    var quantity: Int,
    var appliedDiscount: Discount? = null,
    var freeItems: Int = 0,
)

fun CartItemDto.mapToDomain(): CartItem {
    return CartItem(product.mapToDomain(), quantity, appliedDiscount, freeItems)
}

