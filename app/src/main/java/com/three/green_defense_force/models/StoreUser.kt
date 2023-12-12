package com.three.green_defense_force.models

data class StoreUser(
    val userId: String,
    var coinAmount: Int,
    var equippedItems: Map<String, String?> // "hair", "top", "pants", "weapon", "acc" 등의 카테고리에 해당하는 아이템의 Image URL
)