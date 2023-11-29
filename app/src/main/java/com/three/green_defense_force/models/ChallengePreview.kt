package com.three.green_defense_force.models

data class ChallengePreview(
    val challengeId: String,
    val challengeTitle: String,
    val rewardType: String,
    val rewardCount: Int,
    var isDone: Boolean,
)