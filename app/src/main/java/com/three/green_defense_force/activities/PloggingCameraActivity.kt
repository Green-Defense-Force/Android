package com.three.green_defense_force.activities

import android.app.Dialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.TorchState
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.three.green_defense_force.R
import com.three.green_defense_force.databinding.ActivityPloggingCameraBinding
import com.three.green_defense_force.extensions.setBackButton
import com.three.green_defense_force.extensions.setBarColor
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PloggingCameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPloggingCameraBinding

    private lateinit var previewView: PreviewView
    private lateinit var cameraBtn: Button
    private lateinit var flashBtn: Button
    private lateinit var cameraExecutor: ExecutorService

    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null
    private var isFlashOn = false

    // 상수
    private val COLOR_PLOGGING_TOP = R.color.plogging_top

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPloggingCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBarColor(COLOR_PLOGGING_TOP, COLOR_PLOGGING_TOP)
        setBackButton(R.id.backBtn)

        // 카메라 설정
        previewView = binding.previewView
        cameraBtn = binding.cameraBtn
        flashBtn = binding.flashBtn

        // 카메라 권한 확인 후, 카메라 프리뷰 바인딩
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            bindCameraPreview()
        } else {
            // 권한 없을 시 권한 요청
            requestCameraPermission()
        }

        imageCapture = ImageCapture.Builder().build()

        flashBtn.setOnClickListener {
            toggleFlash()
        }
        cameraBtn.setOnClickListener {
            takePicture()
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    /** 카메라 권한 요청하는 함수 */
    private fun requestCameraPermission() {
        TedPermission.create()
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    // 권한 허용 시 카메라 프리뷰 바인딩
                    bindCameraPreview()
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    // 권한 거부 시 토스트 메시지 출력
                    Toast.makeText(
                        this@PloggingCameraActivity,
                        "카메라 권한이 필요합니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
            .setDeniedMessage("카메라 권한을 허용해 주세요.")
            .setPermissions(android.Manifest.permission.CAMERA)
            .check()
    }

    /** 카메라 프리뷰 바인딩 함수*/
    private fun bindCameraPreview() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // 카메라 미리보기 설정
            val cameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            val preview = Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .build()

            preview.setSurfaceProvider(previewView.surfaceProvider)

            // ImageCapture를 라이프사이클에 바인딩
            imageCapture?.let {
                cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview, it)
                previewView.scaleType = PreviewView.ScaleType.FILL_CENTER
            }

            // 카메라 초기화를 통해 플래시 제어
            camera = cameraProvider.bindToLifecycle(
                this as LifecycleOwner,
                cameraSelector,
                preview,
                imageCapture!!
            )

            // 플래시 상태 업데이트
            updateFlash()

        }, ContextCompat.getMainExecutor(this))
    }

    /** 플래시 토클 함수 */
    private fun toggleFlash() {
        if (isFlashOn) {
            isFlashOn = false
            flashBtn.text = "조명켜기"
        } else {
            isFlashOn = true
            flashBtn.text = "조명끄기"
        }
        updateFlash()
    }

    /** 플래시 상태 업데이트 함수 */
    private fun updateFlash() {
        if (camera != null) {
            val cameraInfo = camera!!.cameraInfo
            val cameraControl = camera!!.cameraControl
            val torchState = cameraInfo.torchState.value

            if (torchState == TorchState.OFF && isFlashOn) {
                // 플래시 켜기
                cameraControl.enableTorch(true)
            } else if (torchState == TorchState.ON && !isFlashOn) {
                // 플래시 끄기
                cameraControl.enableTorch(false)
            }
        }
    }

    /** 사진 촬영 함수 */
    private fun takePicture() {
        val imageCapture = imageCapture ?: return
        val outputOptions =
            ImageCapture.OutputFileOptions.Builder(File.createTempFile("Green_", ".jpg")).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exception: ImageCaptureException) {
                    Log.e("사진 촬영 함수", "촬영 실패 : ${exception.message}", exception)
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Log.d("사진 촬영 함수", "촬영 성공 : ${outputFileResults.savedUri}")

                    // 촬영 성공 시 플래시 끄기
                    isFlashOn = false
                    updateFlash()
                    showGobackDialog()
                }
            }
        )
    }

    /** 플로깅으로 돌아가는 다이얼로그 띄우는 함수 */
    private fun showGobackDialog() {
        val dialog = createDialog(R.layout.dialog_plogging_goback)
        val gobackBtn = dialog.findViewById<Button>(R.id.ploggingGobackBtn)
        val retakeBtn = dialog.findViewById<Button>(R.id.ploggingRetakeBtn)
        val closeBtn = dialog.findViewById<Button>(R.id.ploggingCloseBtn)

        gobackBtn.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        retakeBtn.setOnClickListener { dialog.dismiss() }
        closeBtn.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    /** 다이얼로그 생성하는 함수 */
    private fun createDialog(layoutId: Int): Dialog {
        return Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(layoutId)
            setCancelable(false)
        }
    }
}
