package com.testing.testcard.domain.service

import com.testing.testcard.common.DiscountType
import com.testing.testcard.domain.model.Discount
import com.testing.testcard.domain.model.ShoppingCart
import javax.inject.Inject

class CombinationDiscountStrategy @Inject constructor(
) : DiscountStrategy {
    override fun applyDiscount(cart: ShoppingCart, discount: Discount) {
        val requiredItems = discount.requiredItems.map { requiredItem ->
            val matchedItem = cart.items.find { cartItem ->
                cartItem.product.id == requiredItem.product.id
            }
            matchedItem
        }

        val validItems = requiredItems.filterNotNull()

        if (validItems.size == requiredItems.size) {
            val timesApplicable = validItems.minOf { it.quantity }

            repeat(timesApplicable) {
                requiredItems.forEach { item ->
                    item?.applyDiscount(discount)
                }
            }
            cart.appliedDiscounts.add(discount)
        }
    }

    override fun supportedType(discountType: DiscountType): Boolean {
        return discountType == DiscountType.COMBINATION
    }
}
