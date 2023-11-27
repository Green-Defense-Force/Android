package com.three.green_defense_force.models

data class Challenge(
    val userId: String,
    var challengePreviews: List<ChallengePreview>,
)