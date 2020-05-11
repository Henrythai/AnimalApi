package com.example.animalapi.service

import com.example.animalapi.DI.DaggerApiComponent
import com.example.animalapi.model.Animal
import com.example.animalapi.model.ApiKey
import io.reactivex.Single
import javax.inject.Inject

class AnimalService {


    @Inject
     lateinit var api: AnimalApi

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun getApiKey(): Single<ApiKey> = api.getApiKey()

    fun getAnimals(key: String): Single<List<Animal>> = api.getAnimals(key)
}