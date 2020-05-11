package com.example.animalapi

import com.example.animalapi.DI.ApiModule
import com.example.animalapi.service.AnimalService

class ApiModuleTest(val mockService: AnimalService) : ApiModule() {

    override fun provideAnimalApiService(): AnimalService {
        return mockService
    }
}