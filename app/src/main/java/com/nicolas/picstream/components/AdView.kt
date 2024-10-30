package com.nicolas.picstream.components

import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.nicolas.picstream.BuildConfig
import com.nicolas.picstream.R

fun showInterstitialAd(context: Context, onShowAd: (interstitialAd: InterstitialAd) -> Unit) {

    var interstitialAd: InterstitialAd?
    val interstitialKey =
        if (BuildConfig.DEBUG)
            context.getString(R.string.dummy_admob_interstitial_key) else BuildConfig.INTERSTITIAL_KEY

    InterstitialAd.load(context,
        interstitialKey,
        AdRequest.Builder().build(),
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(error: LoadAdError) {
                interstitialAd = null
            }

            override fun onAdLoaded(ads: InterstitialAd) {
                interstitialAd = ads
                interstitialAd?.let { onShowAd(it) }
            }
        }
    )
}