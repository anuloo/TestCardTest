package com.testing.testcard.domain.model

import com.testing.testcard.common.DiscountType

data class Discount(
    val productId: String?,
    val categoryId: String?,
    val type: DiscountType,
    val value: Double,
    val requiredQuantity: Int = 0,  // For Buy X Get Y Free deals, how many to buy
    val requiredItems: List<CartItem> = emptyList()  // For combination deals, a list of required items
)
