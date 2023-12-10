package com.three.green_defense_force.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.three.green_defense_force.R
import com.three.green_defense_force.adapter.StoreAdapter
import com.three.green_defense_force.models.StoreItem
import com.three.green_defense_force.viewmodels.StoreViewModel

class StoreDialogFragment : DialogFragment() {
    private lateinit var hairBtn: Button
    private lateinit var topBtn: Button
    private lateinit var pantsBtn: Button
    private lateinit var shoesBtn: Button
    private lateinit var weaponBtn: Button
    private lateinit var accBtn: Button

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

        val closeBtn: Button = view.findViewById(R.id.storeCloseBtn)
        closeBtn.setOnClickListener {
            dismiss()
        }

        hairBtn = view.findViewById(R.id.hairCategory)
        topBtn = view.findViewById(R.id.topCategory)
        pantsBtn = view.findViewById(R.id.pantsCategory)
        shoesBtn = view.findViewById(R.id.shoesCategory)
        weaponBtn = view.findViewById(R.id.weaponCategory)
        accBtn = view.findViewById(R.id.accCategory)

        // 초기 설정 : hair
        selectedCategory(hairBtn, "hair")
        showCategory("hair")

        hairBtn.setOnClickListener {
            selectedCategory(hairBtn, "hair")
            showCategory("hair")
        }
        topBtn.setOnClickListener {
            selectedCategory(topBtn, "top")
            showCategory("top")
        }
        pantsBtn.setOnClickListener {
            selectedCategory(pantsBtn, "pants")
            showCategory("pants")
        }
        shoesBtn.setOnClickListener {
            selectedCategory(shoesBtn, "shoes")
            showCategory("shoes")
        }
        weaponBtn.setOnClickListener {
            selectedCategory(weaponBtn, "weapon")
            showCategory("weapon")
        }
        accBtn.setOnClickListener {
            selectedCategory(accBtn, "acc")
            showCategory("acc")
        }
    }

    private fun selectedCategory(button: Button, categoryId: String) {
        unselectCategory()
        button.setBackgroundResource(R.drawable.store_category_checked)
        selectedCategory = categoryId
    }

    private fun unselectCategory() {
        hairBtn.setBackgroundResource(R.drawable.store_category_unchecked)
        topBtn.setBackgroundResource(R.drawable.store_category_unchecked)
        pantsBtn.setBackgroundResource(R.drawable.store_category_unchecked)
        shoesBtn.setBackgroundResource(R.drawable.store_category_unchecked)
        weaponBtn.setBackgroundResource(R.drawable.store_category_unchecked)
        accBtn.setBackgroundResource(R.drawable.store_category_unchecked)
    }

    private fun showCategory(categoryId: String) {
        val storeItems = storeViewModel.fetchItems(USER_ID, categoryId)
        setRecyclerView(storeItems)
    }

    private fun setRecyclerView(storeItems: List<StoreItem>) {
        val recyclerView: RecyclerView = view?.findViewById(R.id.storeRecyclerView) ?: return

        // 그리드의 열 수 (3열)
        val spanCount = 3
        val layoutManager = GridLayoutManager(context, spanCount)

        if (recyclerView.adapter == null) {
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = StoreAdapter(requireContext(), USER_ID, storeItems).apply {
                currentCategory = selectedCategory.toString()
            }
        } else {
            (recyclerView.adapter as StoreAdapter).apply {
                updateItems(storeItems)
                currentCategory = selectedCategory.toString()
            }
        }
    }
}