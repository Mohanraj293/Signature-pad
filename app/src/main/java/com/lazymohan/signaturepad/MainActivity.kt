package com.lazymohan.signaturepad

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lazymohan.signaturepad.ui.theme.SignaturePadTheme

class MainActivity : ComponentActivity() {
  @SuppressLint("MutableCollectionMutableState")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      SignaturePadTheme {
        SignatureScreenContent()
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignatureScreenContent() {
  val context = LocalContext.current

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Signature with Capture Image",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
          )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.LightGray),
        modifier = Modifier.background(Color.LightGray)
      )
    }
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(it),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Spacer(modifier = Modifier.padding(top = 20.dp))
      Button(onClick = {
        context.startActivity(SignatureActivity.getCallingIntent(context))
      }) {
        Text(text = "Signature")
      }
    }
  }
}