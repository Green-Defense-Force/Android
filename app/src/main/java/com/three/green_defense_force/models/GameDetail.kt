package com.three.green_defense_force.models

data class GameDetail(
    val userId: String,
    val attackImages: List<String>,
    val attackEffect: String,
    val monsterId: String,
    val monsterTitle: String,
    val monsterName: String,
    val monsterImage: String,
    val monsterHp: Int,
    val battleField: String,
)