package com.testing.testcard.data.model

import com.testing.testcard.common.DiscountType
import com.testing.testcard.domain.model.CartItem
import com.testing.testcard.domain.model.Discount

data class DiscountDto(
    val productId: String?,
    val categoryId: String?,
    val type: DiscountType,
    val value: Double,
    val requiredQuantity: Int = 0,  // For Buy X Get Y Free deals, how many to buy
    val requiredItems: List<CartItem> = emptyList()  // For combination deals, a list of required items
)

fun DiscountDto.mapToDomain(): Discount {
    return Discount(
        productId = productId,
        categoryId = categoryId,
        type = type,
        value = value,
        requiredQuantity = requiredQuantity,
        requiredItems = requiredItems
    )
}
