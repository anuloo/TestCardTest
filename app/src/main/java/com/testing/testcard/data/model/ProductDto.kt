package com.testing.testcard.data.model

data class ProductDto(
    val id: String,
    val name: String,
    val price: Double,
    val category: CategoryDto
)
