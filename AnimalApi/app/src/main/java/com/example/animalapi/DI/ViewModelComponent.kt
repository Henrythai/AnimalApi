package com.example.animalapi.DI

import com.example.animalapi.viewmodel.ListViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, SharePrefsModule::class, ApplicationModule::class])
interface ViewModelComponent {
    fun inject(listViewModel: ListViewModel)
}