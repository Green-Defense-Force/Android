package com.three.green_defense_force.viewmodels

import com.three.green_defense_force.models.Challenge
import com.three.green_defense_force.models.ChallengePreview

class ChallengeViewModel {
    /** 서버에서 챌린지 정보 받아오는 함수 */
    fun fetchChallengeData(userId: String): Challenge {
        val challengePreviews = listOf(
            ChallengePreview(
                "1",
                "페트병 분리 배출하기",
                "ticket",
                1,
                false
            ),
            ChallengePreview(
                "2",
                "카페에서 텀블러 사용하기",
                "ticket",
                1,
                false
            ),
            ChallengePreview(
                "3",
                "카페 빨대 사용하지 않기",
                "ticket",
                1,
                false
            ),
            ChallengePreview(
                "4",
                "(플로깅) 쓰레기 5개 줍기",
                "coin",
                10,
                false
            ),
            ChallengePreview(
                "5",
                "대중교통(버스) 이용하기",
                "ticket",
                1,
                false
            ),
            ChallengePreview(
                "6",
                "카카오톡 친구 초대하기",
                "coin",
                20,
                false
            ),
        )
        return Challenge(
            userId,
            challengePreviews
        )
    }
}