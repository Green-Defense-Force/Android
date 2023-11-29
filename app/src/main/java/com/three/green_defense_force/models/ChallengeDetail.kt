package com.three.green_defense_force.models

data class ChallengeDetail(
    val challengeId: String,
    val challengeTitle: String,
    val challengeContent: String,
    val challengeGoal: String,
    val challengeCorrectExample: String,
    val challengeWrongExample: String,
    val challengeChecklist: String,
    val rewardType: String,
    val rewardCount: Int,
)