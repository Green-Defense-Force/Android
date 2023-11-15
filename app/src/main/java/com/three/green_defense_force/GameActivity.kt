package com.three.green_defense_force

import android.animation.Animator
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView

class GameActivity : AppCompatActivity() {
    private lateinit var stopCharacter: ImageView
    private lateinit var walkCharacter: ImageView
    private lateinit var attackCharacter: ImageView
    private lateinit var leftBtn: ImageView
    private lateinit var rightBtn: ImageView
    private lateinit var attackBtn: ImageView

    private var moveDistance = 80f // 초당 이동할 거리

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        stopCharacter = findViewById(R.id.stopCharacter)
        walkCharacter = findViewById(R.id.walkCharacter)
        attackCharacter = findViewById(R.id.attackCharacter)

        leftBtn = findViewById(R.id.leftBtn)
        rightBtn = findViewById(R.id.rightBtn)
        attackBtn = findViewById(R.id.attackBtn)

        // 시작 시 : walkCharacter, attackCharacter 안 보이게 설정
        walkCharacter.visibility = View.INVISIBLE
        attackCharacter.visibility = View.INVISIBLE

        // 방향키 클릭 시 : 캐릭터 이동
        leftBtn.setOnClickListener {
            moveCharacter(walkCharacter, -moveDistance)
        }
        rightBtn.setOnClickListener {
            moveCharacter(walkCharacter, moveDistance)
        }

        // 공격키 클릭 시 : 몬스터 공격
        attackBtn.setOnClickListener {
            attackMonster()
        }
    }

    // 캐릭터 이동 함수
    private fun moveCharacter(character: ImageView, distance: Float) {
        // walkCharacter 보이게 설정
        walkCharacter.visibility = View.VISIBLE
        stopCharacter.visibility = View.INVISIBLE

        val animator = ObjectAnimator.ofFloat(
            character, // 움직일 대상
            "translationX", // 움직일 방향
            character.translationX + distance // 움직일 거리
        )

        animator.duration = 200 // 움직일 시간 (0.2초)

        animator.addListener(object : Animator.AnimatorListener {
            // 종료 리스너
            override fun onAnimationEnd(animation: Animator) {
                // 모든 캐릭터의 위치 : walkCharacter와 동일
                stopCharacter.translationX = walkCharacter.translationX
                attackCharacter.translationX = walkCharacter.translationX

                // 애니메이션 종료 시 : stopCharacter 보이게 설정
                walkCharacter.visibility = View.INVISIBLE
                stopCharacter.visibility = View.VISIBLE
            }

            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })

        animator.start() // 애니메이션 시작
    }

    // 몬스터 공격 함수
    private fun attackMonster() {
        // attackCharacter 보이게 설정
        attackCharacter.visibility = View.VISIBLE
        stopCharacter.visibility = View.INVISIBLE

        val animator = ObjectAnimator.ofFloat(
            attackCharacter, // 움직일 대상
            "alpha", // 투명도 조절
            1f // 1f : 완전히 보이게
        )

        animator.duration = 100 // 움직일 시간 (0.1초)

        animator.addListener(object : Animator.AnimatorListener {
            // 종료 리스너
            override fun onAnimationEnd(animation: Animator) {
                // 애니메이션 종료 시 : stopCharacter 보이게 설정
                attackCharacter.visibility = View.INVISIBLE
                stopCharacter.visibility = View.VISIBLE
            }

            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })

        animator.start() // 애니메이션 시작
    }
}