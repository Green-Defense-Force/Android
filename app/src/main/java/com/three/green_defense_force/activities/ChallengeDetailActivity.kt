package com.three.green_defense_force.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.three.green_defense_force.R
import com.three.green_defense_force.databinding.ActivityChallengeDetailBinding
import com.three.green_defense_force.extensions.setBackButton
import com.three.green_defense_force.extensions.setBarColor
import com.three.green_defense_force.models.ChallengeDetail
import com.three.green_defense_force.viewmodels.ChallengeDetailViewModel

class ChallengeDetailActivity : AppCompatActivity() {
    private lateinit var challengeDetailViewModel: ChallengeDetailViewModel
    private lateinit var challengeDetailModel: ChallengeDetail
    private lateinit var binding: ActivityChallengeDetailBinding

    // 상수
    private val COLOR_CHALLENGE_TOP = R.color.challenge_top
    private val COLOR_CHALLENGE_MAIN = R.color.challenge_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChallengeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBarColor(COLOR_CHALLENGE_TOP, COLOR_CHALLENGE_MAIN)
        setBackButton(R.id.backBtn)

        val userId = intent.getStringExtra("USER_ID")
        val challengeId = intent.getStringExtra("CHALLENGE_ID")

        challengeDetailViewModel = ChallengeDetailViewModel()
        challengeDetailModel =
            challengeDetailViewModel.fetchChallengeDetailData(challengeId.toString())

        with(binding) {
            challengeTitle.text = challengeDetailModel.challengeTitle
            rewardContent.text = when (challengeDetailModel.rewardType) {
                "ticket" -> "성공 시, 티켓 ${challengeDetailModel.rewardCount}장"
                "coin" -> "성공 시, 코인 ${challengeDetailModel.rewardCount}원"
                else -> ""
            }
            challengeContent.text = challengeDetailModel.challengeContent
            challengeGoal.text = challengeDetailModel.challengeGoal
            loadImageFromUrl(challengeDetailModel.challengeCorrectExample, challengeCorrectExample)
            loadImageFromUrl(challengeDetailModel.challengeWrongExample, challengeWrongExample)
            challengeChecklist.text = challengeDetailModel.challengeChecklist
        }

        val takeBtn: Button = findViewById(R.id.takeBtn)
        takeBtn.setOnClickListener {
            val intent = Intent(this, ChallengeCameraActivity::class.java).apply {
                putExtra("USER_ID", userId)
                putExtra("CHALLENGE_ID", challengeId)
                putExtra("CHALLENGE_TITLE", challengeDetailModel.challengeTitle)
                putExtra("REWARD_TYPE", challengeDetailModel.rewardType)
                putExtra("REWARD_COUNT", challengeDetailModel.rewardCount)
            }
             startActivity(intent)
        }
    }

    private fun loadImageFromUrl(url: String, imageView: ImageView) {
        Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)
    }
}