package com.three.green_defense_force.models

data class Game(
    val userId: String,
    val userLevel: Int,
    val nickname: String,
    var ticketAmount: Int,
    var coinAmount: Int,
    val bonusCoin: Int,
    val monsterPreviews: List<MonsterPreview>,
    val characterImages: List<String>,
    val field: String,
)