package com.amrdeveloper.linkhub.util

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.amrdeveloper.linkhub.R
import com.google.android.material.snackbar.Snackbar

fun AppCompatActivity.findNavHostController(hostId: Int): NavController {
    val navHostFragment = supportFragmentManager.findFragmentById(hostId) as NavHostFragment
    return navHostFragment.navController
}

fun Activity?.showSnackBar(message: Int) {
    this ?: return
    val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
    snackBar.setTextColor(ContextCompat.getColor(this, R.color.sky))
    snackBar.show()
}