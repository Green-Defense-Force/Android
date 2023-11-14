package com.three.green_defense_force

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView

class GameActivity : AppCompatActivity() {
    private lateinit var stopCharacter: ImageView
    private lateinit var moveCharacter: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        stopCharacter = findViewById(R.id.stopCharacter)
        moveCharacter = findViewById(R.id.moveCharacter)

        // 앱 시작 시, moveCharacter는 안 보이게 설정
        moveCharacter.visibility = View.INVISIBLE
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            // 터치 시작 시, stopCharacter는 안 보이게 moveCharacter는 보이게 설정
            MotionEvent.ACTION_DOWN -> {
                stopCharacter.visibility = View.INVISIBLE
                moveCharacter.visibility = View.VISIBLE
            }
            // 터치 종료 시, stopCharacter는 보이게 moveCharacter는 안 보이게 설정
            MotionEvent.ACTION_UP -> {
                stopCharacter.visibility = View.VISIBLE
                moveCharacter.visibility = View.INVISIBLE
            }
        }
        return super.onTouchEvent(event)
    }
}