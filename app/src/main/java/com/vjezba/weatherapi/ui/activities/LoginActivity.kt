package com.vjezba.weatherapi.ui.activities

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.lifecycle.Observer
import com.vjezba.weatherapi.R
import com.vjezba.weatherapi.viewmodels.FingerprintViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    private val BIOMETRICS_REQUEST_CODE = 1
    private lateinit var biometricPrompt: BiometricPrompt

    private val viewModel: FingerprintViewModel by viewModels()

    var fingerPrintTitle = ""
    var fingerPrintDescription = ""
    var fingerPrintCancel = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if( requestCode == BIOMETRICS_REQUEST_CODE ) {
            Log.i("FingerprintAvailable", "New fingerprint added to mobile phone")
        }
    }

    override fun onStart() {
        super.onStart()

        checkIfThereIsAtLeastOneFingerprintAddedToMobilePhone()
        setCorrectTextForFingerPrintDialog()
        setOnClickListeners()
        addLiveData()

    }

    private fun setOnClickListeners() {
        biometricPrompt = viewModel.createBiometricPrompt()

        btnFingerPrint.setOnClickListener {
            val promptInfo = viewModel.createPromptInfo(fingerPrintTitle, fingerPrintDescription, fingerPrintCancel)
            if (viewModel.getBiometricManager().canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS) {
                biometricPrompt.authenticate(promptInfo)
            } else {
                detail_image.visibility = View.VISIBLE
            }
        }

        btnContinueToNextScreen.setOnClickListener {
            val intent = Intent(this, WeatherActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setCorrectTextForFingerPrintDialog() {
        fingerPrintTitle = resources.getString(R.string.biometric_fingerprint_title)
        fingerPrintDescription = resources.getString(R.string.biometric_fingerprint_description)
        fingerPrintCancel = resources.getString(R.string.biometric_fingerprint_cancel)
    }

    private fun checkIfThereIsAtLeastOneFingerprintAddedToMobilePhone() {
        val notOneFingerExistOnMobilePhone = viewModel.checkIfFingerprinteIsEnabled()
        if( notOneFingerExistOnMobilePhone == android.hardware.biometrics.BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED) {
            Log.i("FingerprintAvailable", "Da li ce uci za fingerprint Check if fingerprint is availabe: ${notOneFingerExistOnMobilePhone}")
            // Prompts the user to create credentials that your app accepts.
            val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                putExtra(
                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    BiometricManager.Authenticators.BIOMETRIC_STRONG
                )
            }
            startActivityForResult(enrollIntent, BIOMETRICS_REQUEST_CODE)
        }
        Log.i("FingerprintAvailable", "Check if there is at least one fingerprint: ${notOneFingerExistOnMobilePhone}")
    }

    private fun addLiveData() {
        viewModel.fingerPrintState.observe(this, Observer { state ->
            detail_image.visibility = View.VISIBLE
            btnContinueToNextScreen.visibility = View.VISIBLE
        })
    }

}