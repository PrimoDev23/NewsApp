package com.example.newsapp.Services

import com.example.newsapp.Models.NewsResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NewsApi{
    @GET("top-headlines")
    fun getTopHeadlines(@Header("X-Api-Key") apiKey : String, @Query("country") countryCode : String, @Query("pageSize") pageSize : Int) : Call<NewsResponse>
    @GET("top-headlines")
    fun getTopHeadlines(@Header("X-Api-Key") apiKey : String, @Query("country") countryCode : String, @Query("q") searchString : String, @Query("pageSize") pageSize : Int) : Call<NewsResponse>

    @GET("everything")
    fun getNewsBySearch(@Header("X-Api-Key") apiKey : String, @Query("q") searchString : String, @Query("language") language : String, @Query("pageSize") pageSize : Int) : Call<NewsResponse>
}