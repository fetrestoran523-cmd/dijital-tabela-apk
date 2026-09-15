package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.MediaItem
import com.example.data.model.MediaType

@Composable
fun MediaLibraryScreen(
  mediaItems: List<MediaItem>,
  selectedFilter: MediaType?,
  onFilterSelect: (MediaType?) -> Unit,
  onUploadMedia: (title: String, type: MediaType, duration: Int, url: String) -> Unit,
  onDeleteMedia: (mediaId: String) -> Unit,
  modifier: Modifier = Modifier
) {
  var showUploadDialog by remember { mutableStateOf(false) }

  BoxWithConstraints(modifier = modifier.fillMaxSize()) {
    val screenWidth = maxWidth

    // Responsive columns requirement: mobilde 1-2, tablette 2-3, masaüstünde 4+
    val columnCount = when {
      screenWidth >= 1200.dp -> 4
      screenWidth >= 850.dp -> 3
      screenWidth >= 550.dp -> 2
      else -> 1
    }

    val isMobile = screenWidth < 768.dp

    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(if (isMobile) 12.dp else 20.dp)
    ) {
      // Header and Upload Action
      if (isMobile) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Column {
            Text(
              text = "Medya Kütüphanesi",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onBackground
            )
            Text(
              text = "Firebase Storage: ${mediaItems.size} görsel & video içeriği",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Button(
            onClick = { showUploadDialog = true },
            modifier = Modifier
              .fillMaxWidth()
              .defaultMinSize(minHeight = 46.dp)
              .testTag("upload_media_button"),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
            shape = RoundedCornerShape(8.dp)
          ) {
            Icon(Icons.Default.CloudUpload, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Yeni Görsel / Video Yükle", fontWeight = FontWeight.Bold)
          }
        }
      } else {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Medya Kütüphanesi (Firebase Storage)",
              style = MaterialTheme.typography.headlineSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onBackground
            )
            Text(
              text = "1920x1080 dijital tabela uyumlu görsel ve video asset deposu",
              fontSize = 13.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Button(
            onClick = { showUploadDialog = true },
            modifier = Modifier
              .defaultMinSize(minHeight = 46.dp)
              .testTag("upload_media_button"),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
            shape = RoundedCornerShape(8.dp)
          ) {
            Icon(Icons.Default.CloudUpload, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Yeni Medya Yükle", fontWeight = FontWeight.Bold)
          }
        }
      }

      // Filter Chips Bar
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        FilterChip(
          selected = selectedFilter == null,
          onClick = { onFilterSelect(null) },
          label = { Text("Tüm İçerikler (${mediaItems.size})") },
          modifier = Modifier.defaultMinSize(minHeight = 44.dp)
        )

        FilterChip(
          selected = selectedFilter == MediaType.IMAGE,
          onClick = { onFilterSelect(MediaType.IMAGE) },
          label = { Text("Görseller") },
          leadingIcon = { Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(16.dp)) },
          modifier = Modifier.defaultMinSize(minHeight = 44.dp)
        )

        FilterChip(
          selected = selectedFilter == MediaType.VIDEO,
          onClick = { onFilterSelect(MediaType.VIDEO) },
          label = { Text("Videolar") },
          leadingIcon = { Icon(Icons.Default.Movie, contentDescription = null, modifier = Modifier.size(16.dp)) },
          modifier = Modifier.defaultMinSize(minHeight = 44.dp)
        )
      }

      // Media Grid (Responsive columns: 1-2 mobile, 2-3 tablet, 4+ desktop)
      LazyVerticalGrid(
        columns = GridCells.Fixed(columnCount),
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .testTag("media_library_grid"),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
      ) {
        items(mediaItems, key = { it.id }) { item ->
          MediaThumbnailCard(
            item = item,
            onDelete = { onDeleteMedia(item.id) }
          )
        }
      }
    }
  }

  // Upload Dialog
  if (showUploadDialog) {
    UploadMediaDialog(
      onDismiss = { showUploadDialog = false },
      onConfirm = { title, type, duration, url ->
        onUploadMedia(title, type, duration, url)
        showUploadDialog = false
      }
    )
  }
}

