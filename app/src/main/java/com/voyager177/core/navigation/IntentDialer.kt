package com.voyager177.core.navigation

import android.app.Activity
import android.content.Intent
import android.net.Uri

class IntentDialer(private val activity: Activity) : Dialer {
    override fun open(phoneNumber: String) {
        val intent = Intent(
            Intent.ACTION_DIAL,
            Uri.fromParts(
                "tel",
                phoneNumber,
                null
            )
        )
        activity.startActivity(intent)
    }
}