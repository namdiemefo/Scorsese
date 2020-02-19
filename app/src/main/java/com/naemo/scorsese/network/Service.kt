package com.naemo.scorsese.network

import com.naemo.scorsese.api.model.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("discover/movie")
    fun getTopRatedMovies(@Query("api_key") apiKey: String): Call<MovieResponse>
}