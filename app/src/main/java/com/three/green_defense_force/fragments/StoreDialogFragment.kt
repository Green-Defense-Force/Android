package com.three.green_defense_force.fragments

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.three.green_defense_force.R
import com.three.green_defense_force.adapter.StoreAdapter
import com.three.green_defense_force.models.StoreItem
import com.three.green_defense_force.viewmodels.StoreViewModel

class StoreDialogFragment : DialogFragment(), StoreAdapter.OnItemClickListener {
    private lateinit var hairBtn: Button
    private lateinit var topBtn: Button
    private lateinit var pantsBtn: Button
    private lateinit var shoesBtn: Button
    private lateinit var weaponBtn: Button
    private lateinit var accBtn: Button
    private lateinit var closeBtn: Button

    private val storeViewModel = StoreViewModel()
    private var selectedCategory: String? = null
    private var onStartClickListener: OnStartClickListener? = null

    private val USER_ID = "6uiaYtLh"

    interface OnStartClickListener {
        fun onStartClicked()
    }

    fun setOnStartClickListener(listener: OnStartClickListener) {
        onStartClickListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_store_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadUserEquippedItems(view)
        initCategoryButtons(view)

        closeBtn = view.findViewById(R.id.storeCloseBtn)
        closeBtn.setOnClickListener { dismiss() }

        selectedCategory(hairBtn, "hair")
        showCategory("hair")
    }

    private fun initCategoryButtons(view: View) {
        hairBtn = view.findViewById(R.id.hairCategory)
        topBtn = view.findViewById(R.id.topCategory)
        pantsBtn = view.findViewById(R.id.pantsCategory)
        shoesBtn = view.findViewById(R.id.shoesCategory)
        weaponBtn = view.findViewById(R.id.weaponCategory)
        accBtn = view.findViewById(R.id.accCategory)

        hairBtn.setOnClickListener { handleCategoryClick("hair") }
        topBtn.setOnClickListener { handleCategoryClick("top") }
        pantsBtn.setOnClickListener { handleCategoryClick("pants") }
        shoesBtn.setOnClickListener { handleCategoryClick("shoes") }
        weaponBtn.setOnClickListener { handleCategoryClick("weapon") }
        accBtn.setOnClickListener { handleCategoryClick("acc") }
    }

    private fun handleCategoryClick(categoryId: String) {
        selectedCategory(findButtonByCategory(categoryId), categoryId)
        showCategory(categoryId)
    }

    private fun loadUserEquippedItems(view: View) {
        val userData = storeViewModel.fetchUserData(USER_ID)
        userData.equippedItems.forEach { (categoryId, imageUrl) ->
            val imageView = when (categoryId) {
                "hair" -> view.findViewById<ImageView>(R.id.hairImageView)
                "top" -> view.findViewById<ImageView>(R.id.topImageView)
                "pants" -> view.findViewById<ImageView>(R.id.pantsImageView)
                "shoes" -> view.findViewById<ImageView>(R.id.shoesImageView)
                "acc" -> view.findViewById<ImageView>(R.id.glassesImageView)
                else -> null
            }

            imageView?.let {
                if (imageUrl != null) {
                    loadImageAndGetDimensions(it, imageUrl)
                }
            }
        }
    }

    private fun findButtonByCategory(categoryId: String): Button {
        return when (categoryId) {
            "hair" -> hairBtn
            "top" -> topBtn
            "pants" -> pantsBtn
            "shoes" -> shoesBtn
            "weapon" -> weaponBtn
            "acc" -> accBtn
            else -> hairBtn
        }
    }

    private fun selectedCategory(button: Button, categoryId: String) {
        unselectCategory()
        button.setBackgroundResource(R.drawable.store_inventory_category_checked)
        selectedCategory = categoryId
    }

    private fun unselectCategory() {
        hairBtn.setBackgroundResource(R.drawable.store_inventory_category_unchecked)
        topBtn.setBackgroundResource(R.drawable.store_inventory_category_unchecked)
        pantsBtn.setBackgroundResource(R.drawable.store_inventory_category_unchecked)
        shoesBtn.setBackgroundResource(R.drawable.store_inventory_category_unchecked)
        weaponBtn.setBackgroundResource(R.drawable.store_inventory_category_unchecked)
        accBtn.setBackgroundResource(R.drawable.store_inventory_category_unchecked)
    }

    private fun showCategory(categoryId: String) {
        val storeItems = storeViewModel.fetchItems(USER_ID, categoryId)
        setRecyclerView(storeItems)
    }

    private fun setRecyclerView(storeItems: List<StoreItem>) {
        val recyclerView: RecyclerView = view?.findViewById(R.id.storeRecyclerView) ?: return
        val spanCount = 3 // 그리드 열 (3)
        val layoutManager = GridLayoutManager(context, spanCount)

        if (recyclerView.adapter == null) {
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = StoreAdapter(requireContext(), USER_ID, storeItems, this).apply {
                currentCategory = selectedCategory.toString()
            }
        } else {
            (recyclerView.adapter as StoreAdapter).apply {
                updateItems(storeItems)
                currentCategory = selectedCategory.toString()
            }
        }
    }

    override fun onItemSelected(categoryId: String, imageUrl: String) {
        val imageView = when (categoryId) {
            "hair" -> view?.findViewById<ImageView>(R.id.hairImageView)
            "top" -> view?.findViewById<ImageView>(R.id.topImageView)
            "pants" -> view?.findViewById<ImageView>(R.id.pantsImageView)
            "shoes" -> view?.findViewById<ImageView>(R.id.shoesImageView)
            "acc" -> view?.findViewById<ImageView>(R.id.glassesImageView)
            else -> null
        }

        imageView?.let {
            loadImageAndGetDimensions(it, imageUrl)
        }
    }

    override fun onItemDeselected(categoryId: String) {
        val imageView = when (categoryId) {
            "hair" -> view?.findViewById<ImageView>(R.id.hairImageView)
            "top" -> view?.findViewById<ImageView>(R.id.topImageView)
            "pants" -> view?.findViewById<ImageView>(R.id.pantsImageView)
            "shoes" -> view?.findViewById<ImageView>(R.id.shoesImageView)
            "acc" -> view?.findViewById<ImageView>(R.id.glassesImageView)
            else -> null
        }

        imageView?.setImageDrawable(null) // 아이템 선택 해제 시 이미지 제거
    }

    private fun loadImageAndGetDimensions(imageView: ImageView, imageUrl: String) {
        Glide.with(this)
            .asBitmap()
            .load(imageUrl)
            .into(object : Target<Bitmap> {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    imageView.setImageBitmap(resource)

                    // ImageView의 크기를 원본 이미지 크기에 맞춰 조절
                    val layoutParams = imageView.layoutParams
                    layoutParams.width = dpToPx(requireContext(), resource.width.toFloat())
                    layoutParams.height = dpToPx(requireContext(), resource.height.toFloat())
                    imageView.layoutParams = layoutParams
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
    private fun dpToPx(context: Context, dp: Float): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }
}