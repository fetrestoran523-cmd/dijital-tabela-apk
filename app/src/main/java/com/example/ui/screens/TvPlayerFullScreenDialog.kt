package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.Playlist
import com.example.ui.components.Playlist16x9Preview
import com.example.ui.theme.SignageOnlineGreen

@Composable
fun TvPlayerFullScreenDialog(
  playlist: Playlist?,
  currentSlideIndex: Int,
  isPlaying: Boolean,
  onTogglePlay: () -> Unit,
  onNextSlide: () -> Unit,
  onPreviousSlide: () -> Unit,
  onClose: () -> Unit
) {
  Dialog(
    onDismissRequest = onClose,
    properties = DialogProperties(
      usePlatformDefaultWidth = false,
      dismissOnBackPress = true,
      dismissOnClickOutside = false
    )
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
        .testTag("tv_player_fullscreen_dialog"),
      contentAlignment = Alignment.Center
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
      ) {
        // Top TV OSD (On-screen display)
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Surface(
              color = Color(0xFF1E293B),
              shape = RoundedCornerShape(8.dp)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(SignageOnlineGreen)
                )
                Text(
                  text = "DİJİTAL TABELA TV CANLI YAYINI",
                  color = Color.White,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            Text(
              text = playlist?.name ?: "Playlist",
              color = Color(0xFF94A3B8),
              fontSize = 12.sp
            )
          }

          IconButton(
            onClick = onClose,
            modifier = Modifier
              .size(44.dp)
              .testTag("close_tv_player_button")
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "TV Modunu Kapat",
              tint = Color.White
            )
          }
        }

        // 16:9 LOCKED TV DISPLAY
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
          contentAlignment = Alignment.Center
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .aspectRatio(16f / 9f)
              .clip(RoundedCornerShape(8.dp))
              .background(Color.Black)
          ) {
            Playlist16x9Preview(
              playlist = playlist,
              currentIndex = currentSlideIndex,
              isPlaying = isPlaying,
              onNext = onNextSlide,
              onPrevious = onPreviousSlide
            )
          }
        }

        // Bottom Controls Bar (with min 44x44px touch targets)
        Surface(
          color = Color(0xFF0F172A).copy(alpha = 0.9f),
          shape = RoundedCornerShape(16.dp),
          border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(listOf(Color(0xFF334155), Color(0xFF1E293B)))
          )
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
          ) {
            IconButton(
              onClick = onPreviousSlide,
              modifier = Modifier.size(44.dp)
            ) {
              Icon(
                imageVector = Icons.Default.SkipPrevious,
                contentDescription = "Önceki",
                tint = Color.White
              )
            }

            IconButton(
              onClick = onTogglePlay,
              modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFF0284C7))
            ) {
              Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Durdur" else "Oynat",
                tint = Color.White
              )
            }

            IconButton(
              onClick = onNextSlide,
              modifier = Modifier.size(44.dp)
            ) {
              Icon(
                imageVector = Icons.Default.SkipNext,
                contentDescription = "Sonraki",
                tint = Color.White
              )
            }

            Text(
              text = "1920x1080 FHD • Otomatik Döngü",
              color = Color(0xFF94A3B8),
              fontSize = 11.sp,
              modifier = Modifier.padding(start = 8.dp)
            )
          }
        }
      }
    }
  }
}
