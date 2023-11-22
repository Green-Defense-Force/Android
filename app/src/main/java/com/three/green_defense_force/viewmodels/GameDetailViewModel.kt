package com.three.green_defense_force.viewmodels

import androidx.lifecycle.ViewModel
import com.three.green_defense_force.models.Game
import com.three.green_defense_force.models.GameDetail
import com.three.green_defense_force.models.MonsterPreview

class GameDetailViewModel : ViewModel() {
    /** 서버에서 유저, 몬스터 정보 받아오는 함수 */
    fun fetchGameDetailData(userId: String, monsterId: String): GameDetail {
        val attackImages: List<String>
        val attackEffect: String
        val monsterTitle: String
        val monsterName: String
        val monsterImage: String
        val monsterHp: Int
        val battleFieldImage: String

        when (monsterId) {
            "1" -> {
                attackImages = listOf(
                    "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_char_attack1.png?raw=true",
                    "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_char_attack2.png?raw=true"
                )
                attackEffect =
                    "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_attack_effect1.png?raw=true"
                monsterTitle = "한강의"
                monsterName = "치킨 뼈"
                monsterImage =
                    "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_skull.png?raw=true"
                monsterHp = 20
                battleFieldImage =
                    "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_bg_3.jpg?raw=true"
            }

            "2" -> {
                attackImages = listOf(
                    "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_char_attack1.png?raw=true",
                    "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_char_attack2.png?raw=true"
                )
                attackEffect =
                    "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_attack_effect1.png?raw=true"
                monsterTitle = "한강의"
                monsterName = "쓰레기 봉지"
                monsterImage =
                    "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_garbage.png?raw=true"
                monsterHp = 10
                battleFieldImage =
                    "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_bg_3.jpg?raw=true"
            }

            else -> {
                attackImages = listOf(
                    "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_char_attack_default1.png?raw=true",
                    "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_char_attack_default2.png?raw=true"
                )
                attackEffect = ""
                monsterTitle = ""
                monsterName = ""
                monsterImage = ""
                monsterHp = 0
                battleFieldImage = "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_bg_3.jpg?raw=true"
            }
        }

        return GameDetail(
            userId,
            attackImages,
            attackEffect,
            monsterId,
            monsterTitle,
            monsterName,
            monsterImage,
            monsterHp,
            battleFieldImage
        )
    }
}