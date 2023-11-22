package com.three.green_defense_force.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.three.green_defense_force.R
import com.three.green_defense_force.models.Game
import com.three.green_defense_force.models.MonsterPreview
import com.three.green_defense_force.viewmodels.GameViewModel
import com.three.joystick.JoystickView
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    private lateinit var viewModel: GameViewModel
    private lateinit var charImageView: ImageView

    private var screenWidth: Int = 0
    private var screenHeight: Int = 0

    // 이전 각도, 이미지 저장하는 변수
    private var prevAngle: Int = 0
    private lateinit var prevImageResource: String

    // Seed : 현재 시간으로 설정
    private val random = Random(System.currentTimeMillis())

    // 상수
    private val MOVE_FACTOR = 0.2f
    private val COLOR_GREEN = R.color.green
    private val MONSTER_SIZE = 70
    private val COIN_SIZE = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        setBarColor()

        screenWidth = resources.displayMetrics.widthPixels
        screenHeight = resources.displayMetrics.heightPixels

        viewModel = GameViewModel()
        charImageView = findViewById(R.id.charImageView)

        val joystickView = findViewById<JoystickView>(R.id.joystick)
        val constraintLayout = findViewById<ViewGroup>(R.id.constraintLayout)

        val backBtn = findViewById<Button>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }

        // 가상 User 객체 생성
        val gameViewModel = viewModel.fetchUserData("6uiaYtLh")
        loadImageFromUrl(gameViewModel.characterImages[3], charImageView)

        joystickView.setOnMoveListener { angle, strength ->
            updateCharacterImage(angle, strength, charImageView, gameViewModel.characterImages)
            moveCharacter(angle, strength, screenWidth, screenHeight, charImageView)
        }

        updateUserInfo(gameViewModel)
        addRandomBonusCoin(gameViewModel.bonusCoin, constraintLayout)
        addRandomMonsters(gameViewModel.monsterPreviews, constraintLayout)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Glide 생명주기 관리
        Glide.with(this).clear(charImageView)
    }

    /** 상태바 및 하단바 색상 지정하는 함수 */
    private fun setBarColor() {
        window.statusBarColor = getColor(COLOR_GREEN)
        window.navigationBarColor = getColor(COLOR_GREEN)
    }

    /** dp → px 변환하는 함수 */
    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    /** URL → 이미지 변환하는 함수 */
    private fun loadImageFromUrl(url: String, imageView: ImageView) {
        Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)
    }

    /** 조이스틱의 각도에 따라 캐릭터 이미지 변경하는 함수 */
    private fun updateCharacterImage(
        angle: Int,
        strength: Int,
        charImageView: ImageView,
        userImages: List<String>
    ) {
        val imageResource = when {
            (angle >= 315 || angle < 45) -> if (strength == 0) userImages[9] else getWalkingImage(
                userImages[10],
                userImages[11]
            )

            (angle >= 45 && angle < 135) -> if (strength == 0) userImages[0] else getWalkingImage(
                userImages[1],
                userImages[2]
            )

            (angle >= 135 && angle < 225) -> if (strength == 0) userImages[6] else getWalkingImage(
                userImages[7],
                userImages[8]
            )

            (angle >= 225 && angle < 315) -> if (strength == 0) userImages[3] else getWalkingImage(
                userImages[4],
                userImages[5]
            )

            else -> prevImageResource
        }

        // 조이스틱이 멈췄을 때 이전 상태의 기본 이미지 유지
        if (strength == 0) {
            loadImageFromUrl(getPreviousDirectionImage(prevAngle, userImages), charImageView)
        } else {
            loadImageFromUrl(imageResource, charImageView)
            prevAngle = angle
            prevImageResource = imageResource
        }
    }

    /** 캐릭터 걷는 동작 설정하는 함수 */
    private fun getWalkingImage(image1: String, image2: String): String {
        // 번갈아 가며 이미지 반환
        return if (System.currentTimeMillis() % 1000 < 500) image1 else image2
    }

    /** 이전 상태 방향의 기본 이미지 반환하는 함수 */
    private fun getPreviousDirectionImage(angle: Int, userImages: List<String>): String {
        val direction = when {
            (angle >= 315 || angle < 45) -> userImages[9]
            (angle >= 45 && angle < 135) -> userImages[0]
            (angle >= 135 && angle < 225) -> userImages[6]
            (angle >= 225 && angle < 315) -> userImages[3]
            else -> userImages[3]
        }
        return direction
    }

    /** 캐릭터 움직이는 함수 */
    private fun moveCharacter(
        angle: Int,
        strength: Int,
        screenWidth: Int,
        screenHeight: Int,
        charImageView: ImageView
    ) {
        // 조이스틱의 각도를 라디안으로 변환
        val radian = Math.toRadians(angle.toDouble())

        // 조이스틱의 각도에 맞게 x, y 방향을 계산
        val moveX = strength * MOVE_FACTOR * Math.cos(radian).toFloat()
        val moveY = strength * MOVE_FACTOR * -Math.sin(radian).toFloat()

        // 현재 캐릭터의 위치
        val currentX = charImageView.x
        val currentY = charImageView.y

        // 이동 후 위치 계산
        val newX = currentX + moveX
        val newY = currentY + moveY

        // 화면 경계 내에 위치하도록 제한
        val clampedX = newX.coerceIn(0f, (screenWidth - charImageView.width).toFloat())
        val clampedY = newY.coerceIn(0f, (screenHeight - charImageView.height).toFloat())

        // "charImageView" 이미지 뷰의 위치 변경
        charImageView.x = clampedX
        charImageView.y = clampedY
    }

    /** 랜덤한 위치에 몬스터 이미지 표출하는 함수 */
    private fun addRandomMonsters(monsterPreview: List<MonsterPreview>, parentLayout: ViewGroup) {
        addRandomElements(
            monsterPreview.map { it.monsterImage },
            parentLayout,
            MONSTER_SIZE,
            MONSTER_SIZE,
            false
        )
    }

    /** 랜덤한 위치에 코인 이미지 표출하는 함수 */
    private fun addRandomBonusCoin(bonusCoin: Int, parentLayout: ViewGroup) {
        val coinImages = List(bonusCoin) { "" }
        addRandomElements(coinImages, parentLayout, COIN_SIZE, COIN_SIZE, true)
    }

    /** 랜덤한 위치에 이미지 표출하는 함수 */
    private fun addRandomElements(
        images: List<String>,
        parentLayout: ViewGroup,
        width: Int,
        height: Int,
        useLocalResource: Boolean
    ) {
        for (image in images) {
            val elementView = ImageView(this)
            elementView.layoutParams = ViewGroup.LayoutParams(dpToPx(width), dpToPx(height))

            if (useLocalResource) {
                elementView.setImageResource(R.drawable.game_coin)
            } else {
                loadImageFromUrl(image, elementView)
            }

            val maxX = screenWidth - elementView.width - dpToPx(width)
            val maxY = screenHeight - elementView.height - dpToPx(height)
            elementView.x = random.nextInt(0, maxX).toFloat().coerceIn(0f, maxX.toFloat())
            elementView.y = random.nextInt(0, maxY).toFloat().coerceIn(0f, maxY.toFloat())

            parentLayout.addView(elementView)
        }
    }

    /** 유저 정보 업데이트 함수 */
    private fun updateUserInfo(gameModel: Game) {
        updateTicketAmount(gameModel.ticketAmount)
        updateCoinAmount(gameModel.coinAmount)
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