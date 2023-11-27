package com.three.green_defense_force.models

data class ChallengePreview(
    val challengeId: Int,
    val challengeTitle: String,
    val rewardImage: String,
    val rewardCount: Int,
    var isDone: Boolean,
)