package com.testing.testcard.domain.repository

import com.testing.testcard.common.DiscountType
import com.testing.testcard.data.repository.CartRepositoryImpl
import com.testing.testcard.domain.model.Category
import com.testing.testcard.domain.model.Discount
import com.testing.testcard.domain.model.Product
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class CartRepositoryTest {

    private lateinit var cartRepository: CartRepositoryImpl
    private lateinit var product: Product
    private lateinit var category: Category

    @Before
    fun setup() {
        cartRepository = CartRepositoryImpl()

        category = Category("1", "Test Category")

        // Mock product
        product = Product(id = "1", name = "Test Product", price = 10.0, category = category)

    }

    @Test
    fun `should fail no product added when getCart is called`() {
        // Assert
        val cart = cartRepository.getCart()
        assertEquals(0, cart.items.size)
    }

    @Test
    fun `should pass add product to the cart when addProduct is called`() {
        // Act
        cartRepository.addProduct(product, 1)
        cartRepository.addProduct(product, 2)

        // Assert
        val cart = cartRepository.getCart()
        assertEquals(1, cart.items.size)
        assertEquals(3, cart.items[0].quantity)
    }

    @Test
    fun `should apply discount to product in cart when applyDiscount is called`() {
        // Arrange
        cartRepository.addProduct(product, 1)
        val discount = Discount(
            productId = product.id,
            categoryId = category.id,
            type = DiscountType.PERCENTAGE,
            value = 10.0,
            requiredQuantity = 0,
            requiredItems = listOf()
        )

        cartRepository.applyDiscount(discount)

        val cart = cartRepository.getCart()
        assertEquals(1, cart.appliedDiscounts.size)
        assertEquals(discount, cart.appliedDiscounts[0])
    }
}
