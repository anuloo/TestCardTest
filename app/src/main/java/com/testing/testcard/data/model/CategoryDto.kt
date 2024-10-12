package com.testing.testcard.data.model

import com.testing.testcard.domain.model.Category

data class CategoryDto(
    val id: String,
    val name: String
)

fun CategoryDto.mapToDomain(): Category {
    return Category(id, name)
}