@Composable
fun MediaThumbnailCard(
  item: MediaItem,
  onDelete: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("media_card_${item.id}"),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    shape = RoundedCornerShape(12.dp),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE2E8F0))
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column {
      // 16:9 Thumbnail Image with badges
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(16f / 9f)
          .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
          .background(Color.Black)
      ) {
        AsyncImage(
          model = item.url,
          contentDescription = item.title,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )

        // Type badge (Image / Video)
        Surface(
          color = Color.Black.copy(alpha = 0.75f),
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier
            .align(Alignment.TopStart)
            .padding(8.dp)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(
              imageVector = if (item.type == MediaType.VIDEO) Icons.Default.Videocam else Icons.Default.Image,
              contentDescription = null,
              tint = if (item.type == MediaType.VIDEO) Color(0xFFF59E0B) else Color(0xFF38BDF8),
              modifier = Modifier.size(12.dp)
            )
            Text(
              text = if (item.type == MediaType.VIDEO) "VİDEO" else "GÖRSEL",
              color = Color.White,
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        // Duration Badge
        Surface(
          color = Color(0xFF0284C7).copy(alpha = 0.85f),
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(8.dp)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Timer,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(11.dp)
            )
            Text(
              text = "${item.durationSeconds} sn",
              color = Color.White,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      // Info Block
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp)
      ) {
        Text(
          text = item.title,
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          color = MaterialTheme.colorScheme.onSurface,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "${item.resolution} • ${item.fileSize}",
            fontSize = 11.sp,
            color = Color(0xFF64748B)
          )

          IconButton(
            onClick = onDelete,
            modifier = Modifier
              .size(44.dp)
              .testTag("delete_media_${item.id}")
          ) {
            Icon(
              imageVector = Icons.Default.DeleteOutline,
              contentDescription = "Medyayı Sil",
              tint = Color(0xFFEF4444)
            )
          }
        }
      }
    }
  }
}

@Composable
fun UploadMediaDialog(
  onDismiss: () -> Unit,
  onConfirm: (title: String, type: MediaType, duration: Int, url: String) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var type by remember { mutableStateOf(MediaType.IMAGE) }
  var durationText by remember { mutableStateOf("10") }
  var url by remember { mutableStateOf("") }

  val sampleImages = listOf(
    "Burger & Menü" to "https://images.unsplash.com/photo-1550547660-d9450f859349?w=800&q=80",
    "Kahve & Fırın" to "https://images.unsplash.com/photo-1509042239860-f550ce710b93?w=800&q=80",
    "Pizza Lansmanı" to "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=800&q=80",
    "Buzlu İçecekler" to "https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?w=800&q=80"
  )

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "Firebase Storage Medya Yükleme",
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
      )
    },
    text = {
      Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Medya Başlığı") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        // Type selection
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clickable { type = MediaType.IMAGE }
              .padding(end = 16.dp)
          ) {
            RadioButton(
              selected = type == MediaType.IMAGE,
              onClick = { type = MediaType.IMAGE }
            )
            Text("Görsel (JPG/PNG)", fontSize = 13.sp)
          }

          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { type = MediaType.VIDEO }
          ) {
            RadioButton(
              selected = type == MediaType.VIDEO,
              onClick = { type = MediaType.VIDEO }
            )
            Text("Video (MP4)", fontSize = 13.sp)
          }
        }

        OutlinedTextField(
          value = durationText,
          onValueChange = { durationText = it },
          label = { Text("Görüntülenme Süresi (Saniye)") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
          value = url,
          onValueChange = { url = it },
          label = { Text("Görsel / Video URL (veya aşağıdan seçin)") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        Text(
          text = "Hızlı Örnek Medyalar:",
          fontSize = 11.sp,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          sampleImages.forEach { (name, sampleUrl) ->
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = Color(0xFFF1F5F9),
              modifier = Modifier
                .clickable {
                  url = sampleUrl
                  if (title.isBlank()) title = name
                }
            ) {
              Text(
                text = name,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                color = Color(0xFF0369A1)
              )
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank()) {
            val dur = durationText.toIntOrNull() ?: 10
            onConfirm(title, type, dur, url)
          }
        },
        modifier = Modifier.defaultMinSize(minHeight = 44.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7))
      ) {
        Text("Depoya Yükle")
      }
    },
    dismissButton = {
      TextButton(
        onClick = onDismiss,
        modifier = Modifier.defaultMinSize(minHeight = 44.dp)
      ) {
        Text("İptal")
      }
    }
  )
}
