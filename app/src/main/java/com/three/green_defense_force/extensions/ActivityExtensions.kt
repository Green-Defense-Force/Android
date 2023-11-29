package com.three.green_defense_force.extensions

import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

/** 상태바 및 하단바 색상 지정하는 함수 */
fun AppCompatActivity.setBarColor(topColor: Int, mainColor: Int) {
    window.statusBarColor = getColor(topColor)
    window.navigationBarColor = getColor(mainColor)
}

/** 뒤로가기 버튼 설정하는 함수 */
fun AppCompatActivity.setBackButton(buttonId: Int) {
    val backBtn: Button = findViewById(buttonId)
    backBtn.setOnClickListener {
        finish()
    }
}