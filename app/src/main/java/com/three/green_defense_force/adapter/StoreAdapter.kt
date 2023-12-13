package com.three.green_defense_force.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.request.target.Target
import com.three.green_defense_force.R
import com.three.green_defense_force.models.StoreItem

class StoreAdapter(
    private val context: Context,
    private val userId: String,
//    private var userCoin: Int,
    private var storeItems: List<StoreItem>,
    private val onItemClickListener: OnItemClickListener
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
        // (1) 사용자 정보 로드
//        holder.coinTextView.text = userCoin.toString()

        // (2) 아이템 로드
        val currentItem = storeItems[position]

        // 이미지 로드
        loadImageAndGetDimensions(holder, currentItem.itemImage)

        // 텍스트 로드
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

        if (selectedItemPosition != null && selectedItemPosition == position) {
            // 클릭된 아이템 재 클릭 시, 선택 해제
            selectedItemPositions[currentCategory] = RecyclerView.NO_POSITION

            // 선택 해제 이벤트 전달
            onItemClickListener.onItemDeselected(currentCategory)
        } else {
            // 새로운 아이템 클릭 시, 선택
            selectedItemPositions[currentCategory] = position

            // 선택된 아이템 이미지 URL 가져오기
            val selectedItem = storeItems[position]
            val selectedImageUrl = selectedItem.itemImage

            // 아이템 선택 이벤트 전달
            onItemClickListener.onItemSelected(currentCategory, selectedImageUrl)
        }

        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemSelected(categoryId: String, imageUrl: String)
        fun onItemDeselected(categoryId: String)
    }

    private fun loadImageAndGetDimensions(holder: StoreViewHolder, imageUrl: String) {
        // Bitmap으로 이미지 로드
        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .into(object : Target<Bitmap> {
                // 이미지 로드 성공 시
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    // resource : 로드된 Bitmap
                    holder.itemImageView.setImageBitmap(resource)

                    // 이미지의 원본 크기 확인
                    val width = resource.width
                    val height = resource.height

                    // ImageView의 크기를 원본 이미지 크기에 맞춰 조절
                    val layoutParams = holder.itemImageView.layoutParams
                    layoutParams.width = dpToPx(context, width.toFloat())
                    layoutParams.height = dpToPx(context, height.toFloat())
                    holder.itemImageView.layoutParams = layoutParams
                }

                override fun onLoadStarted(placeholder: Drawable?) {}
                override fun onLoadFailed(errorDrawable: Drawable?) {}
                override fun onLoadCleared(placeholder: Drawable?) {}
                override fun getSize(cb: SizeReadyCallback) {
                    cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                }

                override fun removeCallback(cb: SizeReadyCallback) {}
                override fun setRequest(request: Request?) {}
                override fun getRequest(): Request? {
                    return null
                }

                override fun onStart() {}
                override fun onStop() {}
                override fun onDestroy() {}
            })
    }

    /** dp → px 변환하는 함수 */
    fun dpToPx(context: Context, dp: Float): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }

    class StoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImageView: ImageView = itemView.findViewById(R.id.itemImageView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
    }
}