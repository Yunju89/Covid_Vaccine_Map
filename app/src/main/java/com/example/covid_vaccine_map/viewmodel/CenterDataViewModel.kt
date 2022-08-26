package com.example.covid_vaccine_map.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.covid_vaccine_map.R
import com.example.covid_vaccine_map.api.APIList
import com.example.covid_vaccine_map.api.ServerAPI
import com.example.covid_vaccine_map.model.CentersAPI
import com.example.covid_vaccine_map.model.MapData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CenterDataViewModel : ViewModel() {

    private val _data = MutableLiveData<Boolean>()
    val data: LiveData<Boolean>
        get() = _data

    init {
        _data.value = false
    }

    private val MAX_COUNT = 100
    private var PAGE_PER = 1


    fun getRequestCenterData(context: Context) {

        val serviceKey = context.getString(R.string.serviceKey)

        val apiList: APIList
        val retrofit = ServerAPI.getRetrofit()
        apiList = retrofit.create(APIList::class.java)


        apiList.getRequestCenter(serviceKey, PAGE_PER, 10, "JSON")
            .enqueue(object : Callback<CentersAPI> {
                override fun onResponse(call: Call<CentersAPI>, response: Response<CentersAPI>) {
                    if (response.code() == 200) {
                        response.body()?.let {
                            if (MapData.centersApi == null) {
                                MapData.centersApi = response.body()
                            } else {
                                MapData.centersApi?.data?.addAll(response.body()!!.data)
                            }

                        }

                        if ((MapData.centersApi?.getTotalCount())!! >= MAX_COUNT && MapData.centersApi?.getListSize() == MAX_COUNT) {
                            _data.value = true
                        } else {
                            PAGE_PER++
                            getRequestCenterData(context)
                        }
                    }

                }

                override fun onFailure(call: Call<CentersAPI>, t: Throwable) {
                    _data.value = false
                }

            })
    }


}