package com.three.green_defense_force.models

data class Plogging(
    val userId: String,
    val ploggingTime: String,
    val ploggingDistance: Int,
    val trashCount: Int,
)