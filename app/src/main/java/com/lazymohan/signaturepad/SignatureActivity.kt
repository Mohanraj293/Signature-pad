package com.lazymohan.signaturepad

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.graphics.applyCanvas
import com.lazymohan.signaturepad.ui.theme.SignaturePadTheme
import kotlin.math.roundToInt

class SignatureActivity : ComponentActivity() {
  companion object {
    fun getCallingIntent(
      context: Context,
    ) = Intent(context, SignatureActivity::class.java)
  }

  @OptIn(ExperimentalMaterial3Api::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      SignaturePadTheme {
        Scaffold(
          topBar = {
            TopAppBar(title = { Text(text = "Signature Area") })
          }
        ) {
          Column(
            modifier = Modifier
              .padding(it)
              .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            SignatureContent()
          }
        }
      }
    }
  }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun SignatureContent() {
  val paths = remember { mutableStateOf(mutableListOf<PathState>()) }
  val capturingViewBounds = remember { mutableStateOf<Rect?>(null) }
  val image = remember { mutableStateOf<Bitmap?>(null) }
  val drawColor = remember { mutableStateOf(Color.Black) }
  val drawBrush = remember { mutableStateOf(5f) }
  val usedColors = remember { mutableStateOf(mutableSetOf(Color.Black, Color.White, Color.Gray)) }
  paths.value.add(PathState(Path(), drawColor.value, drawBrush.value))

  val view = LocalView.current //get current view

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .fillMaxHeight()
      .border(2.dp, Color.Red)
  ) {
    Column(
      modifier = Modifier
        .onGloballyPositioned {
          capturingViewBounds.value = it.boundsInRoot()
        }
        .weight(.8f)
        .background(Color.Blue)
    ) {
      DrawingCanvas(
        drawColor = drawColor,
        drawBrush = drawBrush,
        usedColors = usedColors,
        paths = paths.value
      )
    }
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .weight(.2f)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Button(
          onClick = {
            paths.value = mutableListOf()
          },
          modifier = Modifier.weight(1f)
        ) {
          Text(text = "Clear")
        }
        Button(
          onClick = {
            val bounds = capturingViewBounds.value ?: return@Button
            image.value = Bitmap.createBitmap(
              bounds.width.roundToInt(), bounds.height.roundToInt(),
              ARGB_8888
            ).applyCanvas {
              translate(-bounds.left, -bounds.top)
              view.draw(this)
            }
          },
          modifier = Modifier.weight(1f)
        ) {
          Text(text = "Submit")
        }
      }
    }
  }
}