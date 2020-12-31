package com.huya.mobile.uinspector.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.huya.mobile.uinspector.demo.R
import com.huya.mobile.uinspector.ui.activity.DemoActivity
import com.huya.mobile.uinspector.ui.dialog.DemoDialog
import com.huya.mobile.uinspector.ui.dialog.DemoDialogFragment

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
            HomeItem("Start Activity") { ctx, _ ->
                ctx.startActivity(Intent(ctx, DemoActivity::class.java))
            }
        )
    }
}

data class HomeItem(val title: String, val action: (FragmentActivity, View) -> Unit)