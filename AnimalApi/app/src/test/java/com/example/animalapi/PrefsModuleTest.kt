package com.example.animalapi

import android.app.Application
import com.example.animalapi.DI.SharePrefsModule
import com.example.animalapi.Util.SharePreferencesHelper

class PrefsModuleTest(val mockPrefs: SharePreferencesHelper) : SharePrefsModule() {

    override fun provideSharePrefs(app: Application): SharePreferencesHelper {
        return mockPrefs
    }
}