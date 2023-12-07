package com.three.green_defense_force.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ChallengeCameraViewModel: ViewModel() {
    /** 서버에 이미지 및 데이터 전송하는 함수 */
    fun sendImageToServer(userId: String, challengeId: String, challengeImage: Uri?) {
        viewModelScope.launch {
            // 서버 전송
        }
    }
}