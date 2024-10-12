package com.testing.testcard.data.mapper

import com.testing.testcard.common.DiscountType
import com.testing.testcard.data.model.CartItemDto
import com.testing.testcard.data.model.CategoryDto
import com.testing.testcard.data.model.DiscountDto
import com.testing.testcard.data.model.ProductDto
import com.testing.testcard.data.model.ShoppingCartDto
import com.testing.testcard.domain.model.CartItem
import com.testing.testcard.domain.model.Category
import com.testing.testcard.domain.model.Discount
import com.testing.testcard.domain.model.Product
import com.testing.testcard.domain.model.ShoppingCart

// Mappers to convert between DTOs and domain objects

fun ProductDto.toDomain(): Product {
    return Product(id, name, price, category.toDomain())
}

fun CategoryDto.toDomain(): Category {
    return Category(id, name)
}

fun CartItemDto.toDomain(): CartItem {
    return CartItem(product.toDomain(), quantity)
}

fun ShoppingCartDto.toDomain(): ShoppingCart {
    val items = items.map { it.toDomain() }.toMutableList()
    return ShoppingCart(items, appliedDiscounts.map { it.toDomain() }.toMutableList())
}

fun DiscountDto.toDomain(): Discount {
    return Discount(
        productId = productId,
        categoryId = categoryId,
        type = type,
        value = value
    )
}