package com.example.newsapp.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.Models.NewsResponse
import com.example.newsapp.Services.NewsApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchViewModel : ViewModel() {
    private val _NewsList : MutableLiveData<NewsResponse> = MutableLiveData(NewsResponse(listOf(), "ok", 0))
    val NewsList
        get() = _NewsList

    private val _Refreshing = MutableLiveData(false)
    val Refreshing
        get() = _Refreshing

    private val baseURL : String = "https://newsapi.org/v2/"

    private val service by lazy {
        Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build().create(
                NewsApi::class.java)
    }

    fun fetchNews(searchString : String){
        _Refreshing.value = true
        service.getNewsBySearch("16f644c1c2db4d979b223bebec1c1be1", searchString, "de", 100).enqueue(object : Callback<NewsResponse>{
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                println("Failed retrieving latest news with message ${t.message}")
                _Refreshing.value = false
            }

            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                val body = response.body()

                if(response.isSuccessful && body != null){
                    _NewsList.value = body
                }else{
                    println("Failed to retrieve latest news with message ${response.message()}")
                }
                _Refreshing.value = false
            }

        })
    }
}