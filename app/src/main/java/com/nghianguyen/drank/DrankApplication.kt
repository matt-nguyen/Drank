package com.nghianguyen.drank

import android.app.Application
import android.util.Log
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DrankApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        val apiKey = BuildConfig.PLACES_API_KEY
        if (apiKey.isEmpty() || apiKey == "DEFAULT_API_KEY") {
            Log.e("ASDF", "Can't initialize Places. Invalid places api key")
        } else {
            Places.initializeWithNewPlacesApiEnabled(this, apiKey)
        }
    }
}