package com.developersmarket.instafitt.utils

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import com.developersmarket.instafitt.R
import com.google.android.gms.ads.AdRequest


class AdLoader {

    internal var isAdsShown = true

    var mInterstitialAd: com.google.android.gms.ads.InterstitialAd? = null




    fun loadfullAdAdmob(context: Context){

        mInterstitialAd = com.google.android.gms.ads.InterstitialAd(context)
        mInterstitialAd!!.adUnitId = context.getString(R.string.Admob_adUnitId)
        mInterstitialAd!!.loadAd(AdRequest.Builder().build())

    }
    companion object {
        var adLoader: AdLoader? = null
        val ads: AdLoader
            get() {
                if (adLoader == null) {
                    adLoader = AdLoader()
                }
                return adLoader!!
            }
    }

    fun showInterstitial(context: Context) {
        Log.e("FunCalled", "From ShowInterstitial mInterstitialAd!!.isLoaded"+mInterstitialAd!!.isLoaded)

        if (mInterstitialAd!!.isLoaded) {
            Log.e("Google in show", "From ShowInterstitial")
            mInterstitialAd!!.show()
        }
        loadfullAdAdmob(context)
    }
}
