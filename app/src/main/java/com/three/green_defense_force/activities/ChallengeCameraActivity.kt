package com.three.green_defense_force.activities

import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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
import com.three.green_defense_force.databinding.ActivityChallengeCameraBinding
import com.three.green_defense_force.extensions.setBackButton
import com.three.green_defense_force.extensions.setBarColor
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ChallengeCameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChallengeCameraBinding
    private lateinit var previewView: PreviewView
    private lateinit var cameraBtn: Button
    private lateinit var flashBtn: Button
    private lateinit var cameraExecutor: ExecutorService

    private lateinit var userId: String
    private lateinit var challengeId: String
    private lateinit var challengeTitle: String
    private lateinit var rewardType: String
    private var rewardCount: Int = 0

    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null
    private var isFlashOn = false

    // 상수
    private val COLOR_CHALLENGE_TOP = R.color.challenge_top

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChallengeCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBarColor(COLOR_CHALLENGE_TOP, COLOR_CHALLENGE_TOP)
        setBackButton(R.id.backBtn)

        // (1) 데이터 받아오기
        userId = intent.getStringExtra("USER_ID").toString()
        challengeId = intent.getStringExtra("CHALLENGE_ID").toString()
        challengeTitle = intent.getStringExtra("CHALLENGE_TITLE").toString()
        rewardType = intent.getStringExtra("REWARD_TYPE").toString()
        rewardCount = intent.getIntExtra("REWARD_COUNT", 0)

        with(binding) {
            challengeTitleText.text = challengeTitle
            rewardContentText.text = when (rewardType) {
                "ticket" -> "성공 시, 티켓 ${rewardCount}장"
                "coin" -> "성공 시, 코인 ${rewardCount}원"
                else -> ""
            }
        }

        // (2) 카메라 설정
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
            takePicture(userId, challengeId)
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
                        this@ChallengeCameraActivity,
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
    private fun takePicture(userId: String?, challengeId: String?) {
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

                    showImagePreviewDialog(outputFileResults.savedUri, userId, challengeId)
                }
            }
        )
    }

    /** 챌린지 다이얼로그 띄우는 함수 */
    private fun showImagePreviewDialog(imageUri: Uri?, userId: String?, challengeId: String?) {
        val dialog = createDialog(R.layout.dialog_challenge_retake)
        val challengePicture = dialog.findViewById<ImageView>(R.id.challengePicture)
        val okBtn = dialog.findViewById<Button>(R.id.challengeOkBtn)
        val retakeBtn = dialog.findViewById<Button>(R.id.challengeRetakeBtn)
        val closeBtn = dialog.findViewById<Button>(R.id.challengeCloseBtn)

        val bitmap = BitmapFactory.decodeFile(imageUri?.path)
        val rotatedBitmap = bitmap?.let { rotateImage(it, 90F) }

        challengePicture.setImageBitmap(rotatedBitmap)

        okBtn.setOnClickListener {
            sendImageToServer(userId, challengeId, imageUri)
            dialog.dismiss()
            showEndDialog()
        }
        retakeBtn.setOnClickListener { dialog.dismiss() }
        closeBtn.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    /** 이미지를 회전시키는 함수 */
    fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    /** 챌린지 종료 다이얼로그 띄우는 함수 */
    private fun showEndDialog() {
        val dialog = createDialog(R.layout.dialog_challenge_end)
        val endOkBtn = dialog.findViewById<Button>(R.id.challengeOkBtn)
        val endCloseBtn = dialog.findViewById<Button>(R.id.challengeCloseBtn)
        val challengeText = dialog.findViewById<TextView>(R.id.challengeText)

        if (rewardType == "ticket") {
            challengeText.text = "챌린지 성공으로\n티켓 ${rewardCount}장 받았어요!"
        } else if (rewardType == "coin") {
            challengeText.text = "챌린지 성공으로\n코인 ${rewardCount}원 받았어요!"
        }

        endOkBtn.setOnClickListener {
            dialog.dismiss()
            naviToChallengeFragment()
        }
        endCloseBtn.setOnClickListener {
            dialog.dismiss()
            naviToChallengeFragment()
        }

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

    /** 서버에 이미지 및 데이터 전송하는 함수*/
    private fun sendImageToServer(userId: String?, challengeId: String?, currentImgUri: Uri?) {

    }

    /** ChallengeFragment 이동하는 함수 */
    private fun naviToChallengeFragment() {

    }
}
