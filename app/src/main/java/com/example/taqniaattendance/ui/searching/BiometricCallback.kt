package com.example.taqniaattendance.ui.searching

import android.content.Context
import android.widget.Toast
import androidx.biometric.BiometricPrompt

class BiometricCallback(val context: Context) : BiometricPrompt.AuthenticationCallback() {
    override fun onAuthenticationError(errorCode: Int,
                                       errString: CharSequence) {
        super.onAuthenticationError(errorCode, errString)
        Toast.makeText(context,
            "Authentication error: $errString", Toast.LENGTH_SHORT)
            .show()
    }

    override fun onAuthenticationSucceeded(
        result: BiometricPrompt.AuthenticationResult) {
        super.onAuthenticationSucceeded(result)
        Toast.makeText(context,
            "Authentication succeeded!", Toast.LENGTH_SHORT)
            .show()
    }

    override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
        Toast.makeText(context, "Authentication failed",
            Toast.LENGTH_SHORT)
            .show()
    }
}