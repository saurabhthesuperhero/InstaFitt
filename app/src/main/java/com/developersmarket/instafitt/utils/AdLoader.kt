package com.developersmarket.instafitt.utils

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import com.developersmarket.instafitt.R

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds

object AdLoader {
    private lateinit var mInterstitialAd: InterstitialAd
    private lateinit var applicationContext: Context

    fun initializeAds(applicationContext: Context) {
        this.applicationContext=applicationContext
        MobileAds.initialize(applicationContext) {
            // Use an App Id from AdMob
        }
    }

    fun loadBannerAd(adView: AdView, adUnitId: String) {
        adView.adUnitId = adUnitId
        adView.loadAd(AdRequest.Builder().build())
    }

    fun loadInterstitialAd(adUnitId: String) {
        mInterstitialAd = InterstitialAd(applicationContext)
        mInterstitialAd.adUnitId = adUnitId
        mInterstitialAd.loadAd(AdRequest.Builder().build())
    }

    fun showInterstitialAd() {
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        } else {
            mInterstitialAd.loadAd(AdRequest.Builder().build())
        }
    }
}
