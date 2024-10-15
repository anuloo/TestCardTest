package com.testing.testcard.domain.service

import com.testing.testcard.domain.model.CartItem
import com.testing.testcard.domain.model.Discount
import com.testing.testcard.domain.model.ShoppingCart

class CartItemFinder {

    // Find items in the cart based on the category of the discount
    fun filterItemByCategory(cart: ShoppingCart, discount: Discount): List<CartItem> {
        return cart.items.filter { it.product.category.id == discount.categoryId }
    }

    // Find the item in the cart based on discount's product
    fun findItem(cart: ShoppingCart, discount: Discount): CartItem? {
        return cart.items.find { it.product.category.id == discount.categoryId }
    }

    // Check if a discount is already applied to the product
    fun isDiscountAlreadyApplied(cart: ShoppingCart, discount: Discount): Boolean {
        return cart.appliedDiscounts.any { it.productId == discount.productId }
    }
}
