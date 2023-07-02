package com.chengzis.interceptor.demo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val repository = UserRepositoryImpl()
        Log.d("MainActivity", repository.findUserById("d").toString())
    }
}
