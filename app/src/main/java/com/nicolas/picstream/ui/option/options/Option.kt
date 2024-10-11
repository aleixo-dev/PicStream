package com.nicolas.picstream.ui.option.options

data class Option(
    val id : String = (0..10000).random().toString(),
    val title : String,
    val description : String,
    val enabled : Boolean
)
