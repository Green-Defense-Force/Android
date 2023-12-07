package com.three.green_defense_force.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.three.green_defense_force.R
import com.three.green_defense_force.activities.GameActivity
import com.three.green_defense_force.activities.PloggingCameraActivity
import com.three.green_defense_force.databinding.FragmentPloggingBinding

// OnMapReadyCallback 추가
class PloggingFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentPloggingBinding // 데이터 바인딩
    private lateinit var googleMap: GoogleMap // 지도
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient // 위치

    private var isDialogShown = false
    private var isPause = true
    private var pauseOffset: Long = 0

    // 이동 경로
    private lateinit var polyline: Polyline
    private val pathPoints = mutableListOf<LatLng>()

    // 업데이트 핸들러
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 5000L
    private var userZoomLevel: Float = 15f

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
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        // 초기 상태 설정
        binding.timerContainer.visibility = View.INVISIBLE
        binding.endBtn.visibility = View.INVISIBLE
        binding.takeBtn.visibility = View.INVISIBLE

        // 다이얼로그 띄우기
        if (!isDialogShown) {
            isDialogShown = true
            showStartDialog()
        }

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

        // 촬영 버튼 리스너 설정
        binding.takeBtn.setOnClickListener {
            val intent = Intent(activity, PloggingCameraActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    /** 지도 사용 가능 시 자동 호출하는 함수*/
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        polyline = googleMap.addPolyline(
            PolylineOptions().color(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.polyline_color
                )
            ).width(10f)
        )

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
                        moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, userZoomLevel))
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

    /** 플로깅 시작, 재시작 함수*/
    private fun startPlogging() {
        binding.ploggingBtn.setBackgroundResource(R.drawable.plogging_stop_btn)
        binding.endBtn.visibility = View.INVISIBLE
        binding.takeBtn.visibility = View.INVISIBLE
        binding.timer.base = SystemClock.elapsedRealtime() - pauseOffset
        binding.timer.start()
        startUpdatingPolyline()
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

        pathPoints.clear()
        handler.removeCallbacksAndMessages(null)
        googleMap.clear()

        // 플로깅 재시작
        refreshMap()
        binding.timerContainer.visibility = View.INVISIBLE
        binding.ploggingTitleContainer.visibility = View.VISIBLE
        binding.disableView.visibility = View.VISIBLE
        showStartDialog()
    }

    /** 플로깅 시작 다이얼로그 띄우는 함수 */
    private fun showStartDialog() {
        val dialogFragment = PloggingStartDialogFragment()
        dialogFragment.setOnStartClickListener(object :
            PloggingStartDialogFragment.OnStartClickListener {
            override fun onStartClicked() {
                refreshMap()
                binding.timerContainer.visibility = View.VISIBLE
                binding.ploggingTitleContainer.visibility = View.INVISIBLE
                binding.disableView.visibility = View.INVISIBLE
                isDialogShown = false
            }
        })
        dialogFragment.show(childFragmentManager, "PloggingStartDialogFragment")
    }

    /** 지도 재렌더링 함수 */
    internal fun refreshMap() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    pathPoints.add(latLng)
                    updatePolyline()
                    googleMap.apply {
                        clear() // 기존 마커 제거
                        addMarker(MarkerOptions().position(latLng).title("현재 위치"))
                        addPolyline(
                            PolylineOptions().color(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.polyline_color
                                )
                            ).width(10f).addAll(pathPoints)
                        )
                    }
                    googleMap.setOnCameraMoveListener {
                        userZoomLevel = googleMap.cameraPosition.zoom
                    }
                }
            }
        }
    }

    /** 이동 경로 업데이트 시작하는 함숙 */
    private fun startUpdatingPolyline() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                // 일시 중지되지 않은 경우, 업데이트
                if (!isPause) {
                    refreshMap()
                    handler.postDelayed(this, updateInterval)
                }
            }
        }, updateInterval)
    }

    /** 이동 경로 업데이트 함수 */
    private fun updatePolyline() {
        polyline.points = pathPoints

        // 마지막 위치를 기준으로 카메라 시점 업데이트
        if (pathPoints.isNotEmpty()) {
            val lastLatLng = pathPoints.last()
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(getCameraPosition(lastLatLng)))
        }
    }

    /** 카메라 시점 반환하는 함수 */
    private fun getCameraPosition(latLng: LatLng): CameraPosition {
        val builder = CameraPosition.Builder()
        builder.target(latLng)
        builder.zoom(userZoomLevel)
        return builder.build()
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
        window.statusBarColor = ContextCompat.getColor(context, R.color.plogging_top)
        window.navigationBarColor = ContextCompat.getColor(context, R.color.navi_bottom)
    }
}