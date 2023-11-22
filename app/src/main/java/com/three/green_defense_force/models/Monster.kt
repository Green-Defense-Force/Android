package com.three.green_defense_force.models

data class Monster(
    val monsterId: String,
    val monsterLevel: Int,
    val monsterLocation: String,
    val monsterName: String,
    val monsterImage: String,
    val monsterHp: Int,
)