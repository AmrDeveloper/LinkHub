package com.amrdeveloper.linkhub

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.amrdeveloper.linkhub.util.ACTION_CREATE_FOLDER
import com.amrdeveloper.linkhub.util.ACTION_CREATE_LINK
import com.amrdeveloper.linkhub.util.findNavHostController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handleLinkHubIntent()
    }

    private fun handleLinkHubIntent() {
        when(intent.action) {
            Intent.ACTION_VIEW -> return
            Intent.ACTION_SEND -> {
                val sharedLink = intent.getStringExtra(Intent.EXTRA_TEXT)
                val bundle = bundleOf("shared_link" to sharedLink)
                findNavHostController(R.id.nav_host_fragment).navigate(R.id.linkFragment, bundle)
                return
            }
            Intent.ACTION_PROCESS_TEXT -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val sharedLink = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT).toString()
                    val bundle = bundleOf("shared_link" to sharedLink)
                    findNavHostController(R.id.nav_host_fragment).navigate(R.id.linkFragment, bundle)
                }
            }
            ACTION_CREATE_LINK -> {
                findNavHostController(R.id.nav_host_fragment).navigate(R.id.linkFragment)
            }
            ACTION_CREATE_FOLDER -> {
                findNavHostController(R.id.nav_host_fragment).navigate(R.id.folderFragment)
            }
        }
    }
}