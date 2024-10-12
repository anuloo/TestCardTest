package com.testing.testcard.domain.model

import com.testing.testcard.common.DiscountType

data class Discount(
    val productId: String?,
    val categoryId: String?,
    val type: DiscountType,
    val value: Double
)
