package com.huya.mobile.uinspector.ui.activity

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.bumptech.glide.Glide
import com.huya.mobile.uinspector.demo.R
import kotlinx.android.synthetic.main.activity_demo.*

/**
 * @author YvesCheung
 * 2020/12/31
 */
class DemoActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        Glide.with(this)
            .load("https://raw.githubusercontent.com/YvesCheung/UInspector/master/art/uinspector.png")
            .error(ColorDrawable(Color.GREEN))
            .placeholder(ColorDrawable(Color.parseColor("#ff0099cc")))
            .into(glide_imageview)

        fresco_imageview.setImageURI(
            "https://raw.githubusercontent.com/YvesCheung/UInspector/master/art/uinspector.png"
        )
    }
}