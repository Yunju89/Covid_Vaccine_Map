package com.example.covid_vaccine_map.api

import com.example.covid_vaccine_map.model.CentersAPI
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIList {

    @GET("15077586/v1/centers")
    fun getRequestCenter(
        @Query("serviceKey") serviceKey: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int,
        @Query("returnType") returnType: String = "JSON"
    ):Call<CentersAPI>
}