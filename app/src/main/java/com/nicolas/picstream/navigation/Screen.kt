package com.nicolas.picstream.navigation

sealed class Screen (val route: String) {
    data object Home : Screen("home")
    data object Options : Screen("options")
    data object Notifications : Screen("notifications")
}