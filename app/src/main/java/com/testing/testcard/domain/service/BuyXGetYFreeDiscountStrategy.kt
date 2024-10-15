package com.testing.testcard.domain.service

import com.testing.testcard.common.DiscountType
import com.testing.testcard.domain.model.Discount
import com.testing.testcard.domain.model.ShoppingCart
import javax.inject.Inject

class BuyXGetYFreeDiscountStrategy @Inject constructor(
    private val cartItemFinder: CartItemFinder
) : DiscountStrategy {
    override fun applyDiscount(cart: ShoppingCart, discount: Discount) {
        val eligibleItems = cartItemFinder.filterItemByCategory(cart, discount)

        if (eligibleItems.isEmpty() || eligibleItems[0].quantity < discount.requiredQuantity) {
            return
        }

        // Calculate how many full groups of "Buy X, Get Y Free" can be applied
        val groups = eligibleItems[0].quantity / discount.requiredQuantity

        // Calculate paid items (total items - free items)
        val paidItems = eligibleItems[0].quantity - groups

        // Apply the discount only to the eligibleItems
        eligibleItems[0].applyDiscount(discount)

        // Adjust the quantity of items in the cart: paid items remain, and free items are subtracted
        eligibleItems[0].quantity = paidItems

        // Add the discount to the applied discounts list
        cart.appliedDiscounts.add(discount)
    }

    override fun supportedType(discountType: DiscountType): Boolean {
        return discountType == DiscountType.BUY_X_GET_Y_FREE
    }
}
