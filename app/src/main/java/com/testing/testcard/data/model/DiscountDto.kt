package com.testing.testcard.data.model

import com.testing.testcard.common.DiscountType

data class DiscountDto(
    val productId: String?,
    val categoryId: String?,
    val type: DiscountType,
    val value: Double
)
