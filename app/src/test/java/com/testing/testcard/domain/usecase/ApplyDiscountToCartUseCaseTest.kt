package com.testing.testcard.domain.usecase

import com.testing.testcard.common.DiscountType
import com.testing.testcard.domain.model.CartItem
import com.testing.testcard.domain.model.Category
import com.testing.testcard.domain.model.Discount
import com.testing.testcard.domain.model.Product
import com.testing.testcard.domain.model.ShoppingCart
import com.testing.testcard.domain.repository.CartRepository
import com.testing.testcard.domain.service.BuyXGetYFreeDiscountStrategy
import com.testing.testcard.domain.service.CartItemFinder
import com.testing.testcard.domain.service.CombinationDiscountStrategy
import com.testing.testcard.domain.service.DiscountStrategy
import com.testing.testcard.domain.service.DiscountValidator
import com.testing.testcard.domain.service.PercentageDiscountStrategy
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ApplyDiscountToCartUseCaseTest {

    private lateinit var cartRepository: CartRepository
    private lateinit var applyDiscountToCartUseCase: ApplyDiscountUseCase
    private lateinit var applyDiscountValidator: DiscountValidator
    private lateinit var cartItemFinder: CartItemFinder
    private lateinit var discountStrategies: Set<DiscountStrategy>
    private lateinit var discountValidator: DiscountValidator
    private lateinit var product: Product
    private lateinit var product3: Product
    private lateinit var category: Category

    @Before
    fun setup() {
        cartRepository = mockk(relaxed = true)
        applyDiscountValidator = mockk(relaxed = true)
        cartItemFinder = mockk(relaxed = true)
        discountValidator = mockk(relaxed = true)

        // Initialize both strategies in the set
        val percentageDiscountStrategy = PercentageDiscountStrategy(cartItemFinder)
        val buyXGetYFreeDiscountStrategy = BuyXGetYFreeDiscountStrategy(cartItemFinder)
        val combinationDiscountStrategy = CombinationDiscountStrategy()

        // Add all strategies to the set
        discountStrategies = setOf(
            percentageDiscountStrategy,
            buyXGetYFreeDiscountStrategy,
            combinationDiscountStrategy
        )

        applyDiscountToCartUseCase =
            ApplyDiscountUseCaseImpl(cartRepository, applyDiscountValidator, discountStrategies)
        category = Category("category1", "Fruit")
        product = Product(id = "product1", name = "Apple", price = 100.0, category = category)
        product3 = Product(id = "product3", name = "Grape", price = 80.0, category = category)
    }

    @Test
    fun `should be discounted by 10%`() {
        val cartItem = CartItem(product = product, quantity = 3)
        val shoppingCart = ShoppingCart(items = mutableListOf(cartItem))
        every { cartRepository.getCart() } returns shoppingCart

        val discount = Discount(
            productId = product.id,
            categoryId = category.id,
            type = DiscountType.PERCENTAGE,
            value = 10.0,
            requiredQuantity = 0,
            requiredItems = listOf()
        )

        every { discountValidator.validate(discount) } just Runs
        every {
            cartItemFinder.filterItemByCategory(
                shoppingCart,
                discount
            )
        } returns shoppingCart.items
        every { cartItemFinder.isDiscountAlreadyApplied(shoppingCart, discount) } returns false

        applyDiscountToCartUseCase.execute(discount)

        assertTrue(cartItem.appliedDiscount != null, "Discount should be applied to the cart item")
        assertEquals(90.0, cartItem.product.price, "Price should be discounted by 10%")
    }

    @Test
    fun `should not apply percentage discount if product does not exist in cart`() {
        val cart = ShoppingCart(
            items = mutableListOf(
                CartItem(product = product, quantity = 1)
            ),
            appliedDiscounts = mutableListOf()
        )

        val discount = Discount(
            productId = "product2",  // Non-existing product
            categoryId = "category2",
            type = DiscountType.PERCENTAGE,
            value = 10.0
        )

        applyDiscountToCartUseCase.execute(discount)

        assertEquals(100.0, cart.items[0].product.price, "Expected the price to remain unchanged")
    }

    @Test
    fun `should apply Buy X Get Y Free discount when there are enough items in the cart`() {
        val cart = ShoppingCart(
            items = mutableListOf(
                CartItem(product = product, quantity = 3)
            ),
            appliedDiscounts = mutableListOf()
        )

        val discount = Discount(
            productId = "1",
            categoryId = "category1",
            type = DiscountType.BUY_X_GET_Y_FREE,
            value = 0.0,
            requiredQuantity = 2
        )

        every { cartRepository.getCart() } returns cart
        every { discountValidator.validate(discount) } returns Unit
        every { cartItemFinder.filterItemByCategory(cart, discount) } returns cart.items

        applyDiscountToCartUseCase.execute(discount)

        assertEquals(2, cart.items[0].quantity, "Expected 2 items to be paid for, 1 free.")
        assertEquals(1, cart.appliedDiscounts.size, "Expected the discount to be applied once.")
    }

    @Test
    fun `should not apply Buy X Get Y Free discount when there are not enough items in the cart`() {
        val cartItem = CartItem(product = product, quantity = 1)
        val cart = ShoppingCart(
            items = mutableListOf(cartItem),
            appliedDiscounts = mutableListOf()
        )

        val discount = Discount(
            productId = "1",
            categoryId = "category1",
            type = DiscountType.BUY_X_GET_Y_FREE,
            value = 0.0,
            requiredQuantity = 2
        )

        every { cartRepository.getCart() } returns cart
        every { discountValidator.validate(discount) } returns Unit
        every { cartItemFinder.findItem(cart, discount) } returns cart.items[0]

        applyDiscountToCartUseCase.execute(discount)

        assertEquals(1, cart.items[0].quantity, "Expected 1 item to remain in the cart.")
    }

    @Test
    fun `should apply combination discount when all required items are in the cart`() {
        val cart = ShoppingCart(
            items = mutableListOf(
                CartItem(
                    product = Product(
                        id = "product1",
                        name = "sandwich",
                        price = 5.0,
                        category = Category(id = "category1", name = "Food")
                    ), quantity = 1
                ),
                CartItem(
                    product = Product(
                        id = "product2",
                        name = "apple",
                        price = 2.0,
                        category = Category(id = "category2", name = "Fruits")
                    ), quantity = 1
                ),
                CartItem(
                    product = Product(
                        id = "product3",
                        name = "drink",
                        price = 3.0,
                        category = Category(id = "category3", name = "Beverages")
                    ), quantity = 1
                )
            ),
            appliedDiscounts = mutableListOf()
        )

        println("Cart items after creation: ${cart.items.map { it.product.id }}")  // Check items here
        println("Cart hashCode: ${cart.hashCode()}")


        val discount = Discount(
            productId = "comboDiscount",
            categoryId = "comboCategory",
            type = DiscountType.COMBINATION,
            value = 5.0,
            requiredItems = listOf(
                CartItem(
                    Product(
                        id = "product1",
                        name = "sandwich",
                        price = 5.0,
                        category = Category(id = "category1", name = "Food")
                    ), quantity = 1
                ),
                CartItem(
                    Product(
                        id = "product2",
                        name = "apple",
                        price = 2.0,
                        category = Category(id = "category2", name = "Fruits")
                    ), quantity = 1
                ),
                CartItem(
                    Product(
                        id = "product3",
                        name = "drink",
                        price = 3.0,
                        category = Category(id = "category3", name = "Beverages")
                    ), quantity = 1
                )
            )
        )

        every { cartRepository.getCart() } returns cart
        applyDiscountToCartUseCase.execute(discount)

        assertEquals(4.75, cart.items[0].product.price, "Expected the combo discount to apply")
        assertEquals(1, cart.appliedDiscounts.size, "Expected no discounts to be applied.")
    }


    @Test
    fun `should throw error for invalid discount value`() {
        val discountValidator = DiscountValidator()

        val invalidDiscount = Discount(
            productId = "1",
            categoryId = "1",
            type = DiscountType.PERCENTAGE,
            value = -10.0,
            requiredQuantity = 0,
            requiredItems = listOf()
        )

        assertFailsWith<IllegalArgumentException> {
            discountValidator.validate(invalidDiscount)
        }
    }

    @Test
    fun `should find item in cart when product exists`() {
        val cart = ShoppingCart(items = mutableListOf(CartItem(product, 1)))

        val item = cartItemFinder.findItem(
            cart,
            Discount(
                productId = product.id,
                categoryId = category.id,
                type = DiscountType.PERCENTAGE,
                value = 10.0,
                requiredQuantity = 0,
                requiredItems = listOf()
            )
        )

        assertNotNull(item)
    }

    @Test
    fun `should throw exception when discount is already applied to the cart item`() {
        val cartItem = CartItem(product = product, quantity = 3)
        val shoppingCart = ShoppingCart(items = mutableListOf(cartItem))

        every { cartRepository.getCart() } returns shoppingCart

        val discount = Discount(
            productId = product.id,
            categoryId = category.id,
            type = DiscountType.PERCENTAGE,
            value = 10.0,
            requiredQuantity = 0,
            requiredItems = listOf()
        )

        every { discountValidator.validate(discount) } just Runs

        cartItem.applyDiscount(discount)

        assertFailsWith<IllegalStateException> {
            cartItem.applyDiscount(discount)
        }
    }

}
