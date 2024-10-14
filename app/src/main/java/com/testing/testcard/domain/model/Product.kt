package com.testing.testcard.domain.model

data class Product(
    val id: String,
    val name: String,
    var price: Double,
    val category: Category
)
