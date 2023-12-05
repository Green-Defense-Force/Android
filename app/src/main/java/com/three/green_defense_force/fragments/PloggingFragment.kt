package com.three.green_defense_force.fragments

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.three.green_defense_force.R
import com.three.green_defense_force.databinding.FragmentPloggingBinding

// OnMapReadyCallback 추가
class PloggingFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentPloggingBinding // 데이터 바인딩
    private lateinit var googleMap: GoogleMap // 지도
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient // 위치

    private var isPause = true
    private var pauseOffset: Long = 0

    private val COLOR_PLOGGING_TOP = R.color.plogging_top
    private val COLOR_NAVI_BOTTOM = R.color.navi_bottom

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBarColor()
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment의 데이터 바인딩
        binding = FragmentPloggingBinding.inflate(inflater, container, false)

        var rootView = binding.root
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        // 초기 상태 설정
        binding.timerContainer.visibility = View.INVISIBLE
        binding.endBtn.visibility = View.INVISIBLE
        binding.takeBtn.visibility = View.INVISIBLE

        // 다이얼로그 띄우기
        showStartDialog()

        // 플로깅 리스너 설정
        binding.ploggingBtn.setOnClickListener {
            if (isPause) {
                startPlogging()
            } else {
                pausePlogging()
            }
            isPause = !isPause
        }

        // 종료 버튼 리스너 설정
        binding.endBtn.setOnClickListener {
            endPlogging()
        }

        return rootView
    }

    /** 플로깅 시작, 재시작 함수*/
    private fun startPlogging() {
        binding.ploggingBtn.setBackgroundResource(R.drawable.plogging_stop_btn)
        binding.endBtn.visibility = View.INVISIBLE
        binding.takeBtn.visibility = View.INVISIBLE
        binding.timer.base = SystemClock.elapsedRealtime() - pauseOffset
        binding.timer.start()
    }

    /** 플로깅 일시 중지 함수 */
    private fun pausePlogging() {
        binding.ploggingBtn.setBackgroundResource(R.drawable.plogging_start_btn)
        binding.endBtn.visibility = View.VISIBLE
        binding.takeBtn.visibility = View.VISIBLE
        pauseOffset = SystemClock.elapsedRealtime() - binding.timer.base
        binding.timer.stop()
    }

    /** 플로깅 종료 함수 */
    private fun endPlogging() {
        binding.ploggingBtn.setBackgroundResource(R.drawable.plogging_start_btn)
        binding.endBtn.visibility = View.INVISIBLE
        binding.takeBtn.visibility = View.INVISIBLE
        binding.timer.base = SystemClock.elapsedRealtime()
        pauseOffset = 0
        binding.timer.stop()
        isPause = true
    }

    /** 플로깅 시작 다이얼로그 띄우는 함수 */
    private fun showStartDialog() {
        val dialog = createDialog(R.layout.dialog_plogging_start)
        val startBtn = dialog.findViewById<Button>(R.id.ploggingStartBtn)

        startBtn.setOnClickListener {
            refreshMap()

            binding.timerContainer.visibility = View.VISIBLE
            binding.ploggingTitleContainer.visibility = View.INVISIBLE
            binding.disableView.visibility = View.INVISIBLE

            dialog.dismiss()
        }

        dialog.show()
    }

    /** 지도 재렌더링 함수 */
    private fun refreshMap() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    googleMap.apply {
                        clear() // 기존 마커 제거
                        addMarker(MarkerOptions().position(latLng).title("현재 위치"))
                        moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
                    }
                }
            }
        }
    }

    /** 다이얼로그 생성하는 함수 */
    private fun createDialog(layoutId: Int): Dialog {
        // this → requireContext()
        return Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(layoutId)
            setCancelable(false)
        }
    }

    /** 지도 사용 가능 시 자동 호출하는 함수*/
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    map.apply {
                        addMarker(MarkerOptions().position(latLng).title("현재 위치"))
                        moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
                    }
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }
    }

    // 생명주기 관련 함수
    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    /** 상태바 및 하단바 색상 지정하는 함수 */
    private fun setBarColor() {
        val window = requireActivity().window
        val context = requireContext()
        window.statusBarColor = ContextCompat.getColor(context, COLOR_PLOGGING_TOP)
        window.navigationBarColor = ContextCompat.getColor(context, COLOR_NAVI_BOTTOM)
    }
}
