package com.nicolas.picstream.components

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.nicolas.picstream.BuildConfig
import com.nicolas.picstream.R

@Composable
fun BannerAdMobView(
    modifier: Modifier = Modifier,
    key: String = stringResource(R.string.dummy_admob_banner_key),
) {

    val currentWidth = LocalConfiguration.current.screenWidthDp
    var adCompleted by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context: Context ->
                AdView(context).apply {
                    setAdSize(
                        AdSize.getLandscapeAnchoredAdaptiveBannerAdSize(
                            context,
                            currentWidth
                        )
                    )
                    adUnitId = if (BuildConfig.DEBUG) key else BuildConfig.BANNER_KEY
                    loadAd(AdRequest.Builder().build())
                    adListener = object : AdListener() {
                        override fun onAdLoaded() {
                            super.onAdLoaded()
                            adCompleted = true
                        }
                    }
                }
            }
        )
    }
}