package com.testing.testcard.domain.usecase

import com.testing.testcard.common.DiscountType
import com.testing.testcard.domain.model.Discount
import com.testing.testcard.domain.model.ShoppingCart
import com.testing.testcard.domain.repository.CartRepository
import com.testing.testcard.domain.service.CartItemFinder
import com.testing.testcard.domain.service.DiscountValidator

interface ApplyDiscountUseCase {
    fun execute(discount: Discount)
}

class ApplyDiscountUseCaseImpl(
    private val cartRepository: CartRepository,
    private val discountValidator: DiscountValidator,
    private val cartItemFinder: CartItemFinder
) : ApplyDiscountUseCase {

    override fun execute(discount: Discount) {
        val cart = cartRepository.getCart()
        discountValidator.validate(discount)

        // Apply discount based on type
        when (discount.type) {
            DiscountType.PERCENTAGE -> applyPercentageDiscount(cart, discount)
            DiscountType.BUY_X_GET_Y_FREE -> applyBuyXGetYFree(cart, discount)
            DiscountType.COMBINATION -> applyCombinationDiscount(cart, discount)
            else -> throw IllegalArgumentException("Unsupported discount type")
        }
    }

    private fun applyPercentageDiscount(cart: ShoppingCart, discount: Discount) {
        cart.items.filter { it.product.category.id == discount.categoryId }
            .forEach { item ->
                if (!cartItemFinder.isDiscountAlreadyApplied(cart, discount)) {
                    item.applyDiscount(discount)
                    cart.appliedDiscounts.add(discount)
                }
            }
    }

    private fun applyBuyXGetYFree(cart: ShoppingCart, discount: Discount) {

        val eligibleItems = cart.items.filter { it.product.category.id == discount.categoryId }

        if (eligibleItems.isEmpty() || eligibleItems[0].quantity < discount.requiredQuantity) {
            return
        }

        //Calculate how many full groups of "Buy X, Get Y Free" can be applied
        val groups = eligibleItems[0].quantity / discount.requiredQuantity

        //Calculate paid items (total items - free items)
        val paidItems = eligibleItems[0].quantity - groups

        //Apply the discount only to the eligibleItems
        eligibleItems[0].applyDiscount(discount)

        //Adjust the quantity of items in the cart: paid items remain, and free items are subtracted
        eligibleItems[0].quantity = paidItems

        //Add the discount to the applied discounts list
        cart.appliedDiscounts.add(discount)
    }

    private fun applyCombinationDiscount(cart: ShoppingCart, discount: Discount) {
        val requiredItems = discount.requiredItems.map { requiredItem ->
            cart.items.find { it.product.id == requiredItem.product.id }
        }

        if (requiredItems.none { it == null }) {
            val timesApplicable = requiredItems
                .filterNotNull()
                .minOf { it.quantity }

            repeat(timesApplicable) {
                requiredItems.forEach { item ->
                    item?.applyDiscount(discount)
                }
            }
            cart.appliedDiscounts.add(discount)
        }
    }
}
