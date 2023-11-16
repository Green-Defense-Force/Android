package com.three.green_defense_force

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.three.joystick.JoystickView

class GameActivity : AppCompatActivity() {
    // 이전 각도와 이미지를 저장할 변수
    private var previousAngle: Int = 0
    private var previousImageResource: Int = R.drawable.game_char_right

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val joystickView = findViewById<JoystickView>(R.id.joystick)
        val angleValueView = findViewById<TextView>(R.id.value_angle)
        val strengthValueView = findViewById<TextView>(R.id.value_strength)

        val charUpImgView = findViewById<ImageView>(R.id.charUp)

        joystickView.setOnMoveListener { angle, strength ->
            angleValueView.text = "angle : $angle"
            strengthValueView.text = "strength : $strength"

            // 조이스틱의 각도를 라디안으로 변환
            val radian = Math.toRadians(angle.toDouble())

            // 조이스틱의 각도에 따라 캐릭터 이미지 변경
            val imageResource = when {
                (angle >= 315 || angle < 45) -> R.drawable.game_char_right
                (angle >= 45 && angle < 135) -> R.drawable.game_char_up
                (angle >= 135 && angle < 225) -> R.drawable.game_char_left
                (angle >= 225 && angle < 315) -> R.drawable.game_char_down
                else -> previousImageResource
            }

            // 조이스틱이 멈췄을 때 이전 이미지 유지
            if (strength == 0) {
                charUpImgView.setImageResource(previousImageResource)
            } else {
                charUpImgView.setImageResource(imageResource)
                previousAngle = angle
                previousImageResource = imageResource
            }

            // 조이스틱의 각도에 맞게 x, y 방향을 계산
            val moveX = strength * 0.2f * Math.cos(radian).toFloat()
            val moveY = strength * 0.2f * -Math.sin(radian).toFloat()

            // 현재 캐릭터의 위치
            val currentX = charUpImgView.x
            val currentY = charUpImgView.y

            // 화면 경계 값
            val screenWidth = resources.displayMetrics.widthPixels
            val screenHeight = resources.displayMetrics.heightPixels

            // 이동 후 위치 계산
            val newX = currentX + moveX
            val newY = currentY + moveY

            // 화면 경계 내에 위치하도록 제한
            val clampedX = newX.coerceIn(0f, (screenWidth - charUpImgView.width).toFloat())
            val clampedY = newY.coerceIn(0f, (screenHeight - charUpImgView.height).toFloat())

            // "charUp" 이미지 뷰의 위치 변경
            charUpImgView.x = clampedX
            charUpImgView.y = clampedY
        }
    }
}