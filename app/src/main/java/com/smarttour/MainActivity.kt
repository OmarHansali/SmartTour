package com.smarttour

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import com.smarttour.ui.SmartTourApp
import com.smarttour.ui.SmartTourViewModel
import com.smarttour.ui.theme.SmartTourTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartTourTheme {
                val viewModel: SmartTourViewModel = hiltViewModel()
                SmartTourApp(viewModel = viewModel)
            }
        }
    }
}