package com.vjezba.weatherapi.ui.activities

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.vjezba.weatherapi.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity() {

    private val BIOMETRICS_REQUEST_CODE = 1
    private lateinit var biometricPrompt: BiometricPrompt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        biometricPrompt = createBiometricPrompt()
    }

    override fun onStart() {
        super.onStart()

        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Log.e("MY_APP_TAG", "No biometric features available on this device.")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG)
                }
                startActivityForResult(enrollIntent, BIOMETRICS_REQUEST_CODE)
            }
        }

        btnBiometricLogin.setOnClickListener {
            val promptInfo = createPromptInfo()
            if (biometricManager.canAuthenticate(BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS) {
                biometricPrompt.authenticate(promptInfo)
            } else {
                lifecycleScope.launch(Dispatchers.Main) {
                    showSnackbarSync(resources.getString(R.string.normal_login_not_fingerprint), true, rootElement)
                    delay(2000)
                    val intent = Intent(this@LoginActivity, WeatherActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun createBiometricPrompt(): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(this)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.d("BiometricError", "Authentication $errorCode :: $errString")
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    // loginWithPassword() // Because in this app, the negative button allows the user to enter an account password. This is completely optional and your app doesn’t have to do it.
                    createPromptInfo()
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.d("BiometricFailed", "Authentication failed for an unknown reason")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Log.d("BiometricSuccess", "Authentication was successful")
                // Proceed with viewing the private encrypted message.
                //showEncryptedMessage(result.cryptoObject)
            }
        }

        val biometricPrompt = BiometricPrompt(this, executor, callback)
        return biometricPrompt
    }

    private fun createPromptInfo(): BiometricPrompt.PromptInfo {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.action_settings))
            .setSubtitle(getString(R.string.app_name))
            .setDescription(getString(R.string.created_at))
            // Authenticate without requiring the user to press a "confirm"
            // button after satisfying the biometric check
            .setConfirmationRequired(false)
            .setNegativeButtonText(getString(R.string.created_by))
            .setAllowedAuthenticators(BIOMETRIC_STRONG )
            .build()
        return promptInfo
    }

}