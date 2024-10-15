package com.testing.testcard.domain.usecase

import com.testing.testcard.common.DiscountType
import com.testing.testcard.domain.model.Discount
import com.testing.testcard.domain.model.ShoppingCart
import com.testing.testcard.domain.repository.CartRepository
import com.testing.testcard.domain.service.BuyXGetYFreeDiscountStrategy
import com.testing.testcard.domain.service.CartItemFinder
import com.testing.testcard.domain.service.CombinationDiscountStrategy
import com.testing.testcard.domain.service.DiscountStrategy
import com.testing.testcard.domain.service.DiscountValidator
import com.testing.testcard.domain.service.PercentageDiscountStrategy

interface ApplyDiscountUseCase {
    fun execute(discount: Discount)
}

class ApplyDiscountUseCaseImpl(
    private val cartRepository: CartRepository,
    private val discountValidator: DiscountValidator,
    private val discountStrategies: Set<DiscountStrategy>
) : ApplyDiscountUseCase {
    override fun execute(discount: Discount) {
        val cart = cartRepository.getCart()
        discountValidator.validate(discount)

        // Get the appropriate discount strategy based on the discount type
        val applicableStrategy = discountStrategies.find { it.supportedType(discount.type) }

        applicableStrategy?.applyDiscount(cart, discount)
            ?: throw IllegalStateException("No valid discount strategy found")
    }
}
