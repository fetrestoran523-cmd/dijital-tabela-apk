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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Branch
import com.example.ui.theme.SignageNavy
import com.example.ui.theme.SignageOfflineRed
import com.example.ui.theme.SignageOnlineGreen

@Composable
fun BranchesScreen(
  branches: List<Branch>,
  searchQuery: String,
  onlineOnly: Boolean,
  onSearchChange: (String) -> Unit,
  onOnlineOnlyChange: (Boolean) -> Unit,
  onToggleStatus: (branchId: String) -> Unit,
  onAddBranch: (name: String, location: String, pairingCode: String, screenCount: Int) -> Unit,
  onDeleteBranch: (branchId: String) -> Unit,
  modifier: Modifier = Modifier
) {
  var showAddDialog by remember { mutableStateOf(false) }

  BoxWithConstraints(modifier = modifier.fillMaxSize()) {
    val screenWidth = maxWidth

    // Responsive columns requirement:
    // mobilde 1-2 kolon, tablette 2-3, masaüstünde 4+
    val columnCount = when {
      screenWidth >= 1200.dp -> 4
      screenWidth >= 900.dp -> 3
      screenWidth >= 600.dp -> 2
      else -> 1
    }

    val isMobile = screenWidth < 768.dp

    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(if (isMobile) 12.dp else 20.dp)
    ) {
      // Header and Action Row (Stacks vertically on mobile)
      if (isMobile) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Şube Yönetimi",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
              )
              Text(
                text = "Firestore 'branches' koleksiyonu • Toplam ${branches.size} Şube",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Button(
            onClick = { showAddDialog = true },
            modifier = Modifier
              .fillMaxWidth()
              .defaultMinSize(minHeight = 46.dp)
              .testTag("add_branch_button"),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
            shape = RoundedCornerShape(8.dp)
          ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Yeni Şube & Ekran Eşle", fontWeight = FontWeight.Bold)
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
              text = "Şube Yönetimi (Branches)",
              style = MaterialTheme.typography.headlineSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onBackground
            )
            Text(
              text = "Firestore 'branches' koleksiyonu: şube adı, konum, cihaz eşleştirme kodu, online/offline durumu",
              fontSize = 13.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Button(
            onClick = { showAddDialog = true },
            modifier = Modifier
              .defaultMinSize(minHeight = 46.dp)
              .testTag("add_branch_button"),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
            shape = RoundedCornerShape(8.dp)
          ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Yeni Şube & Ekran Eşle", fontWeight = FontWeight.Bold)
          }
        }
      }

      // Filter and Search Toolbar (Stack on mobile, Row on desktop)
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        if (isMobile) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedTextField(
              value = searchQuery,
              onValueChange = onSearchChange,
              placeholder = { Text("Şube adı, adres veya cihaz kodu ara...") },
              leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
              singleLine = true,
              modifier = Modifier
                .fillMaxWidth()
                .testTag("branch_search_input")
            )

            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              FilterChip(
                selected = onlineOnly,
                onClick = { onOnlineOnlyChange(!onlineOnly) },
                label = { Text("Sadece Çevrimiçi Şubeler") },
                leadingIcon = {
                  Box(
                    modifier = Modifier
                      .size(8.dp)
                      .clip(CircleShape)
                      .background(SignageOnlineGreen)
                  )
                },
                modifier = Modifier.defaultMinSize(minHeight = 44.dp)
              )

              Text(
                text = "${branches.count { it.isOnline }} Aktif",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = SignageOnlineGreen
              )
            }
          }
        } else {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            OutlinedTextField(
              value = searchQuery,
              onValueChange = onSearchChange,
              placeholder = { Text("Şube adı, konum veya cihaz eşleştirme kodu ile ara...") },
              leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
              singleLine = true,
              modifier = Modifier
                .weight(1f)
                .testTag("branch_search_input")
            )

            FilterChip(
              selected = onlineOnly,
              onClick = { onOnlineOnlyChange(!onlineOnly) },
              label = { Text("Sadece Çevrimiçi Şubeler") },
              leadingIcon = {
                Box(
                  modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(SignageOnlineGreen)
                )
              },
              modifier = Modifier.defaultMinSize(minHeight = 44.dp)
            )

            Surface(
              color = Color(0xFFF1F5F9),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text(
                text = "Firestore Canlı Senkronize",
                color = Color(0xFF475569),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
              )
            }
          }
        }
      }

      // Responsive Grid of Branches
      if (branches.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              imageVector = Icons.Default.Tv,
              contentDescription = null,
              tint = Color(0xFF94A3B8),
              modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = "Kriterlere uygun şube bulunamadı.",
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontSize = 14.sp
            )
          }
        }
      } else {
        LazyVerticalGrid(
          columns = GridCells.Fixed(columnCount),
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .testTag("branches_grid"),
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp),
          contentPadding = PaddingValues(bottom = 16.dp)
        ) {
          items(branches, key = { it.id }) { branch ->
            BranchCardItem(
              branch = branch,
              onToggleStatus = { onToggleStatus(branch.id) },
              onDelete = { onDeleteBranch(branch.id) }
            )
          }
        }
      }
    }
  }

  // Add Branch Dialog
  if (showAddDialog) {
    AddBranchDialog(
      onDismiss = { showAddDialog = false },
      onConfirm = { name, location, code, screens ->
        onAddBranch(name, location, code, screens)
        showAddDialog = false
      }
    )
  }
}

