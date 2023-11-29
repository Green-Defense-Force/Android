package com.three.green_defense_force.viewmodels

import com.three.green_defense_force.models.ChallengeDetail

class ChallengeDetailViewModel {
    /** 서버에서 챌린지 상세 정보 받아오는 함수 */
    fun fetchChallengeDetailData(challengeId: String): ChallengeDetail {
        val challengeTitle: String
        val challengeContent: String
        val challengeGoal: String
        val challengeCorrectExample: String
        val challengeWrongExample: String
        val challengeChecklist: String
        val rewardType: String
        val rewardCount: Int

        when (challengeId) {
            "1" -> {
                challengeTitle = "페트병 분리 배출하기"
                challengeContent = "[하루 1회]\n한 개의 페트병을 재활용하면\n약 142g의 탄소배출을 줄일 수 있어요."
                challengeGoal = "올바른 분리배출로 우리의 환경을 지켜보아요!"
                challengeCorrectExample = "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/challenge_pet_correct.jpg?raw=true"
                challengeWrongExample = "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/challenge_pet_wrong.jpg?raw=true"
                challengeChecklist = "내용물이 비었나요?\n페트병 안을 잘 헹궜나요?\n분리 배출한 것이 잘 보이나요?"
                rewardType = "ticket"
                rewardCount = 1
            }

            "2" -> {
                challengeTitle = "카페에서 텀블러 사용하기"
                challengeContent = "[하루 1회]\n텀블러를 사용하면 일회용 컵 대비\n약 11g의 탄소배출을 줄일 수 있어요."
                challengeGoal = "카페에서 텀블러를 사용하고, 지구를 사랑해 보세요!"
                challengeCorrectExample = "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/challenge_tumbler_correct.jpeg?raw=true"
                challengeWrongExample = "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/challenge_tumbler_wrong.jpg?raw=true"
                challengeChecklist = "개인용 텀블러인가요?\n외부에서 사용하는 사진인가요?"
                rewardType = "ticket"
                rewardCount = 1
            }

            "3" -> {
                challengeTitle = "카페 빨대 사용하지 않기"
                challengeContent = "[하루 1회]\n일회용 빨대 대신, 빨대 없이 음료를 즐기면\n약 0.42g의 탄소배출을 줄일 수 있어요."
                challengeGoal = "작은 행동 하나가\n환경을 위한 큰 변화를 만들 수 있어요!"
                challengeCorrectExample = "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/challenge_straw_correct.jpg?raw=true"
                challengeWrongExample = "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/challenge_straw_wrong.jpg?raw=true"
                challengeChecklist = "빨대를 사용하지 않았나요?\n재사용 가능한 빨대를 사용했나요?"
                rewardType = "ticket"
                rewardCount = 1
            }

            "4" -> {
                challengeTitle = "(플로깅) 쓰레기 5개 줍기"
                challengeContent = "[하루 1회]\n쓰레기 5개를 주워서 재촬영하면\n약 80g의 탄소배출을 줄일 수 있어요."
                challengeGoal = "산책하며 플로깅을 해보세요!"
                challengeCorrectExample = "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/challenge_plogging_correct.jpg?raw=true"
                challengeWrongExample = "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/challenge_plogging_wrong.jpg?raw=true"
                challengeChecklist = "외부에서 찍은 사진인가요?\n줍깅을 통해 주운 쓰레기가 잘 보이나요?"
                rewardType = "coin"
                rewardCount = 10
            }

            "5" -> {
                challengeTitle = "대중교통(버스) 이용하기"
                challengeContent = "[하루 1회]\n버스를 이용하면 차량 대비\n약 1kg의 탄소배출을 줄일 수 있어요."
                challengeGoal = "대중교통을 이용하고,\n지구를 위한 행동을 실천해 보세요!"
                challengeCorrectExample = "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/challenge_bus_correct.jpg?raw=true"
                challengeWrongExample = "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/challenge_bus_wrong.jpg?raw=true"
                challengeChecklist = "버스 내부에서 찍은 사진인가요?"
                rewardType = "ticket"
                rewardCount = 1
            }

            else -> {
                challengeTitle = ""
                challengeContent = ""
                challengeGoal = ""
                challengeCorrectExample = ""
                challengeWrongExample = ""
                challengeChecklist = ""
                rewardType = ""
                rewardCount = 0
            }
        }

        return ChallengeDetail(
            challengeId,
            challengeTitle,
            challengeContent,
            challengeGoal,
            challengeCorrectExample,
            challengeWrongExample,
            challengeChecklist,
            rewardType,
            rewardCount,
        )
    }
}