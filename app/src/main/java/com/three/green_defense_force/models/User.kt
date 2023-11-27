package com.three.green_defense_force.models

data class User(
    val userId: String,
    val userLevel: Int,
    val nickname: String,
    val ticketAmount: Int,
    var coinAmount: Int,
    val characterImages: List<String>,
    val attackImages: List<String>,
    val attackEffect: String,
)