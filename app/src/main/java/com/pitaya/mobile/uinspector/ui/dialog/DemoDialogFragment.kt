package com.pitaya.mobile.uinspector.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.pitaya.mobile.uinspector.demo.R

/**
 * @author YvesCheung
 * 2020/12/30
 */
class DemoDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_demo_compose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val container: ComposeView = view.findViewById(R.id.compose_in_android_view)
        container.setContent {
            DemoDialogCompose()
        }
    }
}

@Composable
@Preview
fun DemoDialogCompose() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .background(color = Color.White)
            .padding(start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "It's a Compose dialog",
            fontSize = 20.sp,
            color = Color(
                ContextCompat.getColor(context, android.R.color.holo_blue_light)
            )
        )
        Text(
            text = "You can inspect a compose dialog",
            fontSize = 16.sp,
            color = Color(
                ContextCompat.getColor(context, android.R.color.darker_gray)
            )
        )
    }
}