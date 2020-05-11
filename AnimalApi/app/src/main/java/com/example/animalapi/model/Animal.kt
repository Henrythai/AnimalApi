package com.example.animalapi.model

import com.squareup.moshi.Json
import java.io.Serializable


data class ApiKey(
    @field:Json(name = "message")
    val message: String?,
    @field:Json(name = "key")
    val key: String?
) : Serializable

data class Animal(
    @field:Json(name = "name")
    val name: String?,
    @field:Json(name = "taxonomy")
    val taxonomy: Taxonomy?,
    @field:Json(name = "location")
    val location: String?,
    @field:Json(name = "speed")
    val speed: Speed?,
    @field:Json(name = "diet")
    val diet: String?,
    @field:Json(name = "lifespan")
    val lifeSpan: String?,
    @field:Json(name = "image")
    val imageUrl: String?
) : Serializable

data class Taxonomy(
    @field:Json(name = "kingdom")
    val kingdom: String?,
    @field:Json(name = "order")
    val order: String?,
    @field:Json(name = "family")
    val family: String?
) : Serializable

data class Speed(
    @field:Json(name = "metric")
    val metric: String?,
    @field:Json(name = "imperial")
    val imperial: String?
) : Serializable