package com.three.green_defense_force.models

data class StoreItem(
    val itemId: String,
    val itemTitle: String,
    val itemImage: String,
    val itemPrice: Int,
    var isBuy: Boolean,
)
