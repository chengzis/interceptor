package com.chengzis.interceptor.view.style

import android.content.Context
import android.content.res.Configuration

fun interface Style<T> {


    fun create(context: Context) : T

}


class StyleContext(
    val context: Context,
    val configuration: Configuration
)