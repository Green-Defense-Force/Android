package com.three.green_defense_force.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import com.three.green_defense_force.R
import com.three.green_defense_force.activities.GameActivity
import com.three.green_defense_force.activities.GameDetailActivity

class HomeFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBarColor()

        val gameBtn: Button = view.findViewById(R.id.gameBtn)
        gameBtn.setOnClickListener {
            val intent = Intent(activity, GameActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    /** 상태바 및 하단바 색상 지정하는 함수 */
    private fun setBarColor() {
        val window = requireActivity().window
        val context = requireContext()
        window.statusBarColor = ContextCompat.getColor(context, R.color.main_top)
        window.navigationBarColor = ContextCompat.getColor(context, R.color.navi_bottom)
    }
}