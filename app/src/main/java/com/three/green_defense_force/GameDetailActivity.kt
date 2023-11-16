package com.three.green_defense_force

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class GameDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_detail)

        // 상태바, 하단바 색상
        window.statusBarColor = getColor(R.color.sky)
        window.navigationBarColor = getColor(R.color.grass)
    }
}