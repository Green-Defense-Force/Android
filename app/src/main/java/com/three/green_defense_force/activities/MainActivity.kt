package com.three.green_defense_force.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavView)
        btnChallenge = findViewById(R.id.navChallenge)
        btnHome = findViewById(R.id.navHome)
        btnPlogging = findViewById(R.id.navPlogging)

        // 초기 화면 : HomeFragment
        replaceFragment(homeFragment)

        // 버튼 클릭 리스너 설정
        btnChallenge.setOnClickListener {
            selectFragment(challengeFragment)
        }
        btnHome.setOnClickListener {
            selectFragment(homeFragment)
        }
        btnPlogging.setOnClickListener {
            selectFragment(ploggingFragment)
        }

        // 홈 탭 : 기본 선택
        btnHome.isSelected = true
    }

    private fun selectFragment(fragment: Fragment) {
        replaceFragment(fragment)

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

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.bottomNavFragment, fragment)
            .commit()
    }
}