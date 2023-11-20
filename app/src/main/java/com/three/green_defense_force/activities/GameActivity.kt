package com.three.green_defense_force.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.three.green_defense_force.R
import com.three.green_defense_force.models.GameModel
import com.three.green_defense_force.viewmodels.GameViewModel
import com.three.joystick.JoystickView
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    private lateinit var viewModel: GameViewModel
    private lateinit var charImgView: ImageView
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        setBarColor()

        viewModel = GameViewModel()
        charImgView = findViewById(R.id.charImgView)

        val joystickView = findViewById<JoystickView>(R.id.joystick)
        val constraintLayout = findViewById<ViewGroup>(R.id.constraintLayout)
        val backBtn = findViewById<Button>(R.id.backBtn)

        backBtn.setOnClickListener {
            finish()
        }

        // displayMetrics : ViewModel 접근 불가, GameActivity → 전달 → GameViewModel
        screenWidth = resources.displayMetrics.widthPixels
        screenHeight = resources.displayMetrics.heightPixels

        joystickView.setOnMoveListener { angle, strength ->
            viewModel.updateCharacterImage(angle, strength, charImgView)
            viewModel.moveCharacter(angle, strength, screenWidth, screenHeight, charImgView)
        }

        // 가상 User 객체 생성
        val gameModel = viewModel.fetchUserData("6uiaYtLh")
        updateUserInfo(gameModel)

        addRandomBonusCoin(gameModel.bonusCoin, constraintLayout)
        addRandomMonsters(gameModel.monsterImages, constraintLayout)
    }

    /** 상태바 및 하단바 색상 지정하는 함수 */
    private fun setBarColor() {
        window.statusBarColor = getColor(R.color.green)
        window.navigationBarColor = getColor(R.color.green)
    }

    /** dp → px 변환하는 함수 */
    private fun dpToPx(dp: Int): Int {
        val destiny = resources.displayMetrics.density
        return (dp * destiny).toInt()
    }

    /** 화면에 몬스터 이미지 표출하는 함수 */
    private fun addRandomMonsters(monsterImages: List<String>, parentLayout: ViewGroup) {
        for (monsterImage in monsterImages) {
            val monsterView = ImageView(this)
            monsterView.layoutParams = ViewGroup.LayoutParams(dpToPx(70), dpToPx(70))

            Glide.with(this)
                .load(monsterImage)
                .into(monsterView)

            val maxX = screenWidth - monsterView.width - dpToPx(70)
            val maxY = screenHeight - monsterView.height - dpToPx(70)
            monsterView.x = Random.nextInt(0, maxX).toFloat().coerceIn(0f, maxX.toFloat())
            monsterView.y = Random.nextInt(0, maxY).toFloat().coerceIn(0f, maxY.toFloat())

            parentLayout.addView(monsterView)
        }
    }

    /** 화면에 코인 이미지 표출하는 함수 */
    private fun addRandomBonusCoin(bonusCoin: Int, parentLayout: ViewGroup) {
        for (i in 0 until bonusCoin) {
            val coinView = ImageView(this)
            coinView.setImageResource(R.drawable.game_coin)
            coinView.layoutParams = ViewGroup.LayoutParams(dpToPx(30), dpToPx(30))

            val maxX = screenWidth - coinView.width - dpToPx(30)
            val maxY = screenHeight - coinView.height - dpToPx(30)
            coinView.x = Random.nextInt(0, maxX).toFloat().coerceIn(0f, maxX.toFloat())
            coinView.y = Random.nextInt(0, maxY).toFloat().coerceIn(0f, maxY.toFloat())

            parentLayout.addView(coinView)
        }
    }

    /** 유저 정보 업데이트 함수 */
    private fun updateUserInfo(user: GameModel) {
        updateTicketAmount(user.ticketAmount)
        updateCoinAmount(user.coinAmount)
    }

    /** 화면에 티켓 정보 업데이트 함수 */
    private fun updateTicketAmount(ticketAmount: Int) {
        val ticketAmountTextView = findViewById<TextView>(R.id.ticketAmount)
        ticketAmountTextView.text = ticketAmount.toString()
    }

    /** 화면에 코인 정보 업데이트 함수 */
    private fun updateCoinAmount(coinAmount: Int) {
        val coinAmountTextView = findViewById<TextView>(R.id.coinAmount)
        coinAmountTextView.text = coinAmount.toString()
    }
}