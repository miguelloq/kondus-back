package com.example.modules.items.presenter.dto

import kotlinx.serialization.Serializable

@Serializable
data class ItemFinderDto(
    val search: String? = null,
    val categoriesIds: List<Int>, //Id
    val types: List<String>,
)
