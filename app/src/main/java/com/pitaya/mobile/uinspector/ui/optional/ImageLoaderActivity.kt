package com.pitaya.mobile.uinspector.ui.optional

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.bumptech.glide.Glide
import com.facebook.drawee.view.SimpleDraweeView
import com.pitaya.mobile.uinspector.demo.R

/**
 * @author YvesCheung
 * 2020/12/31
 */
class ImageLoaderActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_loader)

        Glide.with(this)
            .load("https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/uinspector.png")
            .error(ColorDrawable(Color.GREEN))
            .placeholder(ColorDrawable(Color.parseColor("#ff0099cc")))
            .into(findViewById(R.id.glide_imageview))

        findViewById<SimpleDraweeView>(R.id.fresco_imageview).setImageURI(
            "https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/uinspector.png"
        )
    }
}