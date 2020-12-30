package com.huya.mobile.uinspector.ui.home

import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huya.mobile.uinspector.ui.dialog.DemoDialogFragment

class HomeViewModel : ViewModel() {

    val data: LiveData<List<HomeItem>> = MutableLiveData<List<HomeItem>>().apply {
        value = listOf(
            HomeItem("show DialogFragment") { ctx, _ ->
                DemoDialogFragment().show(ctx.supportFragmentManager, "DemoDialogFragment")
            }
        )
    }
}

data class HomeItem(val title: String, val action: (FragmentActivity, View) -> Unit)