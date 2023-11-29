package com.three.green_defense_force.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.three.green_defense_force.R
import com.three.green_defense_force.adapter.ChallengeAdapter
import com.three.green_defense_force.viewmodels.ChallengeViewModel

class ChallengeFragment : Fragment() {
    private val COLOR_CHALLENGE_TOP = R.color.challenge_top
    private val COLOR_NAVI_BOTTOM = R.color.navi_bottom

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBarColor()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment의 레이아웃 inflate
        val view = inflater.inflate(R.layout.fragment_challenge, container, false)

        // RecyclerView 설정
        val recyclerView: RecyclerView = view.findViewById(R.id.challengeRecyclerView)

        // ChallengeViewModel을 사용하여 데이터 가져오기
        val challengeViewModel = ChallengeViewModel()
        val challengeData = challengeViewModel.fetchChallengeData("6uiaYtLh")

        // Adapter에 가져온 데이터 설정
        val challengeAdapter = ChallengeAdapter(requireContext(), challengeData.challengePreviews)
        recyclerView.adapter = challengeAdapter

        // RecyclerView의 레이아웃 매니저 설정
        recyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    /** 상태바 및 하단바 색상 지정하는 함수 */
    private fun setBarColor() {
        val window = requireActivity().window
        val context = requireContext()
        window.statusBarColor = ContextCompat.getColor(context, COLOR_CHALLENGE_TOP)
        window.navigationBarColor = ContextCompat.getColor(context, COLOR_NAVI_BOTTOM)
    }
}