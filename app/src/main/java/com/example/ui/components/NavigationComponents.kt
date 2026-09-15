package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PermMedia
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.PriceChange
import androidx.compose.material.icons.filled.ScreenSearchDesktop
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AdminUser
import com.example.data.model.SignageScreen
import com.example.data.model.ViewportMode
import com.example.ui.theme.SignageNavy
import com.example.ui.theme.SignageOnlineGreen

data class NavItem(
  val screen: SignageScreen,
  val icon: ImageVector,
  val description: String
)

val MainNavItems = listOf(
  NavItem(SignageScreen.BRANCHES, Icons.Default.Business, "Şubeler ve ekran eşleştirmeleri"),
  NavItem(SignageScreen.MEDIA, Icons.Default.PermMedia, "Görsel ve video içerik deposu"),
  NavItem(SignageScreen.PLAYLISTS, Icons.Default.PlayCircleOutline, "Slayt akışı ve şube atamaları"),
  NavItem(SignageScreen.PRICES, Icons.Default.PriceChange, "16:9 TV menü & fiyat slaytları")
)

/**
 * Top Admin Navigation Bar with Responsive Controls
 */
@Composable
fun AdminTopBar(
  currentUser: AdminUser?,
  currentScreen: SignageScreen,
  viewportMode: ViewportMode,
  onlineBranchCount: Int,
  totalBranchCount: Int,
  showHamburger: Boolean,
  onOpenDrawer: () -> Unit,
  onSelectViewportMode: (ViewportMode) -> Unit,
  onOpenTvPlayer: () -> Unit,
  onLogout: () -> Unit,
  modifier: Modifier = Modifier
) {
  var viewportDropdownExpanded by remember { mutableStateOf(false) }

  Surface(
    modifier = modifier
      .fillMaxWidth()
      .testTag("admin_top_bar"),
    color = SignageNavy,
    shadowElevation = 4.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 10.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      // Left: Hamburger + Logo & Title
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        if (showHamburger) {
          IconButton(
            onClick = onOpenDrawer,
            modifier = Modifier
              .size(44.dp)
              .testTag("hamburger_menu_button")
          ) {
            Icon(
              imageVector = Icons.Default.Menu,
              contentDescription = "Menüyü Aç",
              tint = Color.White
            )
          }
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFF0284C7)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Tv,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(22.dp)
            )
          }

          Column {
            Text(
              text = "TABELA YÖNETİMİ",
              color = Color.White,
              fontSize = 14.sp,
              fontWeight = FontWeight.Black,
              letterSpacing = 0.5.sp
            )
            Text(
              text = currentScreen.title,
              color = Color(0xFF38BDF8),
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }

        // Branch status badge (hide on very narrow screen if needed)
        Surface(
          color = Color(0xFF1E293B),
          shape = RoundedCornerShape(12.dp)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
          ) {
            Box(
              modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(SignageOnlineGreen)
            )
            Text(
              text = "$onlineBranchCount/$totalBranchCount Aktif",
              color = Color(0xFFE2E8F0),
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }

      // Right: Viewport Mode Switcher + Live TV Simulation Button + User Profile
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // Viewport Switcher Dropdown
        Box {
          Surface(
            modifier = Modifier
              .clickable { viewportDropdownExpanded = true }
              .defaultMinSize(minWidth = 44.dp, minHeight = 44.dp)
              .testTag("viewport_mode_switcher"),
            color = Color(0xFF1E293B),
            shape = RoundedCornerShape(8.dp),
            border = CardDefaults.outlinedCardBorder().copy(
              brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF334155))
            )
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Devices,
                contentDescription = "Ekran Genişliği Modu",
                tint = Color(0xFF38BDF8),
                modifier = Modifier.size(16.dp)
              )
              Text(
                text = viewportMode.title,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
          }

          DropdownMenu(
            expanded = viewportDropdownExpanded,
            onDismissRequest = { viewportDropdownExpanded = false }
          ) {
            ViewportMode.values().forEach { mode ->
              DropdownMenuItem(
                text = {
                  Column {
                    Text(text = mode.title, fontWeight = FontWeight.Bold)
                    Text(
                      text = mode.description,
                      fontSize = 10.sp,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                },
                onClick = {
                  onSelectViewportMode(mode)
                  viewportDropdownExpanded = false
                }
              )
            }
          }
        }

        // Live TV Player Button
        FilledTonalButton(
          onClick = onOpenTvPlayer,
          modifier = Modifier
            .defaultMinSize(minHeight = 44.dp)
            .testTag("live_tv_header_button"),
          colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = Color(0xFF0284C7),
            contentColor = Color.White
          ),
          shape = RoundedCornerShape(8.dp)
        ) {
          Icon(
            imageVector = Icons.Default.PlayCircleOutline,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "TV Önizle",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }

        // Logout
        IconButton(
          onClick = onLogout,
          modifier = Modifier
            .size(44.dp)
            .testTag("admin_logout_button")
        ) {
          Icon(
            imageVector = Icons.Default.ExitToApp,
            contentDescription = "Çıkış Yap",
            tint = Color(0xFF94A3B8)
          )
        }
      }
    }
  }
}

