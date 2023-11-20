package com.three.green_defense_force.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.three.green_defense_force.R
import com.three.green_defense_force.fragments.ChallengeFragment
import com.three.green_defense_force.fragments.HomeFragment
import com.three.green_defense_force.fragments.MypageFragment
import com.three.green_defense_force.fragments.PloggingFragment
import com.three.green_defense_force.fragments.StoreFragment

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    private val homeFragment = HomeFragment()
    private val challengeFragment = ChallengeFragment()
    private val ploggingFragment = PloggingFragment()
    private val storeFragment = StoreFragment()
    private val mypageFragment = MypageFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_nav_view)

        // 초기 화면 : HomeFragment
        replaceFragment(homeFragment)

        // BottomNavigationView의 아이템 클릭 리스너 설정
        bottomNavigationView.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.nav_mypage -> mypageFragment
                R.id.nav_plogging -> ploggingFragment
                R.id.nav_home -> homeFragment
                R.id.nav_store -> storeFragment
                R.id.nav_challenge -> challengeFragment
                else -> homeFragment
            }
            replaceFragment(fragment)
            true
        }

        // 홈 탭 : 기본 선택
        bottomNavigationView.menu.findItem(R.id.nav_home)?.isChecked = true
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.bottom_nav_fragment, fragment)
            .commit()
    }
}