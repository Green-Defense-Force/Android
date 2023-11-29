package com.three.green_defense_force.activities

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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.three.green_defense_force.customviews.CustomButton
import com.three.green_defense_force.R
import com.three.green_defense_force.extensions.setBackButton
import com.three.green_defense_force.extensions.setBarColor
import com.three.green_defense_force.models.GameDetail
import com.three.green_defense_force.viewmodels.GameDetailViewModel

class GameDetailActivity : AppCompatActivity() {
    private lateinit var gameDetailViewModel: GameDetailViewModel
    private lateinit var gameDetailModel: GameDetail

    private lateinit var attackBtn: CustomButton
    private lateinit var attackText: TextView
    private lateinit var hpFrameLayout: FrameLayout

    private lateinit var monsterHp: ProgressBar
    private lateinit var battleFieldView: ImageView
    private lateinit var charAttackView: ImageView
    private lateinit var monsterView: ImageView
    private lateinit var attackEffect: ImageView

    private var alertDialog: AlertDialog? = null
    private var hp_progress_decrease: Int = 0
    private var before_text_margin: Int = 0

    // 상수
    private val COLOR_SKY = R.color.sky
    private val COLOR_GRASS = R.color.grass
    private val BEFORE_BTN_IMAGE = R.drawable.game_btn_before
    private val AFTER_BTN_IMAGE = R.drawable.game_btn_after
    private val AFTER_MARGIN_BOTTOM = 70

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_detail)

        setBarColor(COLOR_SKY, COLOR_GRASS)
        setBackButton(R.id.backBtn)

        val userId = intent.getStringExtra("USER_ID")
        val monsterId = intent.getStringExtra("MONSTER_ID")

        gameDetailViewModel = GameDetailViewModel()
        gameDetailModel =
            gameDetailViewModel.fetchGameDetailData(userId.toString(), monsterId.toString())

        hp_progress_decrease = gameDetailModel.monsterHp

        attackBtn = findViewById(R.id.attackBtn)
        attackText = findViewById(R.id.attackText)
        hpFrameLayout = findViewById(R.id.hpFrameLayout)
        monsterHp = findViewById(R.id.monsterHp)

        battleFieldView = findViewById(R.id.battleFieldView)
        charAttackView = findViewById(R.id.charAttackView)
        monsterView = findViewById(R.id.monsterView)
        attackEffect = findViewById(R.id.attackEffect)

        loadImageFromUrl(gameDetailModel.battleField, battleFieldView)
        loadImageFromUrl(gameDetailModel.attackImages[0], charAttackView)
        loadImageFromUrl(gameDetailModel.monsterImage, monsterView)
        loadImageFromUrl(gameDetailModel.attackEffect, attackEffect)

        // 시작 시, attackEffect 보이지 않도록 설정
        attackEffect.visibility = View.INVISIBLE

        val layoutParams = attackText.layoutParams
        if (layoutParams is ConstraintLayout.LayoutParams) {
            before_text_margin = layoutParams.bottomMargin
        }

        setAttackButton()
        setMonster(gameDetailModel)
    }

    /** URL → 이미지 변환하는 함수 */
    private fun loadImageFromUrl(url: String, imageView: ImageView) {
        Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)
    }

    /** 공격 버튼 터치 이벤트 설정 */
    private fun setAttackButton() {
        attackBtn.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // 버튼 이미지 변경
                        attackBtn.setBackgroundResource(AFTER_BTN_IMAGE)

                        // 텍스트 위치 변경
                        val textParams = attackText.layoutParams as ConstraintLayout.LayoutParams
                        textParams.setMargins(0, 0, 0, AFTER_MARGIN_BOTTOM)
                        attackText.layoutParams = textParams

                        // 캐릭터 이미지 변경
                        loadImageFromUrl(gameDetailModel.attackImages[1], charAttackView)

                        // 상태 바 (-10) 변경
                        val curProgress = monsterHp.progress
                        val newProgress = curProgress - hp_progress_decrease
                        monsterHp.progress = newProgress

                        // 공격 효과 보이기
                        attackEffect.visibility = View.VISIBLE

                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        // 기본 상태로 원상 복구
                        attackBtn.setBackgroundResource(BEFORE_BTN_IMAGE)

                        val textParams = attackText.layoutParams as ConstraintLayout.LayoutParams
                        textParams.setMargins(0, 0, 0, before_text_margin)
                        attackText.layoutParams = textParams

                        loadImageFromUrl(gameDetailModel.attackImages[0], charAttackView)

                        attackEffect.visibility = View.INVISIBLE

                        if (monsterHp.progress == 0) {
                            showGameOverPopup()
                        }
                        return false
                    }

                    else -> return false
                }
            }
        })
    }

    /** 팝업 띄우는 함수 */
    private fun showGameOverPopup() {
        // 일부 보이지 않도록 설정
        monsterHp.visibility = View.INVISIBLE
        attackBtn.visibility = View.INVISIBLE
        attackText.visibility = View.INVISIBLE
        hpFrameLayout.visibility = View.INVISIBLE
        monsterView.visibility = View.INVISIBLE

        // 팝업 띄우기
        val dialogView = layoutInflater.inflate(R.layout.dialog_game_over, null)
        val dialogBuilder = AlertDialog.Builder(this@GameDetailActivity)
            .setView(dialogView)
            .create()
        alertDialog = dialogBuilder
        alertDialog?.show()

        // 팝업 내 확인 버튼
        val okBtn = dialogView.findViewById<Button>(R.id.okBtn)
        okBtn.setOnClickListener {
            // 확인 버튼 클릭 시 팝업 닫기
            alertDialog?.dismiss()

            // 이전 화면으로 돌아가기
            finish()
        }
    }

    /** 화면에 몬스터 정보 표출하는 함수 */
    private fun setMonster(gameDetailModel: GameDetail) {
        val monsterTitleTextView = findViewById<TextView>(R.id.monsterTitle)
        val monsterNameTextView = findViewById<TextView>(R.id.monsterName)
        monsterTitleTextView.text = gameDetailModel.monsterTitle
        monsterNameTextView.text = gameDetailModel.monsterName
    }
}