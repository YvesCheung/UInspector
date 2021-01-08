package com.huya.mobile.uinspector.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.huya.mobile.uinspector.demo.R
import com.huya.mobile.uinspector.ui.dialog.DemoDialog
import com.huya.mobile.uinspector.ui.dialog.DemoDialogFragment
import com.huya.mobile.uinspector.ui.optional.ImageLoaderActivity
import com.huya.mobile.uinspector.ui.optional.LottieActivity

class HomeViewModel : ViewModel() {

    val data: LiveData<List<HomeItem>> = MutableLiveData<List<HomeItem>>().apply {
        value = listOf(
            HomeItem("Show DialogFragment") { ctx, _ ->
                DemoDialogFragment().show(ctx.supportFragmentManager, "DemoDialogFragment")
            },
            HomeItem("Show Dialog") { ctx, _ ->
                DemoDialog(ctx).show()
            },
            HomeItem("Show PopupWindow") { ctx, view ->
                PopupWindow(ctx).apply {
                    @SuppressLint("InflateParams")
                    contentView =
                        LayoutInflater.from(ctx).inflate(R.layout.content_popupwindow, null)
                    contentView.setOnClickListener {
                        dismiss()
                    }
                }.showAsDropDown(view)
            },
            HomeItem("Open drawerLayout") { ctx, _ ->
                ctx.findViewById<DrawerLayout>(R.id.drawer_layout)?.open()
            },
            HomeItem("Show SnackBar") { _, view ->
                Snackbar.make(view, "You can inspect the SnackBar!", Snackbar.LENGTH_LONG).show()
            },
            HomeItem("Show Toast") { ctx, _ ->
                //toast window is on the top
                Toast.makeText(ctx, "Toast's window is hard to inspect!", Toast.LENGTH_LONG).show()
            },
            HomeItem("Add to WindowManager") { ctx, _ ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Settings.canDrawOverlays(ctx)) {
                        val wm = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager

                        @SuppressLint("InflateParams")
                        val view =
                            LayoutInflater.from(ctx).inflate(R.layout.content_popupwindow, null)
                        view.setOnClickListener {
                            wm.removeView(view)
                        }
                        wm.addView(view, WindowManager.LayoutParams().apply {
                            width = WRAP_CONTENT
                            height = WRAP_CONTENT
                        })
                    } else {
                        val intent = Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + ctx.packageName)
                        )
                        ctx.startActivity(intent)
                    }
                } else {
                    Toast.makeText(
                        ctx,
                        "Sdk version should not be less than M!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            HomeItem("Inspect Glide/Fresco") { ctx, _ ->
                ctx.startActivity(Intent(ctx, ImageLoaderActivity::class.java))
            },
            HomeItem("Inspect Lottie") { ctx, _ ->
                ctx.startActivity(Intent(ctx, LottieActivity::class.java))
            }
        )
    }
}

data class HomeItem(val title: String, val action: (FragmentActivity, View) -> Unit)