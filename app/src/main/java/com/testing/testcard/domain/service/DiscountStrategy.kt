package com.testing.testcard.domain.service

import com.testing.testcard.common.DiscountType
import com.testing.testcard.domain.model.Discount
import com.testing.testcard.domain.model.ShoppingCart

interface DiscountStrategy {
    fun applyDiscount(cart: ShoppingCart, discount: Discount)
    fun supportedType(discountType: DiscountType): Boolean
}
