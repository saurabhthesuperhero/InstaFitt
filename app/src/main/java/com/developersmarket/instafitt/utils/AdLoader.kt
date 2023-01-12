package com.developersmarket.instafitt.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import com.developersmarket.instafitt.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.doubleclick.PublisherAdRequest
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

object AdLoader {
    private lateinit var mInterstitialAd: InterstitialAd
    private lateinit var mRewardedInterstitialAd: RewardedAd
    private lateinit var applicationContext: Context
    private const val TAG = "AdLoader"
    fun initializeAds(applicationContext: Context) {
        this.applicationContext = applicationContext
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

    fun loadRewardedInterstitialAd(adUnitId: String) {
        mRewardedInterstitialAd = RewardedAd(applicationContext, adUnitId)
        val rewardedAdLoadCallback = object : RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            override fun onRewardedAdFailedToLoad(errorCode: Int) {
                // Ad failed to load.
            }
        }
        mRewardedInterstitialAd.loadAd(PublisherAdRequest.Builder().build(), rewardedAdLoadCallback)
    }


    fun showInterstitialAd() {
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        } else {
            mInterstitialAd.loadAd(AdRequest.Builder().build())
        }
    }

    interface RewardListener {
        fun onRewardEarned()
        fun onError()
    }

    fun showRewardedInterstitialAd(listener: RewardListener) {
        if (mRewardedInterstitialAd.isLoaded) {
            val activityContext = applicationContext as Activity
            mRewardedInterstitialAd.show(activityContext, object : RewardedAdCallback() {
                override fun onRewardedAdOpened() {
                    // The ad was opened.
                    Log.d(TAG, "onRewardedAdOpened() called")
                }

                override fun onRewardedAdClosed() {
                    // The ad was closed.
                    Log.d(TAG, "onRewardedAdClosed() called")
                }

                override fun onUserEarnedReward(reward: RewardItem) {
                    // The user earned a reward.
                    Log.d(TAG, "onUserEarnedReward() called with: reward = $reward")
                    listener.onRewardEarned()
                }

                override fun onRewardedAdFailedToShow(errorCode: Int) {
                    Log.d(TAG, "onRewardedAdFailedToShow() called with: errorCode = $errorCode")
                    // The ad failed to show.
                    listener.onError()

                }
            })
        } else {
            // Ad not loaded yet, you can retry loading the ad
            listener.onError()

            Log.d(TAG, "showRewardedInterstitialAd() Ad not loaded yet, you can retry loading the ad called with: listener = $listener")
            loadRewardedInterstitialAd(applicationContext.getString(R.string.Admob_adUnitId_Interstitial_Rewarded))
        }
    }

}

