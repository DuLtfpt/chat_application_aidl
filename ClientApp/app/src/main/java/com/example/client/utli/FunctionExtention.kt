package com.example.client.utli

import android.annotation.SuppressLint
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import java.text.SimpleDateFormat
import java.util.Date

fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run {
        navigate(direction)
    }
}

@SuppressLint("SimpleDateFormat")
fun Long.formatTimestamp(): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm a")
    val currentDate = dateFormat.format(Date()).split(' ')[0]
    val date = dateFormat.format(Date(this)).split(' ')
    return if (date[0] == currentDate) {
        date[1].plus(' ').plus(date[2])
    } else {
        date[0]
    }
}

fun String.isValidId(): Boolean {
    val regex = Regex("\\b[1-9]\\d*\\b")
    return this.matches(regex)
}