package com.three.green_defense_force.viewmodels

import androidx.lifecycle.ViewModel
import android.widget.ImageView
import com.three.green_defense_force.R
import com.three.green_defense_force.models.GameModel

class GameViewModel : ViewModel() {
    // 이전 각도, 이미지 저장하는 변수
    private var previousAngle: Int = 0
    private var previousImageResource: Int = R.drawable.game_char_down

    /** 조이스틱의 각도에 따라 캐릭터 이미지 변경하는 함수 */
    fun updateCharacterImage(angle: Int, strength: Int, charImgView: ImageView) {
        // 조이스틱의 각도에 따라 캐릭터 이미지 변경
        val imageResource = when {
            (angle >= 315 || angle < 45) -> if (strength == 0) R.drawable.game_char_right else getWalkingImage(
                R.drawable.game_char_right_walk1,
                R.drawable.game_char_right_walk2
            )

            (angle >= 45 && angle < 135) -> if (strength == 0) R.drawable.game_char_up else getWalkingImage(
                R.drawable.game_char_up_walk1,
                R.drawable.game_char_up_walk2
            )

            (angle >= 135 && angle < 225) -> if (strength == 0) R.drawable.game_char_left else getWalkingImage(
                R.drawable.game_char_left_walk1,
                R.drawable.game_char_left_walk2
            )

            (angle >= 225 && angle < 315) -> if (strength == 0) R.drawable.game_char_down else getWalkingImage(
                R.drawable.game_char_down_walk1,
                R.drawable.game_char_down_walk2
            )

            else -> previousImageResource
        }

        // 조이스틱이 멈췄을 때 이전 상태의 기본 이미지 유지
        if (strength == 0) {
            val previousDirectionImage = getPreviousDirectionImage(previousAngle)
            charImgView.setImageResource(previousDirectionImage)
        } else {
            charImgView.setImageResource(imageResource)
            previousAngle = angle
            previousImageResource = imageResource
        }
    }

    /** 캐릭터 걷는 동작 설정하는 함수 */
    private fun getWalkingImage(image1: Int, image2: Int): Int {
        // 번갈아 가며 이미지 반환
        return if (System.currentTimeMillis() % 1000 < 500) image1 else image2
    }

    /** 이전 상태 방향의 기본 이미지 반환하는 함수 */
    private fun getPreviousDirectionImage(angle: Int): Int {
        return when {
            (angle >= 315 || angle < 45) -> R.drawable.game_char_right
            (angle >= 45 && angle < 135) -> R.drawable.game_char_up
            (angle >= 135 && angle < 225) -> R.drawable.game_char_left
            (angle >= 225 && angle < 315) -> R.drawable.game_char_down
            else -> R.drawable.game_char_down
        }
    }

    /** 캐릭터 움직이는 함수 */
    fun moveCharacter(
        angle: Int,
        strength: Int,
        screenWidth: Int,
        screenHeight: Int,
        charImgView: ImageView
    ) {
        // 조이스틱의 각도를 라디안으로 변환
        val radian = Math.toRadians(angle.toDouble())

        // 조이스틱의 각도에 맞게 x, y 방향을 계산
        val moveX = strength * 0.2f * Math.cos(radian).toFloat()
        val moveY = strength * 0.2f * -Math.sin(radian).toFloat()

        // 현재 캐릭터의 위치
        val currentX = charImgView.x
        val currentY = charImgView.y

        // 이동 후 위치 계산
        val newX = currentX + moveX
        val newY = currentY + moveY

        // 화면 경계 내에 위치하도록 제한
        val clampedX = newX.coerceIn(0f, (screenWidth - charImgView.width).toFloat())
        val clampedY = newY.coerceIn(0f, (screenHeight - charImgView.height).toFloat())

        // "charImgView" 이미지 뷰의 위치 변경
        charImgView.x = clampedX
        charImgView.y = clampedY
    }

    /** 서버에서 몬스터 이미지 받아오는 함수 */
    fun getMonsters(): List<Int> {
        val monsterList = mutableListOf(
            R.drawable.game_skull,
            R.drawable.game_garbage,
        )
        return monsterList.shuffled()
    }

    /** 서버에서 코인 개수 받아오는 함수 */
    fun getBonusCoin(): Int {
        return 2
    }

    /** 서버에서 유저 정보 받아오는 함수 */
    fun fetchUserData(userId: String): GameModel {
        val nickname = "three"
        val ticketAmount = 3
        val coinAmount = 100
        val userLevel = 1
        val bonusCoin = 2
        val monsterImages = listOf(
            "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_skull.png?raw=true",
            "https://github.com/1three/1three-green-defense-force-image-api/blob/main/images/game_garbage.png?raw=true"
        )

        return GameModel(
            userId,
            nickname,
            ticketAmount,
            coinAmount,
            userLevel,
            bonusCoin,
            monsterImages
        )
    }
}