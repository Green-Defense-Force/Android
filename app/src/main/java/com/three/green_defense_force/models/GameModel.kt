package com.three.green_defense_force.models

data class GameModel(
    val userId: String,
    val nickname: String,
    val ticketAmount: Int,
    val coinAmount: Int,
    val userLevel: Int,
    val bonusCoin: Int,
    val monsterImages: List<String>,
)