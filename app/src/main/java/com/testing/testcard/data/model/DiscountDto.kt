package com.testing.testcard.data.model

import com.testing.testcard.common.DiscountType
import com.testing.testcard.domain.model.Discount

data class DiscountDto(
    val productId: String?,
    val categoryId: String?,
    val type: DiscountType,
    val value: Double
)

fun DiscountDto.mapToDomain(): Discount {
    return Discount(
        productId = productId,
        categoryId = categoryId,
        type = type,
        value = value
    )
}
