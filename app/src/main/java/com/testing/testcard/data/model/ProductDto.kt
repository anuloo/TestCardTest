package com.testing.testcard.data.model

import com.testing.testcard.domain.model.Product

data class ProductDto(
    val id: String,
    val name: String,
    val price: Double,
    val category: CategoryDto
)

fun ProductDto.mapToDomain(): Product {
    return Product(
        id = id,
        name = name,
        price = price,
        category = category.mapToDomain()
    )
}

