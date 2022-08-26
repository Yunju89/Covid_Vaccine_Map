package com.example.covid_vaccine_map.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServerAPI {

    companion object {
        private var retrofit: Retrofit? = null
        private var BASE_URL = "https://api.odcloud.kr/api/"

        fun getRetrofit(): Retrofit {

            val interceptor = Interceptor {
                with(it) {
                    val newRequest = request().newBuilder()
                        .addHeader(
                            "Authorization",
                            "bNmSjmL3NWL%2FmAmsQV0SyDT%2B8DCdZckhVg5%2FtSsmJHa47eBZBE%2BaFvCHYxeM1Dsz2FcgQ64elqYL3mr6GUyjOg%3D%3D"
                        )
                        .build()

                    proceed(newRequest)

                }
            }

            val myClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(myClient)
                    .build()
            }

            return retrofit!!
        }

    }
}