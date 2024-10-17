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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nicolas.picstream.helper.NotificationHelper
import com.nicolas.picstream.navigation.Screen
import com.nicolas.picstream.ui.design.theme.PicStreamTheme
import com.nicolas.picstream.ui.home.HomeRoute
import com.nicolas.picstream.ui.option.OptionRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.KoinAndroidContext

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { _ -> }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        CoroutineScope(Dispatchers.Main).launch {
            NotificationHelper(this@MainActivity).scheduleDailyNotification()
        }

        setContent {
            PicStreamTheme {

                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    }) { innerPadding ->
                    NavigationHost(modifier = Modifier.padding(innerPadding), snackbarHostState)
                }
            }
        }
    }
}

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState
) {

    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            KoinAndroidContext {
                HomeRoute(
                    onConnectionLost = {
                        scope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = context.getString(R.string.title_network_info_error_view),
                                actionLabel = context.getString(R.string.label_snack_bar),
                                duration = SnackbarDuration.Indefinite
                            )
                            if (result == SnackbarResult.Dismissed) {
                                snackbarHostState.currentSnackbarData?.dismiss()
                            }
                        }
                    },
                    onConnectionReestablish = {
                        scope.launch { snackbarHostState.currentSnackbarData?.dismiss() }
                    },
                    onNavigateOptions = { navController.navigate(Screen.Options.route) },
                    onNavigateNotifications = {}
                )
            }
        }
        composable(route = Screen.Options.route) {
            OptionRoute(
                onBackScreen = { navController.popBackStack() },
            )
        }
        composable(route = Screen.Notifications.route) { }
    }
}