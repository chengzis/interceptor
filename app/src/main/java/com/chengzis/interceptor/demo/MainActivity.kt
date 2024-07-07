package com.chengzis.interceptor.demo

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.chengzis.interceptor.view.Alignment
import com.chengzis.interceptor.view.Arrangement
import com.chengzis.interceptor.view.BlueprintSize
import com.chengzis.interceptor.view.BlueprintView
import com.chengzis.interceptor.view.Row
import com.chengzis.interceptor.view.Text
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


        setContentView(
            BlueprintView(
                context = this,
                blueprint = Row(
                    context = this,
                    width = BlueprintSize.MatchParent,
                    height = BlueprintSize.MatchParent,
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    addChild(
                        blueprint = Text(
                            context = this@MainActivity,
                            width = BlueprintSize.WrapContent,
                            height = BlueprintSize.WrapContent,
                            text = "Hello World!1111",
                            Color.RED
                        ),
                        verticalAlignment = Alignment.Bottom
                    )
                    addChild(
                        blueprint = Text(
                            context = this@MainActivity,
                            width = BlueprintSize.WrapContent,
                            height = BlueprintSize.Pixel(200),
                            text = "Hello World!2222",
                            Color.GREEN
                        )
                    )
                    addChild(
                        blueprint = Text(
                            context = this@MainActivity,
                            width = BlueprintSize.WrapContent,
                            height = BlueprintSize.WrapContent,
                            text = "Hello World!3333333333333333",
                            Color.YELLOW
                        ),
                    )
                }
            )
        )
    }
}
