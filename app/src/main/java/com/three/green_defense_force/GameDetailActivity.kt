package com.three.green_defense_force

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class GameDetailActivity : AppCompatActivity() {
    private var originBtnImg = R.drawable.game_btn_before
    private var originTextMargin = 0
    private var originCharImg = R.drawable.game_char_attack1

    private lateinit var attackBtn: CustomButton
    private lateinit var attackText: TextView
    private lateinit var charAttackView: ImageView
    private lateinit var monsterHp: ProgressBar
    private lateinit var attackEffect: ImageView
    private lateinit var backBtn: Button
    private lateinit var hpFrameLayout: FrameLayout
    private lateinit var monsterView: ImageView

    private var alertDialog: AlertDialog? = null

    // companion object : 상수, 정적 메서드
    companion object {
        private const val BUTTON_MARGIN_DOWN = 70
        private const val HP_PROGRESS_DECREASE = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_detail)

        // 상태바, 하단바 색상
        window.statusBarColor = getColor(R.color.sky)
        window.navigationBarColor = getColor(R.color.grass)

        attackBtn = findViewById(R.id.attackBtn)
        attackText = findViewById(R.id.attackText)
        charAttackView = findViewById(R.id.charAttackView)
        monsterHp = findViewById(R.id.monsterHp)
        attackEffect = findViewById(R.id.attackEffect)
        backBtn = findViewById(R.id.backBtn)
        hpFrameLayout = findViewById(R.id.hpFrameLayout)
        monsterView = findViewById(R.id.monsterView)

        // 시작 시, attackEffect 보이지 않도록 설정
        attackEffect.visibility = View.INVISIBLE

        // 뒤로 가기 버튼
        val backBtn: Button = findViewById(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }

        // 기존 상태 값 저장
        originTextMargin = (attackText.layoutParams as ConstraintLayout.LayoutParams).bottomMargin

        attackBtn.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // 버튼 이미지 변경
                        attackBtn.setBackgroundResource(R.drawable.game_btn_after)

                        // 텍스트 위치 변경
                        val textParams = attackText.layoutParams as ConstraintLayout.LayoutParams
                        textParams.setMargins(0, 0, 0, BUTTON_MARGIN_DOWN)
                        attackText.layoutParams = textParams

                        // 캐릭터 이미지 변경
                        charAttackView.setImageResource(R.drawable.game_char_attack2)

                        // 상태 바 (-10) 변경
                        val curProgress = monsterHp.progress
                        val newProgress = curProgress - HP_PROGRESS_DECREASE
                        monsterHp.progress = newProgress

                        // 공격 효과 보이기
                        attackEffect.visibility = View.VISIBLE

                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        // 기본 상태로 원상 복구
                        attackBtn.setBackgroundResource(originBtnImg)

                        val textParams = attackText.layoutParams as ConstraintLayout.LayoutParams
                        textParams.setMargins(0, 0, 0, originTextMargin)
                        attackText.layoutParams = textParams

                        charAttackView.setImageResource(originCharImg)

                        attackEffect.visibility = View.INVISIBLE

                        if (monsterHp.progress == 0) {
                            // 일부 보이지 않도록 설정
                            monsterHp.visibility = View.INVISIBLE
                            attackBtn.visibility = View.INVISIBLE
                            attackText.visibility = View.INVISIBLE
                            hpFrameLayout.visibility = View.INVISIBLE
                            monsterView.visibility = View.INVISIBLE

                            // 팝업 띄우기
                            val dialogView = layoutInflater.inflate(R.layout.dialog_game_over, null)
                            val dialogBuilder = AlertDialog.Builder(this@GameDetailActivity).setView(dialogView).create()
                            alertDialog = dialogBuilder
                            alertDialog?.show()

                            // 팝업 내 확인 버튼
                            val okBtn = dialogView.findViewById<Button>(R.id.okBtn)
                            okBtn.setOnClickListener {
                                // 확인 버튼 클릭 시 팝업 닫기
                                alertDialog?.dismiss()

                                val intent = Intent(this@GameDetailActivity, GameActivity::class.java)
                                startActivity(intent)
                            }
                        }

                        return false
                    }

                    else -> return false
                }
            }
        })
    }
}