@Composable
fun BranchCardItem(
  branch: Branch,
  onToggleStatus: () -> Unit,
  onDelete: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("branch_card_${branch.id}"),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    shape = RoundedCornerShape(14.dp),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.horizontalGradient(
        if (branch.isOnline)
          listOf(Color(0xFFE2E8F0), Color(0xFFE2E8F0))
        else
          listOf(Color(0xFFFCA5A5), Color(0xFFE2E8F0))
      )
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // Top status row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          color = if (branch.isOnline) Color(0xFFECFDF5) else Color(0xFFFEF2F2),
          shape = RoundedCornerShape(20.dp),
          border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(
              if (branch.isOnline) Color(0xFFA7F3D0) else Color(0xFFFECACA)
            )
          )
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
                .background(if (branch.isOnline) SignageOnlineGreen else SignageOfflineRed)
            )
            Text(
              text = if (branch.isOnline) "ÇEVRİMİÇİ" else "ÇEVRİMDIŞI",
              color = if (branch.isOnline) Color(0xFF047857) else Color(0xFFB91C1C),
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        // Switch button with at least 44x44 interactive area
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = if (branch.isOnline) "Açık" else "Kapalı",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.width(4.dp))
          Switch(
            checked = branch.isOnline,
            onCheckedChange = { onToggleStatus() },
            modifier = Modifier
              .defaultMinSize(minWidth = 44.dp, minHeight = 44.dp)
              .testTag("branch_toggle_${branch.id}"),
            colors = SwitchDefaults.colors(
              checkedThumbColor = Color.White,
              checkedTrackColor = SignageOnlineGreen,
              uncheckedThumbColor = Color.White,
              uncheckedTrackColor = Color(0xFF94A3B8)
            )
          )
        }
      }

      // Branch Name
      Text(
        text = branch.name,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      // Location
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Icon(
          imageVector = Icons.Default.LocationOn,
          contentDescription = null,
          tint = Color(0xFF64748B),
          modifier = Modifier.size(15.dp)
        )
        Text(
          text = branch.location,
          fontSize = 12.sp,
          color = Color(0xFF64748B),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }

      // Device Pairing Code Card
      Surface(
        color = Color(0xFFF8FAFC),
        shape = RoundedCornerShape(8.dp),
        border = CardDefaults.outlinedCardBorder().copy(
          brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE2E8F0))
        ),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Cihaz Eşleştirme Kodu",
              fontSize = 10.sp,
              color = Color(0xFF64748B)
            )
            Text(
              text = branch.devicePairingCode,
              fontWeight = FontWeight.ExtraBold,
              fontFamily = FontFamily.Monospace,
              fontSize = 14.sp,
              color = Color(0xFF0284C7)
            )
          }

          Surface(
            color = Color(0xFFE0F2FE),
            shape = RoundedCornerShape(6.dp)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Tv,
                contentDescription = null,
                tint = Color(0xFF0369A1),
                modifier = Modifier.size(12.dp)
              )
              Text(
                text = "${branch.screenCount} Ekran",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0369A1)
              )
            }
          }
        }
      }

      // Bottom Row with Ping & Delete Action
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = branch.lastPing,
          fontSize = 11.sp,
          color = Color(0xFF94A3B8)
        )

        IconButton(
          onClick = onDelete,
          modifier = Modifier
            .size(44.dp)
            .testTag("delete_branch_${branch.id}")
        ) {
          Icon(
            imageVector = Icons.Default.DeleteOutline,
            contentDescription = "Şubeyi Sil",
            tint = Color(0xFFEF4444)
          )
        }
      }
    }
  }
}

@Composable
fun AddBranchDialog(
  onDismiss: () -> Unit,
  onConfirm: (name: String, location: String, pairingCode: String, screenCount: Int) -> Unit
) {
  var name by remember { mutableStateOf("") }
  var location by remember { mutableStateOf("") }
  var pairingCode by remember { mutableStateOf("TR-${(1000..9999).random()}") }
  var screenCountText by remember { mutableStateOf("2") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "Yeni Şube ve Ekran Eşleştir",
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
          label = { Text("Şube Adı (Örn: Kadıköy Çarşı)") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
          value = location,
          onValueChange = { location = it },
          label = { Text("Konum / Şehir / Adres") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
          value = pairingCode,
          onValueChange = { pairingCode = it },
          label = { Text("Cihaz Eşleştirme Kodu") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
          value = screenCountText,
          onValueChange = { screenCountText = it },
          label = { Text("Tabela Ekran Sayısı") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (name.isNotBlank()) {
            val screens = screenCountText.toIntOrNull() ?: 1
            onConfirm(name, location, pairingCode, screens)
          }
        },
        modifier = Modifier.defaultMinSize(minHeight = 44.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7))
      ) {
        Text("Firestore'a Kaydet")
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
