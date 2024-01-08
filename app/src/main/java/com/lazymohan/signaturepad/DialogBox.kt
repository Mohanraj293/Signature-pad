package com.lazymohan.signaturepad

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.graphics.applyCanvas
import kotlin.math.roundToInt

@ExperimentalComposeUiApi
@Composable
fun SignatureDialog(
  isDialogOpen: MutableState<Boolean>,
  capturingViewBound: MutableState<Rect?>,
  drawColor: MutableState<Color>,
  drawBrush: MutableState<Float>,
  usedColors: MutableState<MutableSet<Color>>,
  paths: MutableState<MutableList<PathState>>,
  image: MutableState<Bitmap?>,
) {
  if (isDialogOpen.value) {

    val view = LocalView.current //get current view
    Column(
      modifier = Modifier
        .fillMaxWidth(0.9f)
        .height(200.dp)
        .padding(5.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(Color.White)
    ) {
      Column(
        modifier = Modifier
          .padding(horizontal = 20.dp)
          .weight(.6f)
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
          .weight(0.4f)
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
              val bounds = capturingViewBound.value ?: return@Button
              image.value = Bitmap.createBitmap(
                bounds.width.roundToInt(), bounds.height.roundToInt(),
                Bitmap.Config.ARGB_8888
              ).applyCanvas {
                translate(-bounds.left, -bounds.top)
                view.draw(this)
              }
              isDialogOpen.value = false
            },
            modifier = Modifier.weight(1f)
          ) {
            Text(text = "Submit")
          }
        }
      }
    }
  }
}















