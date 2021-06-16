package com.amrdeveloper.linkhub.util

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

fun AppCompatActivity.findNavHostController(hostId : Int) : NavController {
    val navHostFragment = supportFragmentManager.findFragmentById(hostId) as NavHostFragment
    return navHostFragment.navController
}