package com.nicolas.picstream

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nicolas.picstream.helper.DailyScheduleNotificationHelper
import com.nicolas.picstream.navigation.Screen
import com.nicolas.picstream.ui.design.theme.PicStreamTheme
import com.nicolas.picstream.ui.home.HomeRoute
import com.nicolas.picstream.ui.notifications.NotificationRoute
import com.nicolas.picstream.ui.option.OptionRoute
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.core.component.KoinComponent

class MainActivity : ComponentActivity(), KoinComponent {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ -> }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        DailyScheduleNotificationHelper(this)
        setContent {
            PicStreamTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavigationHost(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
) {

    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            KoinAndroidContext {
                HomeRoute(
                    onNavigateOptions = { navController.navigate(Screen.Options.route) },
                    onNavigateNotifications = { navController.navigate(Screen.Notifications.route) }
                )
            }
        }
        composable(route = Screen.Options.route) {
            OptionRoute(
                onBackScreen = { navController.popBackStack() },
            )
        }
        composable(route = Screen.Notifications.route) {
            NotificationRoute(onBackScreen = { navController.popBackStack() })
        }
    }
}