/**
 * Desktop & Tablet Persistent Sidebar
 */
@Composable
fun AdminSidebar(
  currentScreen: SignageScreen,
  currentUser: AdminUser?,
  branchCount: Int,
  mediaCount: Int,
  playlistCount: Int,
  onSelectScreen: (SignageScreen) -> Unit,
  onOpenTvPlayer: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier
      .width(260.dp)
      .fillMaxHeight()
      .testTag("admin_sidebar"),
    color = SignageNavy,
    shadowElevation = 8.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxHeight()
        .padding(16.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Column {
        // App Identity
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          modifier = Modifier.padding(bottom = 20.dp, top = 4.dp)
        ) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(Color(0xFF0284C7)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Tv,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(24.dp)
            )
          }

          Column {
            Text(
              text = "TABELA PANELİ",
              color = Color.White,
              fontSize = 15.sp,
              fontWeight = FontWeight.Black
            )
            Text(
              text = "Dijital Signage Sistemi",
              color = Color(0xFF94A3B8),
              fontSize = 11.sp
            )
          }
        }

        HorizontalDivider(color = Color(0xFF1E293B), modifier = Modifier.padding(bottom = 16.dp))

        Text(
          text = "YÖNETİM MENÜSÜ",
          color = Color(0xFF64748B),
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
        )

        // Navigation Items
        MainNavItems.forEach { item ->
          val isSelected = item.screen == currentScreen
          val countBadge = when (item.screen) {
            SignageScreen.BRANCHES -> "$branchCount"
            SignageScreen.MEDIA -> "$mediaCount"
            SignageScreen.PLAYLISTS -> "$playlistCount"
            SignageScreen.PRICES -> "16:9"
            else -> null
          }

          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 3.dp)
              .clip(RoundedCornerShape(10.dp))
              .clickable { onSelectScreen(item.screen) }
              .defaultMinSize(minHeight = 46.dp)
              .testTag("nav_item_${item.screen.name}"),
            color = if (isSelected) Color(0xFF0284C7) else Color.Transparent,
            shape = RoundedCornerShape(10.dp)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                Icon(
                  imageVector = item.icon,
                  contentDescription = item.screen.title,
                  tint = if (isSelected) Color.White else Color(0xFF94A3B8),
                  modifier = Modifier.size(20.dp)
                )
                Text(
                  text = item.screen.title,
                  color = if (isSelected) Color.White else Color(0xFFCBD5E1),
                  fontSize = 13.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
              }

              if (countBadge != null) {
                Surface(
                  color = if (isSelected) Color(0x33FFFFFF) else Color(0xFF1E293B),
                  shape = RoundedCornerShape(6.dp)
                ) {
                  Text(
                    text = countBadge,
                    color = if (isSelected) Color.White else Color(0xFF94A3B8),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }
            }
          }
        }
      }

      // Bottom Admin Info & TV Quick Launch Card
      Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Card(
          colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Tv,
                contentDescription = null,
                tint = Color(0xFF38BDF8),
                modifier = Modifier.size(18.dp)
              )
              Text(
                text = "16:9 TV Player",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Tüm şubeler için canlı tabela yayın simülasyonu.",
              color = Color(0xFF94A3B8),
              fontSize = 10.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
              onClick = onOpenTvPlayer,
              modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 44.dp),
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFF38BDF8)
              )
            ) {
              Text("Canlı TV Başlat", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }

        // Current User Profile Badge
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(Color(0xFF0EA5E9)),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = currentUser?.name?.take(1)?.uppercase() ?: "A",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }

          Column {
            Text(
              text = currentUser?.name ?: "Yönetici",
              color = Color.White,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            Text(
              text = currentUser?.email ?: "admin@tabela.corp",
              color = Color(0xFF64748B),
              fontSize = 10.sp,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }
      }
    }
  }
}
