package com.lazymohan.signaturepad

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawingCanvas(
  drawColor: MutableState<Color>,
  drawBrush: MutableState<Float>,
  usedColors: MutableState<MutableSet<Color>>,
  paths: MutableList<PathState>,
) {
  val currentPath = paths.last().path
  val movePath = remember {
    mutableStateOf<Offset?>(null)
  }

  Canvas(
    modifier = Modifier
      .wrapContentHeight()
      .fillMaxWidth()
      .pointerInteropFilter {
        when (it.action) {
          MotionEvent.ACTION_DOWN -> {
            currentPath.moveTo(it.x, it.y)
            usedColors.value.add(drawColor.value)
          }

          MotionEvent.ACTION_MOVE -> {
            movePath.value = Offset(it.x, it.y)
          }

          else -> {
            movePath.value = null
          }
        }
        true
      },
    onDraw = {
      movePath.value?.let {
        currentPath.lineTo(it.x, it.y)
        drawPath(
          path = currentPath,
          color = drawColor.value,
          style = Stroke(drawBrush.value)
        )
      }

      paths.forEach {
        drawPath(
          path = it.path,
          color = Color.Black,
          style = Stroke(it.stroke)
        )
      }
    }
  )
}