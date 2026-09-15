package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Branch
import com.example.data.model.Playlist
import com.example.data.model.PlaylistItem
import com.example.data.model.PriceProduct
import com.example.data.model.PriceSlideTheme
import com.example.ui.theme.SignageNavy
import com.example.ui.theme.SignageOnlineGreen

/**
 * TV Preview Enclosure
 * STRICT RULE: Always locks to 16:9 aspect-ratio to accurately simulate real digital signage TV.
 * Scales down with container width while never breaking its 16:9 aspect ratio.
 */
@Composable
fun TvPreviewContainer(
  title: String,
  subtitle: String = "1920x1080 FHD • TV Slayt Canlı Görünümü",
  isLive: Boolean = true,
  onOpenFullScreen: (() -> Unit)? = null,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("tv_preview_container"),
    colors = CardDefaults.cardColors(
      containerColor = Color(0xFF0B1120)
    ),
    shape = RoundedCornerShape(16.dp),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.horizontalGradient(
        listOf(Color(0xFF334155), Color(0xFF1E293B))
      )
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
  ) {
    Column(
      modifier = Modifier.padding(14.dp)
    ) {
      // TV Bezel Top Status Header
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Box(
            modifier = Modifier
              .size(10.dp)
              .clip(CircleShape)
              .background(if (isLive) SignageOnlineGreen else Color.Gray)
          )
          Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
          Surface(
            color = Color(0xFF1E293B),
            shape = RoundedCornerShape(6.dp)
          ) {
            Text(
              text = "16:9 TV EKRANI",
              color = Color(0xFF38BDF8),
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Text(
            text = subtitle,
            color = Color(0xFF94A3B8),
            fontSize = 11.sp
          )
          if (onOpenFullScreen != null) {
            IconButton(
              onClick = onOpenFullScreen,
              modifier = Modifier
                .size(36.dp)
                .testTag("tv_fullscreen_button")
            ) {
              Icon(
                imageVector = Icons.Default.Fullscreen,
                contentDescription = "Tam Ekran TV Önizleme",
                tint = Color(0xFF38BDF8)
              )
            }
          }
        }
      }

      // TV Outer Bezel Frame
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0xFF020617))
          .border(2.dp, Color(0xFF1E293B), RoundedCornerShape(8.dp))
          .padding(4.dp),
        contentAlignment = Alignment.Center
      ) {
        // STRICT 16:9 ASPECT RATIO SCREEN
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(6.dp))
            .background(Color.Black)
            .testTag("tv_screen_aspect_ratio_box"),
          contentAlignment = Alignment.Center
        ) {
          content()
        }
      }

      // TV Stand / Bottom Bar info
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Cast,
            contentDescription = null,
            tint = Color(0xFF64748B),
            modifier = Modifier.size(14.dp)
          )
          Text(
            text = "Digital Signage Player • 60 FPS Standart Yayın",
            color = Color(0xFF64748B),
            fontSize = 11.sp
          )
        }
        Text(
          text = "Otomatik 1920x1080 Ölçekleme",
          color = Color(0xFF64748B),
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium
        )
      }
    }
  }
}

/**
 * 16:9 Dynamic TV Menu & Price Slide Component
 */
@Composable
fun PriceSlide16x9Preview(
  branch: Branch?,
  products: List<PriceProduct>,
  theme: PriceSlideTheme,
  modifier: Modifier = Modifier
) {
  val backgroundBrush = when (theme) {
    PriceSlideTheme.DARK_LUXURY -> Brush.verticalGradient(
      colors = listOf(Color(0xFF0F172A), Color(0xFF020617))
    )
    PriceSlideTheme.BISTRO_WARM -> Brush.verticalGradient(
      colors = listOf(Color(0xFF2E1065), Color(0xFF1E1B4B))
    )
    PriceSlideTheme.NEON_PROMO -> Brush.verticalGradient(
      colors = listOf(Color(0xFF0C4A6E), Color(0xFF082F49))
    )
    PriceSlideTheme.MINIMAL_CORP -> Brush.verticalGradient(
      colors = listOf(Color(0xFF1E293B), Color(0xFF334155))
    )
  }

  val accentColor = when (theme) {
    PriceSlideTheme.DARK_LUXURY -> Color(0xFFF59E0B) // Gold
    PriceSlideTheme.BISTRO_WARM -> Color(0xFFFB923C) // Warm Amber
    PriceSlideTheme.NEON_PROMO -> Color(0xFF38BDF8) // Neon Cyan
    PriceSlideTheme.MINIMAL_CORP -> Color(0xFF34D399) // Emerald
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(backgroundBrush)
      .padding(12.dp)
  ) {
    Column(
      modifier = Modifier.fillMaxSize()
    ) {
      // Header of Digital Menu Slide
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Tv,
              contentDescription = null,
              tint = accentColor,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = branch?.name ?: "ŞUBE MENÜ & FİYAT LİSTESİ",
              color = Color.White,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
          Text(
            text = branch?.location ?: "Canlı Dijital Tabela Fiyat Ekranı",
            color = Color(0xFF94A3B8),
            fontSize = 9.sp,
            maxLines = 1
          )
        }

        Surface(
          color = accentColor.copy(alpha = 0.2f),
          shape = RoundedCornerShape(4.dp),
          border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(accentColor, accentColor)))
        ) {
          Text(
            text = "GÜNCEL FİYATLAR",
            color = accentColor,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      // Products Grid / List inside 16:9 Slide
      if (products.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "Bu şube için henüz fiyat ve ürün girilmedi.\nSoldaki formdan ürün ekleyin.",
            color = Color(0xFF94A3B8),
            fontSize = 11.sp,
            textAlign = TextAlign.Center
          )
        }
      } else {
        // Responsive 2-column list inside the 16:9 canvas
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          val half = (products.size + 1) / 2
          val col1 = products.take(half)
          val col2 = products.drop(half)

          Column(
            modifier = Modifier
              .weight(1f)
              .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            col1.take(4).forEach { product ->
              SlideProductItemRow(product = product, accentColor = accentColor)
            }
          }

          Column(
            modifier = Modifier
              .weight(1f)
              .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            col2.take(4).forEach { product ->
              SlideProductItemRow(product = product, accentColor = accentColor)
            }
          }
        }
      }

      // Footer
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Fiyatlarımıza KDV dahildir • Menü Kodu: ${branch?.devicePairingCode ?: "TR-0000"}",
          color = Color(0xFF64748B),
          fontSize = 8.sp
        )
        Text(
          text = "Otomatik 1920x1080 Dijital Tabela Çıktısı",
          color = accentColor.copy(alpha = 0.8f),
          fontSize = 8.sp,
          fontWeight = FontWeight.Medium
        )
      }
    }
  }
}

