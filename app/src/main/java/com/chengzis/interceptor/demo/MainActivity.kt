package com.chengzis.interceptor.demo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    @Test1
    lateinit var r1: DefineRadioFavoriteRepository

    @Inject
    @Test2
    lateinit var r2: DefineRadioRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e("ddddd", "r1 = $r1")
        Log.e("ddddd", "r2 = $r2")


    }

}
