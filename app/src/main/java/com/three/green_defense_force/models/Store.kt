package com.three.green_defense_force.models

data class Store(
    val userId: String,
    val categoryId: String,
    var storeItems: List<StoreItem> = listOf(),
)