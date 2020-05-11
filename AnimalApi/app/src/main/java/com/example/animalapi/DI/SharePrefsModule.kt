package com.example.animalapi.DI

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.example.animalapi.Util.SharePreferencesHelper
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
open class SharePrefsModule {

    @Singleton
    @Provides
    @TypeOfContext(CONTEXT_APP)
    open fun provideSharePrefs(app: Application): SharePreferencesHelper {
        return SharePreferencesHelper.invoke(app)
    }

    @Singleton
    @Provides
    @TypeOfContext(CONTEXT_ACTIVITY)
    fun provideActivitySharePrefs(activity: AppCompatActivity): SharePreferencesHelper {
        return SharePreferencesHelper.invoke(activity)
    }
}

//USE to make different ways to initialize the same object by
//different TYPE of Context
const val CONTEXT_APP = "APPLICATION_CONTEXT"
const val CONTEXT_ACTIVITY = "ACTIVITY_CONTEXT"

@Qualifier
annotation class TypeOfContext(val type: String)