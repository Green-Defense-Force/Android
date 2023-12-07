package com.three.green_defense_force.models

import android.net.Uri

data class ChallengeCamera(
    var userId: String,
    var challengeId: String,
    var challengeImage: Uri,
)