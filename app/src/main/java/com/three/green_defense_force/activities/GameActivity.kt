package com.three.green_defense_force.activities

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
    private lateinit var gameViewModel: GameViewModel
    private lateinit var gameModel: Game
    private lateinit var fieldView: ImageView
    private lateinit var charImageView: ImageView
    private lateinit var joystickView: JoystickView
    private lateinit var constraintLayout: ViewGroup

    private var screenWidth: Int = 0
    private var screenHeight: Int = 0
    private var walkingCounter: Int = 0
    private var isStop: Boolean = false

    // 이전 각도, 이미지 저장하는 변수
    private var prevAngle: Int = 0
    private lateinit var prevImageResource: String

    // Seed : 현재 시간으로 설정
    private val random = Random(System.currentTimeMillis())

    // 동적 생성된 ImageView 저장하는 리스트
    private val coinViews = mutableListOf<ImageView>()
    private val monsterViews = mutableListOf<ImageView>()

    // 상수
    private val COLOR_GREEN = R.color.green
    private val MOVE_FACTOR = 0.25f
    private val MONSTER_SIZE = 70
    private val COIN_SIZE = 30
    private val WALKING_SPEED = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        setBarColor()
        setBackButton()

        screenWidth = resources.displayMetrics.widthPixels
        screenHeight = resources.displayMetrics.heightPixels

        gameViewModel = GameViewModel()
        gameModel = gameViewModel.fetchGameData("6uiaYtLh")

        fieldView = findViewById(R.id.fieldView)
        charImageView = findViewById(R.id.charImageView)
        joystickView = findViewById(R.id.joystick)
        constraintLayout = findViewById(R.id.constraintLayout)

        loadImageFromUrl(gameModel.field, fieldView)
        loadImageFromUrl(gameModel.characterImages[3], charImageView)

        setJoystick()
        setTicketAmount(gameModel.ticketAmount)
        setCoinAmount(gameModel.coinAmount)
        addRandomBonusCoin(gameModel.bonusCoin, constraintLayout)
        addRandomMonsters(gameModel.monsterPreviews, constraintLayout)
    }

    override fun onResume() {
        super.onResume()
        isStop = false
        joystickView.setAngle(0)
        joystickView.setStrength(0)
        joystickView.setOnMoveListener { angle, strength ->
            if (!isStop) {
                updateCharacterImage(angle, strength, charImageView, gameModel.characterImages)
                moveCharacter(angle, strength, screenWidth, screenHeight, charImageView)
                checkCoinIntersect(charImageView)
                checkMonsterIntersect(charImageView)
            }
        }
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

    /** 뒤로 가기 버튼 설정하는 함수 */
    private fun setBackButton() {
        val backBtn: Button = findViewById(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }
    }

    /** 조이스틱 버튼 설정하는 함수 */
    private fun setJoystick() {
        joystickView.setOnMoveListener { angle, strength ->
            if (!isStop) {
                updateCharacterImage(angle, strength, charImageView, gameModel.characterImages)
                moveCharacter(angle, strength, screenWidth, screenHeight, charImageView)
                checkCoinIntersect(charImageView)
                checkMonsterIntersect(charImageView)
            }
        }
    }

    /** 조이스틱 각도에 따라 캐릭터 이미지 변경하는 함수 */
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

        // 조이스틱 멈췄을 때 이전 상태의 기본 이미지 유지
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
        walkingCounter = (walkingCounter + 1) % WALKING_SPEED
        return if (walkingCounter < 5) image1 else image2
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

    /** 화면에 티켓 정보 표출하는 함수 */
    private fun setTicketAmount(ticketAmount: Int) {
        val ticketAmountTextView = findViewById<TextView>(R.id.ticketAmount)
        ticketAmountTextView.text = ticketAmount.toString()
    }

    /** 화면에 코인 정보 표출하는 함수 */
    private fun setCoinAmount(coinAmount: Int) {
        val coinAmountTextView = findViewById<TextView>(R.id.coinAmount)
        coinAmountTextView.text = coinAmount.toString()
    }

    /** 화면에 몬스터 이미지 표출하는 함수 */
    private fun addRandomMonsters(monsterPreviews: List<MonsterPreview>, parentLayout: ViewGroup) {
        val monsterImages = monsterPreviews.map { it.monsterImage }

        for (monsterImage in monsterImages) {
            val monsterView = ImageView(this)
            monsterView.layoutParams =
                ViewGroup.LayoutParams(dpToPx(MONSTER_SIZE), dpToPx(MONSTER_SIZE))

            loadImageFromUrl(monsterImage, monsterView)

            val maxX = screenWidth - monsterView.width - dpToPx(MONSTER_SIZE)
            val maxY = screenHeight - monsterView.height - dpToPx(MONSTER_SIZE)
            monsterView.x = random.nextInt(0, maxX).toFloat().coerceIn(0f, maxX.toFloat())
            monsterView.y = random.nextInt(0, maxY).toFloat().coerceIn(0f, maxY.toFloat())

            monsterView.tag = monsterPreviews.first { it.monsterImage == monsterImage }.monsterId

            parentLayout.addView(monsterView)
            monsterViews.add(monsterView)
        }
    }

    /** 화면에 보너스 코인 이미지 표출하는 함수 */
    private fun addRandomBonusCoin(bonusCoin: Int, parentLayout: ViewGroup) {
        for (i in 0 until bonusCoin) {
            val coinView = ImageView(this)
            coinView.setImageResource(R.drawable.game_coin)
            coinView.layoutParams = ViewGroup.LayoutParams(dpToPx(COIN_SIZE), dpToPx(COIN_SIZE))

            val maxX = screenWidth - coinView.width - dpToPx(COIN_SIZE)
            val maxY = screenHeight - coinView.height - dpToPx(COIN_SIZE)
            coinView.x = random.nextInt(0, maxX).toFloat().coerceIn(0f, maxX.toFloat())
            coinView.y = random.nextInt(0, maxY).toFloat().coerceIn(0f, maxY.toFloat())

            parentLayout.addView(coinView)
            coinViews.add(coinView)
        }
    }

    /** 몬스터와 만났을 때 처리 담당하는 함수 */
    private fun checkMonsterIntersect(charImageView: ImageView) {
        for (monsterView in monsterViews) {
            val charRect = Rect()
            val monsterRect = Rect()

            charImageView.getHitRect(charRect)
            monsterView.getHitRect(monsterRect)

            if (Rect.intersects(charRect, monsterRect)) {
                isStop = true
                joystickView.setAngle(0)
                joystickView.setStrength(0)
                loadImageFromUrl(
                    getPreviousDirectionImage(prevAngle, gameModel.characterImages),
                    charImageView
                )

                val monsterId = monsterView.tag as String
                val intent = Intent(this, GameDetailActivity::class.java).apply {
                    putExtra("MONSTER_ID", monsterId)
                    putExtra("USER_ID", gameModel.userId)
                }
                startActivity(intent)

                gameViewModel.handleTicketIntersect(gameModel)
                setTicketAmount(gameModel.ticketAmount)
                monsterView.visibility = View.GONE
                monsterViews.remove(monsterView)
                break
            }
        }
    }

    /** 코인과 만났을 때 처리 담당하는 함수 */
    private fun checkCoinIntersect(charImageView: ImageView) {
        for (coinView in coinViews) {
            val charRect = Rect()
            val coinRect = Rect()

            charImageView.getHitRect(charRect)
            coinView.getHitRect(coinRect)

            if (Rect.intersects(charRect, coinRect)) {
                gameViewModel.handleCoinIntersect(gameModel)
                setCoinAmount(gameModel.coinAmount)
                coinView.visibility = View.GONE
                coinViews.remove(coinView)
                break
            }
        }
    }
}