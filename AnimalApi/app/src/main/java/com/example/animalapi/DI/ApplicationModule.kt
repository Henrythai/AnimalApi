package com.example.animalapi.DI

import android.app.Application
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(val app: Application) {

    @Provides
    fun provideApplication(): Application = app

}