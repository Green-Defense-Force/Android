package com.three.green_defense_force.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.three.green_defense_force.R

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_challenge, container, false)
    }

    /** 상태바 및 하단바 색상 지정하는 함수 */
    private fun setBarColor() {
        val window = requireActivity().window
        val context = requireContext()
        window.statusBarColor = ContextCompat.getColor(context, COLOR_CHALLENGE_TOP)
        window.navigationBarColor = ContextCompat.getColor(context, COLOR_NAVI_BOTTOM)
    }
}