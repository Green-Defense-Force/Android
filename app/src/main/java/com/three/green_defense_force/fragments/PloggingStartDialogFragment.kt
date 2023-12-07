package com.three.green_defense_force.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.three.green_defense_force.R

class PloggingStartDialogFragment: DialogFragment() {
    // 다이얼로그 내 버튼 클릭 이벤트 처리
    interface OnStartClickListener {
        fun onStartClicked()
    }

    // 클릭 이벤트 리스너
    private var onStartClickListener: OnStartClickListener? = null

    // 클릭 이벤트 리스너 설정
    fun setOnStartClickListener(listener: OnStartClickListener) {
        onStartClickListener = listener
    }

    // 다이얼로그 레이아웃 설정
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_plogging_start, container, false)
    }

    // 레이아웃 생성 후, 자동 호출
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 다이얼로그 내 버튼 클릭 이벤트 설정
        val startBtn = view.findViewById<Button>(R.id.ploggingStartBtn)
        startBtn.setOnClickListener {
            onStartClickListener?.onStartClicked()
            dismiss()
        }
    }

    // 다이얼로그 생성 시, 자동 호출
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }
}