package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Branch
import com.example.data.model.MediaItem
import com.example.data.model.MediaType
import com.example.data.model.Playlist
import com.example.data.model.PlaylistItem
import com.example.ui.components.Playlist16x9Preview
import com.example.ui.components.TvPreviewContainer
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlaylistBuilderScreen(
  playlists: List<Playlist>,
  activePlaylist: Playlist?,
  branches: List<Branch>,
  allMedia: List<MediaItem>,
  onSelectPlaylist: (String) -> Unit,
  onCreatePlaylist: (name: String, branchIds: List<String>) -> Unit,
  onMoveItem: (index: Int, moveUp: Boolean) -> Unit,
  onUpdateDuration: (itemId: String, duration: Int) -> Unit,
  onRemoveItem: (itemId: String) -> Unit,
  onToggleBranchAssignment: (playlistId: String, branchId: String) -> Unit,
  onAddMediaItem: (MediaItem) -> Unit,
  onOpenFullScreenTv: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showCreateDialog by remember { mutableStateOf(false) }
  var showAddMediaDialog by remember { mutableStateOf(false) }
  var livePreviewIndex by remember { mutableStateOf(0) }

  // Auto slide interval for live TV preview inside builder
  LaunchedEffect(activePlaylist?.items, livePreviewIndex) {
    val items = activePlaylist?.items.orEmpty()
    if (items.isNotEmpty()) {
      val current = items.getOrNull(livePreviewIndex) ?: items[0]
      delay((current.durationSeconds.coerceAtLeast(3)) * 1000L)
      livePreviewIndex = (livePreviewIndex + 1) % items.size
    }
  }

  BoxWithConstraints(modifier = modifier.fillMaxSize()) {
    val screenWidth = maxWidth
    val isMobile = screenWidth < 850.dp

    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(if (isMobile) 12.dp else 20.dp)
    ) {
      // Top Header & Playlist Tabs
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Playlist Oluşturucu & Yayın Sıralama",
            style = if (isMobile) MaterialTheme.typography.titleLarge else MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = "Medya öğelerini sıralayın, süre atayın ve şubelere yayınlayın",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Button(
          onClick = { showCreateDialog = true },
          modifier = Modifier
            .defaultMinSize(minHeight = 44.dp)
            .testTag("create_playlist_button"),
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
          shape = RoundedCornerShape(8.dp)
        ) {
          Icon(Icons.Default.Add, contentDescription = null)
          if (!isMobile) {
            Spacer(modifier = Modifier.width(6.dp))
            Text("Yeni Playlist")
          }
        }
      }

      // Playlist Selection Tabs
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        playlists.forEach { pl ->
          val isSelected = pl.id == activePlaylist?.id
          FilterChip(
            selected = isSelected,
            onClick = {
              onSelectPlaylist(pl.id)
              livePreviewIndex = 0
            },
            label = { Text(pl.name, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
            leadingIcon = {
              Icon(
                imageVector = Icons.Default.Tv,
                contentDescription = null,
                tint = if (isSelected) Color(0xFF0284C7) else Color(0xFF94A3B8),
                modifier = Modifier.size(16.dp)
              )
            },
            modifier = Modifier.defaultMinSize(minHeight = 44.dp)
          )
        }
      }

      // Responsive Main Content Layout
      if (isMobile) {
        // MOBILE VERTICAL STACK (No horizontal scroll)
        Column(
          modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          // 16:9 TV PREVIEW (STRICT RATIO RULE)
          TvPreviewContainer(
            title = activePlaylist?.name ?: "Canlı TV Önizleme",
            subtitle = "Toplam Süre: ${activePlaylist?.totalDurationSeconds ?: 0} sn",
            onOpenFullScreen = onOpenFullScreenTv
          ) {
            Playlist16x9Preview(
              playlist = activePlaylist,
              currentIndex = livePreviewIndex,
              isPlaying = true,
              onNext = {
                val size = activePlaylist?.items?.size ?: 1
                livePreviewIndex = (livePreviewIndex + 1) % size
              },
              onPrevious = {
                val size = activePlaylist?.items?.size ?: 1
                val prev = livePreviewIndex - 1
                livePreviewIndex = if (prev < 0) size - 1 else prev
              }
            )
          }

          // Branch Assignment Card
          BranchAssignmentCard(
            playlist = activePlaylist,
            branches = branches,
            onToggleBranch = { bId ->
              activePlaylist?.let { onToggleBranchAssignment(it.id, bId) }
            }
          )

          // Items List with Reorder buttons & duration
          PlaylistItemEditorSection(
            activePlaylist = activePlaylist,
            onAddMediaClick = { showAddMediaDialog = true },
            onMoveItem = onMoveItem,
            onUpdateDuration = onUpdateDuration,
            onRemoveItem = onRemoveItem
          )
        }
      } else {
        // DESKTOP / TABLET TWO-COLUMN LAYOUT
        Row(
          modifier = Modifier
            .fillMaxSize()
            .weight(1f),
          horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          // Left Column: Items Editor & Branch assignment
          Column(
            modifier = Modifier
              .weight(1.1f)
              .fillMaxHeight()
              .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            BranchAssignmentCard(
              playlist = activePlaylist,
              branches = branches,
              onToggleBranch = { bId ->
                activePlaylist?.let { onToggleBranchAssignment(it.id, bId) }
              }
            )

            PlaylistItemEditorSection(
              activePlaylist = activePlaylist,
              onAddMediaClick = { showAddMediaDialog = true },
              onMoveItem = onMoveItem,
              onUpdateDuration = onUpdateDuration,
              onRemoveItem = onRemoveItem
            )
          }

          // Right Column: TV 16:9 PREVIEW FRAME (STRICT RATIO RULE)
          Column(
            modifier = Modifier
              .weight(0.9f)
              .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            TvPreviewContainer(
              title = activePlaylist?.name ?: "Canlı TV Önizleme",
              subtitle = "Slayt: ${livePreviewIndex + 1}/${activePlaylist?.items?.size ?: 0} • Toplam ${activePlaylist?.totalDurationSeconds ?: 0} sn",
              onOpenFullScreen = onOpenFullScreenTv
            ) {
              Playlist16x9Preview(
                playlist = activePlaylist,
                currentIndex = livePreviewIndex,
                isPlaying = true,
                onNext = {
                  val size = activePlaylist?.items?.size ?: 1
                  livePreviewIndex = (livePreviewIndex + 1) % size
                },
                onPrevious = {
                  val size = activePlaylist?.items?.size ?: 1
                  val prev = livePreviewIndex - 1
                  livePreviewIndex = if (prev < 0) size - 1 else prev
                }
              )
            }

            // Quick tips card
            Card(
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Text(
                  text = "Canlı Tabela Yayını Hakkında",
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "Bu playliste atanan şubelerin dijital tabela cihazları internete bağlandığında slaytlar sırayla ve belirlenen saniyelerde kesintisiz döngüde oynatılır.",
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }
    }
  }

  // Add Media to Playlist Dialog
  if (showAddMediaDialog) {
    AddMediaToPlaylistDialog(
      mediaList = allMedia,
      onDismiss = { showAddMediaDialog = false },
      onSelect = { media ->
        onAddMediaItem(media)
        showAddMediaDialog = false
      }
    )
  }

  // Create Playlist Dialog
  if (showCreateDialog) {
    CreatePlaylistDialog(
      branches = branches,
      onDismiss = { showCreateDialog = false },
      onConfirm = { name, branchIds ->
        onCreatePlaylist(name, branchIds)
        showCreateDialog = false
      }
    )
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BranchAssignmentCard(
  playlist: Playlist?,
  branches: List<Branch>,
  onToggleBranch: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    shape = RoundedCornerShape(12.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Business,
            contentDescription = null,
            tint = Color(0xFF0284C7),
            modifier = Modifier.size(18.dp)
          )
          Text(
            text = "Yayınlanacak Şubeler (Bir veya Birden Fazla)",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }

        Text(
          text = "${playlist?.assignedBranchIds?.size ?: 0} Şube Seçili",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF0284C7)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        branches.forEach { branch ->
          val isAssigned = playlist?.assignedBranchIds?.contains(branch.id) == true
          FilterChip(
            selected = isAssigned,
            onClick = { onToggleBranch(branch.id) },
            label = { Text(branch.name, fontSize = 12.sp) },
            leadingIcon = if (isAssigned) {
              { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp)) }
            } else null,
            modifier = Modifier.defaultMinSize(minHeight = 44.dp)
          )
        }
      }
    }
  }
}

