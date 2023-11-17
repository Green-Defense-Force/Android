package com.three.green_defense_force

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatButton

/*
 * CustomButton
 * AppCompatButton을 확장한 커스텀 버튼
 * 외부 터치 이벤트를 처리할 수 있도록 OnTouchListener 등록 가능
*/
class CustomButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    private var onTouchListener: OnTouchListener? = null

    fun setOnTouchEventListener(listener: OnTouchListener) {
        this.onTouchListener = listener
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        onTouchListener?.onTouch(this, event)
        // true  : 해당 View에서 터치 이벤트 소비 및 종료
        // false : 해당 View에서 터치 이벤트 계속 전달
        return false
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }
}