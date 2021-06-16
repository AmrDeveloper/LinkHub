package com.amrdeveloper.linkhub

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amrdeveloper.linkhub.util.ACTION_CREATE_FOLDER
import com.amrdeveloper.linkhub.util.ACTION_CREATE_LINK
import com.amrdeveloper.linkhub.util.findNavHostController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handleAppShortcut()
    }

    private fun handleAppShortcut() {
        when(intent.action) {
            Intent.ACTION_VIEW -> return
            ACTION_CREATE_LINK -> {
                findNavHostController(R.id.nav_host_fragment).navigate(R.id.linkFragment)
            }
            ACTION_CREATE_FOLDER -> {
                findNavHostController(R.id.nav_host_fragment).navigate(R.id.folderFragment)
            }
        }
    }
}