@Composable
fun PlaylistItemEditorSection(
  activePlaylist: Playlist?,
  onAddMediaClick: () -> Unit,
  onMoveItem: (index: Int, moveUp: Boolean) -> Unit,
  onUpdateDuration: (itemId: String, duration: Int) -> Unit,
  onRemoveItem: (itemId: String) -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    shape = RoundedCornerShape(12.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Playlist Akış Sırası & Saniyeler",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
          Text(
            text = "Yukarı/aşağı butonları ile sıralayın, slayt süresini değiştirin",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Button(
          onClick = onAddMediaClick,
          modifier = Modifier.defaultMinSize(minHeight = 44.dp),
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
          shape = RoundedCornerShape(8.dp)
        ) {
          Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Slayt Ekle", fontSize = 12.sp)
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      val items = activePlaylist?.items.orEmpty()
      if (items.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "Bu playlistte henüz slayt yok. 'Slayt Ekle' butonuna basarak medya ekleyin.",
            fontSize = 12.sp,
            color = Color(0xFF94A3B8)
          )
        }
      } else {
        Column(
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items.forEachIndexed { index, item ->
            PlaylistItemRow(
              item = item,
              index = index,
              totalItems = items.size,
              onMoveUp = { onMoveItem(index, true) },
              onMoveDown = { onMoveItem(index, false) },
              onDurationChange = { newDur -> onUpdateDuration(item.id, newDur) },
              onRemove = { onRemoveItem(item.id) }
            )
          }
        }
      }
    }
  }
}

