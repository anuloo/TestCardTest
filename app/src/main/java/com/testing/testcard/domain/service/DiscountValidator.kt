package com.testing.testcard.domain.service

import com.testing.testcard.domain.model.Discount

class DiscountValidator {
    fun validate(discount: Discount) {
        if (discount.value <= 0) {
            throw IllegalArgumentException("Discount value must be greater than zero")
        }
    }
}