package com.nicolas.picstream.components

import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.nicolas.picstream.R

fun showInterstitialAd(context: Context, onShowAd: (interstitialAd: InterstitialAd) -> Unit) {

    var interstitialAd : InterstitialAd?

    InterstitialAd.load(context,
        context.getString(R.string.dummy_admob_interstitial_key),
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