@Composable
fun PlaylistItemRow(
  item: PlaylistItem,
  index: Int,
  totalItems: Int,
  onMoveUp: () -> Unit,
  onMoveDown: () -> Unit,
  onDurationChange: (Int) -> Unit,
  onRemove: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(10.dp),
    color = Color(0xFFF8FAFC),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE2E8F0))
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      // Reorder buttons (Touch target at least 44x44px)
      Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        IconButton(
          onClick = onMoveUp,
          enabled = index > 0,
          modifier = Modifier
            .size(44.dp)
            .testTag("move_up_${item.id}")
        ) {
          Icon(
            imageVector = Icons.Default.ArrowUpward,
            contentDescription = "Yukarı Taşı",
            tint = if (index > 0) Color(0xFF0284C7) else Color(0xFFCBD5E1),
            modifier = Modifier.size(18.dp)
          )
        }

        IconButton(
          onClick = onMoveDown,
          enabled = index < totalItems - 1,
          modifier = Modifier
            .size(44.dp)
            .testTag("move_down_${item.id}")
        ) {
          Icon(
            imageVector = Icons.Default.ArrowDownward,
            contentDescription = "Aşağı Taşı",
            tint = if (index < totalItems - 1) Color(0xFF0284C7) else Color(0xFFCBD5E1),
            modifier = Modifier.size(18.dp)
          )
        }
      }

      // Order Badge
      Surface(
        color = Color(0xFF0F172A),
        shape = CircleShape,
        modifier = Modifier.size(24.dp)
      ) {
        Box(contentAlignment = Alignment.Center) {
          Text(
            text = "${index + 1}",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
          )
        }
      }

      Spacer(modifier = Modifier.width(8.dp))

      // Thumbnail
      Box(
        modifier = Modifier
          .size(width = 60.dp, height = 36.dp)
          .clip(RoundedCornerShape(4.dp))
          .background(Color.Black)
      ) {
        AsyncImage(
          model = item.previewUrl,
          contentDescription = item.title,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      // Title & Type
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = item.title,
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Text(
          text = if (item.type == MediaType.VIDEO) "Video Slaytı" else "Görsel Slaytı",
          fontSize = 10.sp,
          color = Color(0xFF64748B)
        )
      }

      // Duration Controls (- 5s +)
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        IconButton(
          onClick = { onDurationChange((item.durationSeconds - 2).coerceAtLeast(3)) },
          modifier = Modifier.size(44.dp)
        ) {
          Text("-", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = Color(0xFF0284C7))
        }

        Surface(
          color = Color(0xFFE0F2FE),
          shape = RoundedCornerShape(6.dp)
        ) {
          Text(
            text = "${item.durationSeconds} sn",
            color = Color(0xFF0369A1),
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }

        IconButton(
          onClick = { onDurationChange(item.durationSeconds + 2) },
          modifier = Modifier.size(44.dp)
        ) {
          Text("+", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = Color(0xFF0284C7))
        }
      }

      // Remove button
      IconButton(
        onClick = onRemove,
        modifier = Modifier
          .size(44.dp)
          .testTag("remove_item_${item.id}")
      ) {
        Icon(
          imageVector = Icons.Default.DeleteOutline,
          contentDescription = "Slaytı Kaldır",
          tint = Color(0xFFEF4444)
        )
      }
    }
  }
}

