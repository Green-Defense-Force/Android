package com.three.green_defense_force.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.three.green_defense_force.R
import com.three.green_defense_force.activities.ChallengeDetailActivity
import com.three.green_defense_force.models.ChallengePreview

class ChallengeAdapter(
    private val context: Context,
    private val userId: String,
    private val challenges: List<ChallengePreview>
) :
    RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_item_challenge, parent, false)
        return ChallengeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        val challenge = challenges[position]

        if (challenge.rewardType == "ticket") {
            holder.rewardImage.setImageResource(R.drawable.game_ticket)
        } else if (challenge.rewardType == "coin") {
            holder.rewardImage.setImageResource(R.drawable.game_coin)
        }

        // rewardCount = 1, TextView 숨김
        if (challenge.rewardCount == 1) {
            holder.rewardCount.text = ""
        } else {
            holder.rewardCount.text = "X${challenge.rewardCount}"
        }

        holder.challengeTitle.text = challenge.challengeTitle

        // 각 아이템 클릭 이벤트
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChallengeDetailActivity::class.java).apply {
                putExtra("USER_ID", userId)
                putExtra("CHALLENGE_ID", challenge.challengeId)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return challenges.size
    }

    class ChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rewardImage: ImageView = itemView.findViewById(R.id.rewardImage)
        val rewardCount: TextView = itemView.findViewById(R.id.rewardCount)
        val challengeTitle: TextView = itemView.findViewById(R.id.challengeTitle)
    }
}