package com.testing.testcard.domain.service

import com.testing.testcard.common.DiscountType
import com.testing.testcard.domain.model.Discount
import com.testing.testcard.domain.model.ShoppingCart
import javax.inject.Inject

class PercentageDiscountStrategy @Inject constructor(
    private val cartItemFinder: CartItemFinder
) : DiscountStrategy {
    override fun applyDiscount(cart: ShoppingCart, discount: Discount) {
        val eligibleItems = cartItemFinder.filterItemByCategory(cart, discount)
        eligibleItems.forEach { item ->
            if (!cartItemFinder.isDiscountAlreadyApplied(cart, discount)) {
                item.applyDiscount(discount)
                cart.appliedDiscounts.add(discount)
            }
        }
    }

    override fun supportedType(discountType: DiscountType): Boolean {
        return discountType == DiscountType.PERCENTAGE
    }
}
