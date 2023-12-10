package com.three.green_defense_force.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.three.green_defense_force.R
import com.three.green_defense_force.models.StoreItem

class StoreAdapter(
    private val context: Context,
    private val userId: String,
    private var storeItems: List<StoreItem>
) : RecyclerView.Adapter<StoreAdapter.StoreViewHolder>() {

    private val selectedItemPositions: MutableMap<String, Int> = mutableMapOf()
    var currentCategory: String = ""
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_item_store, parent, false)
        return StoreViewHolder(view)
    }

    override fun getItemCount(): Int {
        return storeItems.size
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val currentItem = storeItems[position]

        // 이미지 로드
        Glide.with(context)
            .load(currentItem.itemImage)
            .onlyRetrieveFromCache(true)
            .into(holder.itemImageView)

        holder.priceTextView.text = currentItem.itemPrice.toString()

        // 아이템 클릭 이벤트 처리
        holder.itemView.setOnClickListener {
            onItemClicked(position)
        }

        // 선택한 아이템 배경색 변경
        val selectedItemPosition = selectedItemPositions[currentCategory]
        if (selectedItemPosition != null && selectedItemPosition == position) {
            holder.itemView.setBackgroundResource(R.drawable.recycler_view_item_select)
        } else {
            holder.itemView.setBackgroundResource(R.drawable.recycler_view_item_unselect)
        }
    }

    /** 상점 아이템 리스트 업데이트 함수 */
    fun updateItems(newItems: List<StoreItem>) {
        storeItems = newItems
        notifyDataSetChanged()
    }

    private fun onItemClicked(position: Int) {
        val selectedItemPosition = selectedItemPositions[currentCategory]

        when (selectedItemPosition) {
            // 클릭된 아이템 재클릭 시, 해제
            position -> selectedItemPositions[currentCategory] = RecyclerView.NO_POSITION
            // 새로운 아이템 선택 시, 선택
            else -> selectedItemPositions[currentCategory] = position
        }
        notifyDataSetChanged()
    }

    class StoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImageView: ImageView = itemView.findViewById(R.id.itemImageView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
    }
}