package com.three.green_defense_force.viewmodels

import androidx.lifecycle.ViewModel
import com.three.green_defense_force.models.Game
import com.three.green_defense_force.models.MonsterPreview

class GameViewModel : ViewModel() {

    /** 서버에서 유저 정보 받아오는 함수 */
    fun fetchUserData(userId: String): Game {
        val userLevel = 1
        val nickname = "three"
        val ticketAmount = 3
        val coinAmount = 100
        val bonusCoin = 3
        val monsterPreviews = listOf(
            MonsterPreview("1", createImageUrl("game_skull.png")),
            MonsterPreview("2", createImageUrl("game_garbage.png")),
        )
        val characterImages = listOf(
            createImageUrl("game_char_up.png"),
            createImageUrl("game_char_up_walk1.png"),
            createImageUrl("game_char_up_walk2.png"),
            createImageUrl("game_char_down.png"),
            createImageUrl("game_char_down_walk1.png"),
            createImageUrl("game_char_down_walk2.png"),
            createImageUrl("game_char_left.png"),
            createImageUrl("game_char_left_walk1.png"),
            createImageUrl("game_char_left_walk2.png"),
            createImageUrl("game_char_right.png"),
            createImageUrl("game_char_right_walk1.png"),
            createImageUrl("game_char_right_walk2.png"),
        )

        return Game(
            userId,
            userLevel,
            nickname,
            ticketAmount,
            coinAmount,
            bonusCoin,
            monsterPreviews,
            characterImages
        )
    }

    private fun createImageUrl(imageName: String): String {
        return "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/$imageName?raw=true"
    }
}