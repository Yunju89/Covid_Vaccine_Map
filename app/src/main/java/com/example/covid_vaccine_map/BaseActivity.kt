package com.example.covid_vaccine_map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.covid_vaccine_map.api.APIList
import com.example.covid_vaccine_map.api.ServerAPI

abstract class BaseActivity : AppCompatActivity() {

    lateinit var apiList: APIList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit = ServerAPI.getRetrofit()
        apiList = retrofit.create(APIList::class.java)

    }
}