package com.nicolas.picstream.presentation.option.options

import android.content.Context
import com.nicolas.picstream.R

class OptionHelper(context: Context) {

    val options = listOf(
        Option(
            title = context.getString(R.string.title_option_notification),
            description = context.getString(R.string.description_option_notification),
            enabled = false
        ),
    )
}