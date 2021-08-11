package com.amrdeveloper.linkhub.util

import android.app.Activity
import android.graphics.Color
import android.view.View
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
    snackBar.apply {
        setTextColor(ContextCompat.getColor(context, R.color.sky))
        setBackgroundTint(ContextCompat.getColor(context, R.color.dark_sky))
        show()
    }
}

fun Activity?.showSnackBar(message: Int, actionMessage: Int, actionCallback: (View?) -> Unit) {
    this ?: return
    val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
    snackBar.apply {
        setTextColor(ContextCompat.getColor(context, R.color.white))
        setActionTextColor(Color.YELLOW)
        setBackgroundTint(ContextCompat.getColor(context, R.color.dark_sky))
        setAction(actionMessage, actionCallback)
        show()
    }
}