@Composable
fun AddMediaToPlaylistDialog(
  mediaList: List<MediaItem>,
  onDismiss: () -> Unit,
  onSelect: (MediaItem) -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "Kütüphaneden Slayt Ekle",
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
      )
    },
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .height(320.dp)
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        mediaList.forEach { media ->
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .clickable { onSelect(media) }
              .defaultMinSize(minHeight = 44.dp),
            color = Color(0xFFF1F5F9)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(48.dp, 30.dp)
                  .clip(RoundedCornerShape(4.dp))
                  .background(Color.Black)
              ) {
                AsyncImage(
                  model = media.url,
                  contentDescription = media.title,
                  contentScale = ContentScale.Crop,
                  modifier = Modifier.fillMaxSize()
                )
              }

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = media.title,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
                Text(
                  text = "${if (media.type == MediaType.VIDEO) "Video" else "Görsel"} • ${media.durationSeconds} sn",
                  fontSize = 10.sp,
                  color = Color(0xFF64748B)
                )
              }

              Text(
                text = "Ekle",
                color = Color(0xFF0284C7),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              )
            }
          }
        }
      }
    },
    confirmButton = {},
    dismissButton = {
      TextButton(onClick = onDismiss, modifier = Modifier.defaultMinSize(minHeight = 44.dp)) {
        Text("Kapat")
      }
    }
  )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreatePlaylistDialog(
  branches: List<Branch>,
  onDismiss: () -> Unit,
  onConfirm: (name: String, branchIds: List<String>) -> Unit
) {
  var name by remember { mutableStateOf("") }
  val selectedBranches = remember { mutableStateOf(branches.map { it.id }) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "Yeni Playlist Oluştur",
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
          value = name,
          onValueChange = { name = it },
          label = { Text("Playlist Adı (Örn: Hafta Sonu Kampanyası)") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        Text(
          text = "Varsayılan Şube Atamaları:",
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold
        )

        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          branches.forEach { b ->
            val isChecked = selectedBranches.value.contains(b.id)
            FilterChip(
              selected = isChecked,
              onClick = {
                val current = selectedBranches.value.toMutableList()
                if (current.contains(b.id)) current.remove(b.id) else current.add(b.id)
                selectedBranches.value = current
              },
              label = { Text(b.name, fontSize = 11.sp) },
              modifier = Modifier.defaultMinSize(minHeight = 44.dp)
            )
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (name.isNotBlank()) {
            onConfirm(name, selectedBranches.value)
          }
        },
        modifier = Modifier.defaultMinSize(minHeight = 44.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7))
      ) {
        Text("Oluştur")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss, modifier = Modifier.defaultMinSize(minHeight = 44.dp)) {
        Text("İptal")
      }
    }
  )
}
