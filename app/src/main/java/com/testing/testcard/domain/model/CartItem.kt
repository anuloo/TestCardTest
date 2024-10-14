package com.testing.testcard.domain.model

data class CartItem(
    var product: Product,
    var quantity: Int,
    var appliedDiscount: Discount? = null,
    var freeItems: Int = 0,
) {

    fun applyDiscount(discount: Discount) {
        if (appliedDiscount == null) {
            appliedDiscount = discount
            // Apply discount logic (e.g., adjust price based on the discount type)
            // Assuming this applies a fixed or percentage discount to the price of the CartItem
            product.price -= (product.price * discount.value / 100)
        } else {
            throw IllegalStateException("Discount already applied to this item")
        }
    }
}
