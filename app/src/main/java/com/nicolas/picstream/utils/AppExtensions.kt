package com.nicolas.picstream.utils

import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Date.toCurrentDate(): String {

    val currentDate = this
    val dateFormat = SimpleDateFormat("dd/MM/yy hh:mm a", Locale.getDefault())

    return dateFormat.format(currentDate)

}