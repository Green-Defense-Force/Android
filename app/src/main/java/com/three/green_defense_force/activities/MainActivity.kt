package com.three.green_defense_force.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.three.green_defense_force.R
import com.three.green_defense_force.fragments.ChallengeFragment
import com.three.green_defense_force.fragments.HomeFragment
import com.three.green_defense_force.fragments.PloggingFragment

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: LinearLayout
    private lateinit var btnChallenge: Button
    private lateinit var btnHome: Button
    private lateinit var btnPlogging: Button

    private val homeFragment = HomeFragment()
    private val challengeFragment = ChallengeFragment()
    private val ploggingFragment = PloggingFragment()
    private var activeFragment: Fragment = homeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavView)
        btnChallenge = findViewById(R.id.navChallenge)
        btnHome = findViewById(R.id.navHome)
        btnPlogging = findViewById(R.id.navPlogging)

        supportFragmentManager.beginTransaction().apply {
            add(R.id.bottomNavFragment, challengeFragment, "2").hide(challengeFragment)
            add(R.id.bottomNavFragment, homeFragment, "1")
        }.commit()

        // 버튼 클릭 리스너 설정
        btnChallenge.setOnClickListener {
            selectFragment(challengeFragment)
        }
        btnHome.setOnClickListener {
            selectFragment(homeFragment)
        }
        btnPlogging.setOnClickListener {
            // 최초 버튼 클릭 시 → 플로깅 프래그먼트 추가
            if (!ploggingFragment.isAdded) {
                supportFragmentManager.beginTransaction()
                    .add(R.id.bottomNavFragment, ploggingFragment, "3").hide(ploggingFragment)
                    .commit()
            }
            selectFragment(ploggingFragment)
        }

        // 홈 탭 : 기본 선택
        btnHome.isSelected = true
    }

    private fun selectFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().hide(activeFragment).show(fragment).commit()
        activeFragment = fragment

        // 선택한 Fragment UI 설정
        when (fragment) {
            is ChallengeFragment -> setBarColor(R.color.challenge_top, R.color.navi_bottom)
            is HomeFragment -> setBarColor(R.color.main_top, R.color.navi_bottom)
            is PloggingFragment -> setBarColor(R.color.plogging_top, R.color.navi_bottom)
        }

        // 선택한 버튼 스타일 변경
        btnChallenge.setBackgroundResource(
            if (fragment == challengeFragment) R.drawable.bottom_nav_item_selected
            else R.drawable.bottom_nav_item_unselected
        )
        btnHome.setBackgroundResource(
            if (fragment == homeFragment) R.drawable.bottom_nav_item_selected
            else R.drawable.bottom_nav_item_unselected
        )
        btnPlogging.setBackgroundResource(
            if (fragment == ploggingFragment) R.drawable.bottom_nav_item_selected
            else R.drawable.bottom_nav_item_unselected
        )
    }

    private fun setBarColor(statusBarColor: Int, navigationBarColor: Int) {
        val window = window
        val context = applicationContext
        window.statusBarColor = ContextCompat.getColor(context, statusBarColor)
        window.navigationBarColor = ContextCompat.getColor(context, navigationBarColor)
    }
}