package com.huya.mobile.uinspector.ui.home

import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.huya.mobile.uinspector.demo.R
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
            HomeItem("Open drawerLayout") { ctx, _ ->
                ctx.findViewById<DrawerLayout>(R.id.drawer_layout)?.open()
            },
            HomeItem("Show SnackBar") { _, view ->
                Snackbar.make(view, "You can inspect the SnackBar!", Snackbar.LENGTH_LONG).show()
            }
        )
    }
}

data class HomeItem(val title: String, val action: (FragmentActivity, View) -> Unit)