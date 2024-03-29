package com.henrique.electriccarapp.data

import com.henrique.electriccarapp.domain.Carro
import retrofit2.Call
import retrofit2.http.GET

interface CarsApi {

    @GET("cars.json")
    fun getAllCars() : Call<List<Carro>>

}