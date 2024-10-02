package com.example.presentation.util

import android.content.Context
import android.content.Intent
import com.example.core_fcm.IntentProvider
import com.example.core_fcm.model.FcmData
import com.example.presentation.view.main.view.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MainIntentProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : IntentProvider {
    override fun getMainActivityIntent(fcmData: FcmData): Intent =
        MainActivity.createIntent(context, fcmData)
}