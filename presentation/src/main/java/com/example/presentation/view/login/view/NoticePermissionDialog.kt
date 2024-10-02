package com.example.presentation.view.login.view

import android.Manifest
import android.os.Build
import androidx.core.app.ActivityCompat
import com.example.presentation.R
import com.example.presentation.base.BaseDialogFragment
import com.example.presentation.databinding.DialogNoticePermissionBinding
import com.example.presentation.databinding.DialogPermissionBinding

class NoticePermissionDialog: BaseDialogFragment<DialogNoticePermissionBinding>(R.layout.dialog_notice_permission) {
    private val REQUEST_NOTIFICATION_PERMISSION = 1

    override fun init() {
        setOnClickOk()
    }

    private fun setOnClickOk() {
        binding.btnPermissionDialogOk.setOnClickListener {
            this.dismiss()
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_NOTIFICATION_PERMISSION
                )
            }
            // OS 12 이하는 Notification Permission 추가하기
        }
    }
}