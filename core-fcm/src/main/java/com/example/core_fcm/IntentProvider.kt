package com.example.core_fcm

import android.content.Intent
import com.example.core_fcm.model.FcmData

interface IntentProvider {
    fun getMainActivityIntent(fcmData: FcmData):Intent
}