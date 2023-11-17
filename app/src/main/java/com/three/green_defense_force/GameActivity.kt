package com.three.green_defense_force

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.three.joystick.JoystickView

class GameActivity : AppCompatActivity() {
    // 이전 각도와 이미지를 저장할 변수
    private var previousAngle: Int = 0
    private var previousImageResource: Int = R.drawable.game_char_down

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // 상태바, 하단바 색상
        window.statusBarColor = getColor(R.color.green)
        window.navigationBarColor = getColor(R.color.green)

        val joystickView = findViewById<JoystickView>(R.id.joystick)
        val charImgView = findViewById<ImageView>(R.id.charImgView)

        // 뒤로 가기 버튼
        val backBtn = findViewById<Button>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }

        joystickView.setOnMoveListener { angle, strength ->

            // 조이스틱의 각도를 라디안으로 변환
            val radian = Math.toRadians(angle.toDouble())

            // 조이스틱의 각도에 따라 캐릭터 이미지 변경
            val imageResource = when {
                (angle >= 315 || angle < 45) -> if (strength == 0) R.drawable.game_char_right else getWalkingImage(R.drawable.game_char_right_walk1, R.drawable.game_char_right_walk2)
                (angle >= 45 && angle < 135) -> if (strength == 0) R.drawable.game_char_up else getWalkingImage(R.drawable.game_char_up_walk1, R.drawable.game_char_up_walk2)
                (angle >= 135 && angle < 225) -> if (strength == 0) R.drawable.game_char_left else getWalkingImage(R.drawable.game_char_left_walk1, R.drawable.game_char_left_walk2)
                (angle >= 225 && angle < 315) -> if (strength == 0) R.drawable.game_char_down else getWalkingImage(R.drawable.game_char_down_walk1, R.drawable.game_char_down_walk2)
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

            // 조이스틱의 각도에 맞게 x, y 방향을 계산
            val moveX = strength * 0.2f * Math.cos(radian).toFloat()
            val moveY = strength * 0.2f * -Math.sin(radian).toFloat()

            // 현재 캐릭터의 위치
            val currentX = charImgView.x
            val currentY = charImgView.y

            // 화면 경계 값
            val screenWidth = resources.displayMetrics.widthPixels
            val screenHeight = resources.displayMetrics.heightPixels

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
    }

    // 걷는 애니메이션 함수 : 이미지를 번갈아가면서 반환
    private fun getWalkingImage(image1: Int, image2: Int): Int {
        return if (System.currentTimeMillis() % 1000 < 500) image1 else image2
    }

    // 이전 상태 방향의 기본 이미지 반환 함수
    private fun getPreviousDirectionImage(angle: Int): Int {
        return when {
            (angle >= 315 || angle < 45) -> R.drawable.game_char_right
            (angle >= 45 && angle < 135) -> R.drawable.game_char_up
            (angle >= 135 && angle < 225) -> R.drawable.game_char_left
            (angle >= 225 && angle < 315) -> R.drawable.game_char_down
            else -> R.drawable.game_char_down
        }
    }
}