package com.three.green_defense_force.viewmodels

import androidx.lifecycle.ViewModel
import com.three.green_defense_force.models.Game
import com.three.green_defense_force.models.MonsterPreview

class GameViewModel : ViewModel() {
    /** 서버에서 유저 정보 받아오는 함수 */
    fun fetchGameData(userId: String): Game {
        val userLevel = 1
        val nickname = "three"
        val ticketAmount = 3
        val coinAmount = 100
        val bonusCoin = 3
        val monsterPreviews = listOf(
            MonsterPreview(
                "1",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_skull.png?raw=true"
            ),
            MonsterPreview(
                "2",
                "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_garbage.png?raw=true"
            ),
        )
        val characterImages = listOf(
            "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_char_up.png?raw=true",
            "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_char_up_walk1.png?raw=true",
            "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_char_up_walk2.png?raw=true",
            "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_char_down.png?raw=true",
            "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_char_down_walk1.png?raw=true",
            "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_char_down_walk2.png?raw=true",
            "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_char_left.png?raw=true",
            "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_char_left_walk1.png?raw=true",
            "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_char_left_walk2.png?raw=true",
            "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_char_right.png?raw=true",
            "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_char_right_walk1.png?raw=true",
            "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_char_right_walk2.png?raw=true",
        )
        val field =
            "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_bg_2.jpg?raw=true"

        return Game(
            userId,
            userLevel,
            nickname,
            ticketAmount,
            coinAmount,
            bonusCoin,
            monsterPreviews,
            characterImages,
            field
        )
    }

    /** 서버에 변경된 코인 정보를 전달하는 함수 */
    fun handleCoinIntersect(game: Game) {
        game.coinAmount += 1
    }

    /** 서버에 변경된 티켓 정보를 전달하는 함수 */
    fun handleTicketIntersect(game: Game) {
        game.ticketAmount -= 1
    }
}