package com.testing.testcard.data.repository


import com.testing.testcard.domain.model.CartItem
import com.testing.testcard.domain.model.Discount
import com.testing.testcard.domain.model.Product
import com.testing.testcard.domain.model.ShoppingCart
import com.testing.testcard.domain.repository.CartRepository

class CartRepositoryImpl(
) : CartRepository {

    private val shoppingCart = ShoppingCart()


    override fun addProduct(product: Product, quantity: Int) {
        val item = shoppingCart.items.find { it.product == product }
        if (item != null) {
            item.quantity += quantity
        } else {
            shoppingCart.items.add(CartItem(product, quantity))
        }
    }

    override fun applyDiscount(cart: ShoppingCart, discount: Discount) {
        val productInCart = cart.items.find { it.product.id == discount.productId }
        if (productInCart != null) {
            cart.appliedDiscounts.add(discount)
        }
    }

    override fun getCart(): ShoppingCart {
        return shoppingCart
    }
}
