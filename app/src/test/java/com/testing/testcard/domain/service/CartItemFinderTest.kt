package com.testing.testcard.domain.service

import com.testing.testcard.common.DiscountType
import com.testing.testcard.domain.model.CartItem
import com.testing.testcard.domain.model.Category
import com.testing.testcard.domain.model.Discount
import com.testing.testcard.domain.model.Product
import com.testing.testcard.domain.model.ShoppingCart
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNull

class CartItemFinderTest {

    private lateinit var cartItemFinder: CartItemFinder

    @Before
    fun setup() {
        cartItemFinder = CartItemFinder()
    }

    @Test
    fun `should return false if discount is not applied to product`() {
        val cart = ShoppingCart(
            items = mutableListOf(
                CartItem(
                    product = Product(
                        id = "product2",
                        name = "apple",
                        price = 100.0,
                        category = Category(id = "category2", name = "Fruits")
                    ),
                    quantity = 1
                )
            ),
            appliedDiscounts = mutableListOf()  // No discounts applied
        )

        val discount = Discount(
            productId = "product1",
            categoryId = "category1",
            type = DiscountType.PERCENTAGE,
            value = 10.0,
            requiredQuantity = 0,
            requiredItems = listOf()
        )

        // Assert that no discount is applied
        assertFalse(cartItemFinder.isDiscountAlreadyApplied(cart, discount))
    }

    @Test
    fun `should return null when product does not exist in the cart`() {
        val cart = ShoppingCart(items = mutableListOf())

        val discount = Discount(
            productId = "invalid",
            categoryId = "invalid",
            type = DiscountType.PERCENTAGE,
            value = 10.0,
            requiredQuantity = 0,
            requiredItems = listOf()
        )

        val item = cartItemFinder.findItem(cart, discount)

        assertNull(item, "Expected null because the product does not exist in the cart")
    }

}
