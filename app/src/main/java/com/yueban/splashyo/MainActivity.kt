package com.yueban.splashyo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.yueban.splashyo.data.model.UnSplashKeys

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("MainActivity", UnSplashKeys.getInstance(this).toString())
    }
}
