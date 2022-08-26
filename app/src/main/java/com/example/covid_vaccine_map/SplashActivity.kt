package com.example.covid_vaccine_map

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.covid_vaccine_map.databinding.ActivitySplashBinding
import com.example.covid_vaccine_map.viewmodel.CenterDataViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity() {

    var callSuccess = false
    lateinit var centerDataViewModel: CenterDataViewModel

    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        centerDataViewModel = ViewModelProvider(this)[CenterDataViewModel::class.java]

        centerDataViewModel.getRequestCenterData(this)
        centerDataViewModel.data.observe(this, Observer {
            callSuccess = it
        })

        setProgress()
    }


    private fun setProgress() {

        val progressbar = binding.progressBar
        val currentProgress = 80

        ObjectAnimator.ofInt(progressbar, "progress", currentProgress)
            .setDuration(1600)
            .start()

        CoroutineScope(Dispatchers.Main).launch {
            delay(1600)

            var y = 0
            while (!callSuccess) {

                delay(400)
                y++

                if (y == 10 || callSuccess) {
                    break
                }
            }

            if (callSuccess) {
                for (i in 1..10) {
                    delay(40)
                    progressbar.incrementProgressBy(2)
                }
                goMain()
            } else {
                //앱종료처리?
                Toast.makeText(this@SplashActivity, "데이터 수신 실패 재 시작 하세요.", Toast.LENGTH_SHORT).show()
            }

        }
    }


    private fun goMain() {
        val myIntent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(myIntent)
        finish()
    }
}