@Composable
private fun SlideProductItemRow(
  product: PriceProduct,
  accentColor: Color,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    color = if (product.isHighlighted) Color(0xFF1E293B) else Color(0x331E293B),
    shape = RoundedCornerShape(6.dp),
    border = if (product.isHighlighted) CardDefaults.outlinedCardBorder().copy(
      brush = Brush.horizontalGradient(listOf(accentColor, Color.Transparent))
    ) else null
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 4.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          if (product.isHighlighted) {
            Icon(
              imageVector = Icons.Default.Star,
              contentDescription = null,
              tint = accentColor,
              modifier = Modifier.size(10.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
          }
          Text(
            text = product.name,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
        if (product.description.isNotBlank()) {
          Text(
            text = product.description,
            color = Color(0xFF94A3B8),
            fontSize = 8.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }

      Spacer(modifier = Modifier.width(6.dp))

      // Price block with old price & discount badge
      Column(
        horizontalAlignment = Alignment.End
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          if (product.oldPrice != null && product.oldPrice > product.price) {
            Text(
              text = "₺${"%.0f".format(product.oldPrice)}",
              color = Color(0xFFEF4444),
              fontSize = 9.sp,
              textDecoration = TextDecoration.LineThrough
            )
            if (product.discountPercent != null) {
              Surface(
                color = Color(0xFFEF4444),
                shape = RoundedCornerShape(3.dp)
              ) {
                Text(
                  text = "-%${product.discountPercent}",
                  color = Color.White,
                  fontSize = 7.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 3.dp, vertical = 1.dp)
                )
              }
            }
          }
          Text(
            text = "₺${"%.0f".format(product.price)}",
            color = accentColor,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp
          )
        }
      }
    }
  }
}

/**
 * 16:9 Playlist Live Slide Preview Component
 */
@Composable
fun Playlist16x9Preview(
  playlist: Playlist?,
  currentIndex: Int,
  isPlaying: Boolean,
  onNext: () -> Unit,
  onPrevious: () -> Unit,
  modifier: Modifier = Modifier
) {
  val items = playlist?.items.orEmpty()
  val currentItem: PlaylistItem? = items.getOrNull(currentIndex)

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color.Black)
  ) {
    if (currentItem == null) {
      Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.Tv,
            contentDescription = null,
            tint = Color(0xFF475569),
            modifier = Modifier.size(40.dp)
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Bu playlistte henüz slayt yok",
            color = Color(0xFF94A3B8),
            fontSize = 12.sp
          )
        }
      }
    } else {
      AnimatedContent(
        targetState = currentItem,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "slide_transition"
      ) { target ->
        Box(modifier = Modifier.fillMaxSize()) {
          // Slide Image or Video Thumbnail
          AsyncImage(
            model = target.previewUrl,
            contentDescription = target.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )

          // Dark gradient overlay for signage typography
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                Brush.verticalGradient(
                  colors = listOf(
                    Color.Black.copy(alpha = 0.5f),
                    Color.Transparent,
                    Color.Black.copy(alpha = 0.8f)
                  )
                )
              )
          )

          // Slide Info Top Bar
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              color = Color.Black.copy(alpha = 0.7f),
              shape = RoundedCornerShape(6.dp)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
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
                  text = "CANLI YAYIN • Slayt ${currentIndex + 1}/${items.size}",
                  color = Color.White,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            Surface(
              color = Color(0xFF0284C7).copy(alpha = 0.85f),
              shape = RoundedCornerShape(6.dp)
            ) {
              Text(
                text = "${target.durationSeconds} sn",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          // Slide Title Bottom Bar
          Column(
            modifier = Modifier
              .align(Alignment.BottomStart)
              .fillMaxWidth()
              .padding(12.dp)
          ) {
            Text(
              text = target.title,
              color = Color.White,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 14.sp,
              maxLines = 2,
              overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(6.dp))
            // Progress Bar simulation
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color.White.copy(alpha = 0.3f))
            ) {
              Box(
                modifier = Modifier
                  .fillMaxWidth(0.65f)
                  .fillMaxHeight()
                  .background(Color(0xFF38BDF8))
              )
            }
          }
        }
      }

      // Quick Nav overlay buttons
      if (items.size > 1) {
        Row(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 6.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = onPrevious,
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(Color.Black.copy(alpha = 0.5f))
          ) {
            Icon(
              imageVector = Icons.Default.ChevronLeft,
              contentDescription = "Önceki Slayt",
              tint = Color.White
            )
          }

          IconButton(
            onClick = onNext,
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(Color.Black.copy(alpha = 0.5f))
          ) {
            Icon(
              imageVector = Icons.Default.ChevronRight,
              contentDescription = "Sonraki Slayt",
              tint = Color.White
            )
          }
        }
      }
    }
  }
}
