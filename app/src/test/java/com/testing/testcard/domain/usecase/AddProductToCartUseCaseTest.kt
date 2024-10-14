package com.testing.testcard.domain.usecase

import com.testing.testcard.domain.model.CartItem
import com.testing.testcard.domain.model.Product
import com.testing.testcard.domain.model.ShoppingCart
import com.testing.testcard.domain.repository.CartRepository
import io.mockk.*
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertFailsWith

class AddProductToCartUseCaseTest {

    private lateinit var cartRepository: CartRepository
    private lateinit var addProductToCartUseCase: AddProductToCartUseCase
    private lateinit var product: Product
    private lateinit var shoppingCart: ShoppingCart

    @Before
    fun setup() {
        cartRepository = mockk(relaxed = true)
        addProductToCartUseCase = AddProductToCartUseCaseImpl(cartRepository)
        product = Product(id = "product1", name = "Orange", price = 10.0, category = mockk())
        shoppingCart = ShoppingCart()

        every { cartRepository.getCart() } returns shoppingCart
    }

    @Test
    fun `should add product when it does not exist in the cart`() {
        addProductToCartUseCase.execute(product, 1)

        verify { cartRepository.addProduct(product, 1) }
    }

    @Test
    fun `should update product quantity if product exists in cart`() {
        shoppingCart.items.add(CartItem(product, 1))

        addProductToCartUseCase.execute(product, 2)

        verify { cartRepository.addProduct(product, 2) }
    }

    @Test
    fun `should aggregate quantities when adding multiple times`() {
        shoppingCart.items.add(CartItem(product, 1))

        addProductToCartUseCase.execute(product, 3)

        verify { cartRepository.addProduct(product, 3) }
    }

    @Test
    fun `should throw error when adding product with invalid quantity`() {
        assertFailsWith<IllegalArgumentException> {
            addProductToCartUseCase.execute(product, 0)
        }

        assertFailsWith<IllegalArgumentException> {
            addProductToCartUseCase.execute(product, -1)
        }
    }

    @Test
    fun `should allow adding multiple different products`() {
        val product2 = Product(id = "product2", name = "Banana", price = 15.0, category = mockk())

        addProductToCartUseCase.execute(product, 1)
        addProductToCartUseCase.execute(product2, 2)

        verify { cartRepository.addProduct(product, 1) }
        verify { cartRepository.addProduct(product2, 2) }
    }
}
