package com.three.green_defense_force.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.three.green_defense_force.R
import com.three.green_defense_force.models.ChallengePreview

class ChallengeAdapter(private val challenges: List<ChallengePreview>) :
    RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_item_challenge, parent, false)
        return ChallengeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        val challenge = challenges[position]

        // Glide : 이미지 로딩
        Glide.with(holder.itemView.context)
            .load(challenge.rewardImage)
            .into(holder.rewardImage)

        // rewardCount = 1, TextView 숨김
        if (challenge.rewardCount == 1) {
            holder.rewardCount.text = ""
        } else {
            holder.rewardCount.text = "X${challenge.rewardCount}"
        }

        holder.challengeContent.text = challenge.challengeTitle
    }

    override fun getItemCount(): Int {
        return challenges.size
    }

    class ChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rewardImage: ImageView = itemView.findViewById(R.id.rewardImage)
        val rewardCount: TextView = itemView.findViewById(R.id.rewardCount)
        val challengeContent: TextView = itemView.findViewById(R.id.challengeContent)
    }
}