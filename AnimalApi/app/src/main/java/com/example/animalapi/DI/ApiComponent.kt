package com.example.animalapi.DI

import com.example.animalapi.service.AnimalService
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(animalApiService: AnimalService)
}