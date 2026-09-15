package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.SignageScreen
import com.example.data.model.ViewportMode
import com.example.ui.components.AdminSidebar
import com.example.ui.components.AdminTopBar
import com.example.ui.screens.BranchesScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MediaLibraryScreen
import com.example.ui.screens.PlaylistBuilderScreen
import com.example.ui.screens.PriceManagementScreen
import com.example.ui.screens.TvPlayerFullScreenDialog
import com.example.ui.viewmodel.SignageViewModel
import kotlinx.coroutines.launch

@Composable
fun SignageApp(
  viewModel: SignageViewModel = viewModel()
) {
  val currentUser by viewModel.currentUser.collectAsState()

  if (currentUser == null) {
    LoginScreen(
      onLoginSuccess = { email, name ->
        viewModel.login(email, name)
      }
    )
    return
  }

  val currentScreen by viewModel.currentScreen.collectAsState()
  val viewportMode by viewModel.viewportMode.collectAsState()
  val branches by viewModel.filteredBranches.collectAsState()
  val rawBranches by viewModel.rawBranches.collectAsState()
  val mediaItems by viewModel.filteredMediaItems.collectAsState()
  val rawMediaItems by viewModel.rawMediaItems.collectAsState()
  val playlists by viewModel.playlists.collectAsState()
  val activePlaylist by viewModel.activePlaylist.collectAsState()
  val branchProducts by viewModel.currentBranchProducts.collectAsState()
  val slideTheme by viewModel.slideTheme.collectAsState()
  val selectedBranchId by viewModel.selectedBranchId.collectAsState()
  val isTvPlayerOpen by viewModel.isTvPlayerOpen.collectAsState()
  val tvPlayerIndex by viewModel.tvPlayerSlideIndex.collectAsState()
  val isTvPlaying by viewModel.isTvPlaying.collectAsState()

  val branchSearchQuery by viewModel.branchSearchQuery.collectAsState()
  val branchOnlineOnly by viewModel.branchFilterOnlineOnly.collectAsState()
  val mediaFilterType by viewModel.mediaTypeFilter.collectAsState()

  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val scope = rememberCoroutineScope()

  BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
    val naturalWidth = maxWidth

    // Effective width considering user-selected viewport mode
    val effectiveWidth = when (viewportMode) {
      ViewportMode.AUTO -> naturalWidth
      ViewportMode.MOBILE -> 390.dp
      ViewportMode.TABLET -> 820.dp
      ViewportMode.DESKTOP -> 1440.dp
    }

    // Whether this view behaves as mobile (hamburger sidebar) or desktop (persistent sidebar)
    val isMobileView = effectiveWidth < 768.dp

    // Drawer Container for Mobile Hamburger Menu
    ModalNavigationDrawer(
      drawerState = drawerState,
      gesturesEnabled = isMobileView,
      drawerContent = {
        ModalDrawerSheet(
          modifier = Modifier.width(280.dp),
          drawerContainerColor = Color(0xFF0F172A)
        ) {
          AdminSidebar(
            currentScreen = currentScreen,
            currentUser = currentUser,
            branchCount = rawBranches.size,
            mediaCount = rawMediaItems.size,
            playlistCount = playlists.size,
            onSelectScreen = { screen ->
              viewModel.navigateTo(screen)
              scope.launch { drawerState.close() }
            },
            onOpenTvPlayer = {
              viewModel.openTvPlayer()
              scope.launch { drawerState.close() }
            }
          )
        }
      }
    ) {
      // Background viewport simulator container
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(if (viewportMode != ViewportMode.AUTO) Color(0xFF020617) else MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
      ) {
        // Main App Frame (constrained when viewport simulation is active)
        Box(
          modifier = Modifier
            .then(
              if (viewportMode != ViewportMode.AUTO) {
                Modifier
                  .width(effectiveWidth)
                  .fillMaxHeight()
                  .clip(RoundedCornerShape(12.dp))
                  .border(2.dp, Color(0xFF334155), RoundedCornerShape(12.dp))
              } else {
                Modifier.fillMaxSize()
              }
            )
        ) {
          Scaffold(
            topBar = {
              AdminTopBar(
                currentUser = currentUser,
                currentScreen = currentScreen,
                viewportMode = viewportMode,
                onlineBranchCount = rawBranches.count { it.isOnline },
                totalBranchCount = rawBranches.size,
                showHamburger = isMobileView,
                onOpenDrawer = { scope.launch { drawerState.open() } },
                onSelectViewportMode = { viewModel.setViewportMode(it) },
                onOpenTvPlayer = { viewModel.openTvPlayer() },
                onLogout = { viewModel.logout() }
              )
            }
          ) { innerPadding ->
            Row(
              modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
            ) {
              // Persistent Sidebar on Desktop & Tablet width
              if (!isMobileView) {
                AdminSidebar(
                  currentScreen = currentScreen,
                  currentUser = currentUser,
                  branchCount = rawBranches.size,
                  mediaCount = rawMediaItems.size,
                  playlistCount = playlists.size,
                  onSelectScreen = { viewModel.navigateTo(it) },
                  onOpenTvPlayer = { viewModel.openTvPlayer() }
                )
              }

              // Main Dynamic Screen Content Area
              Box(
                modifier = Modifier
                  .weight(1f)
                  .fillMaxHeight()
                  .background(MaterialTheme.colorScheme.background)
              ) {
                when (currentScreen) {
                  SignageScreen.BRANCHES -> {
                    BranchesScreen(
                      branches = branches,
                      searchQuery = branchSearchQuery,
                      onlineOnly = branchOnlineOnly,
                      onSearchChange = { viewModel.setBranchSearch(it) },
                      onOnlineOnlyChange = { viewModel.setOnlineFilter(it) },
                      onToggleStatus = { viewModel.toggleBranchStatus(it) },
                      onAddBranch = { name, loc, code, screens ->
                        viewModel.addBranch(name, loc, code, screens)
                      },
                      onDeleteBranch = { viewModel.deleteBranch(it) }
                    )
                  }

                  SignageScreen.MEDIA -> {
                    MediaLibraryScreen(
                      mediaItems = mediaItems,
                      selectedFilter = mediaFilterType,
                      onFilterSelect = { viewModel.setMediaTypeFilter(it) },
                      onUploadMedia = { title, type, dur, url ->
                        viewModel.uploadMedia(title, type, dur, url)
                      },
                      onDeleteMedia = { viewModel.deleteMedia(it) }
                    )
                  }

                  SignageScreen.PLAYLISTS -> {
                    PlaylistBuilderScreen(
                      playlists = playlists,
                      activePlaylist = activePlaylist,
                      branches = rawBranches,
                      allMedia = rawMediaItems,
                      onSelectPlaylist = { viewModel.selectPlaylist(it) },
                      onCreatePlaylist = { name, bIds -> viewModel.createPlaylist(name, bIds) },
                      onMoveItem = { index, up -> viewModel.movePlaylistItem(index, up) },
                      onUpdateDuration = { id, dur -> viewModel.updateItemDuration(id, dur) },
                      onRemoveItem = { viewModel.removePlaylistItem(it) },
                      onToggleBranchAssignment = { plId, bId ->
                        viewModel.toggleBranchAssignment(plId, bId)
                      },
                      onAddMediaItem = { viewModel.addMediaToActivePlaylist(it) },
                      onOpenFullScreenTv = { viewModel.openTvPlayer() }
                    )
                  }

                  SignageScreen.PRICES -> {
                    PriceManagementScreen(
                      branches = rawBranches,
                      selectedBranchId = selectedBranchId,
                      products = branchProducts,
                      activeTheme = slideTheme,
                      onSelectBranch = { viewModel.selectBranch(it) },
                      onSelectTheme = { viewModel.setSlideTheme(it) },
                      onAddProduct = { name, cat, price, old, desc, high ->
                        viewModel.addProduct(name, cat, price, old, desc, high)
                      },
                      onDeleteProduct = { viewModel.deleteProduct(it) },
                      onOpenFullScreenTv = { viewModel.openTvPlayer() }
                    )
                  }

                  SignageScreen.SETTINGS -> {
                    // Quick settings & branch view
                    BranchesScreen(
                      branches = branches,
                      searchQuery = branchSearchQuery,
                      onlineOnly = branchOnlineOnly,
                      onSearchChange = { viewModel.setBranchSearch(it) },
                      onOnlineOnlyChange = { viewModel.setOnlineFilter(it) },
                      onToggleStatus = { viewModel.toggleBranchStatus(it) },
                      onAddBranch = { name, loc, code, screens ->
                        viewModel.addBranch(name, loc, code, screens)
                      },
                      onDeleteBranch = { viewModel.deleteBranch(it) }
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  // Fullscreen TV Live Player Dialog
  if (isTvPlayerOpen) {
    TvPlayerFullScreenDialog(
      playlist = activePlaylist,
      currentSlideIndex = tvPlayerIndex,
      isPlaying = isTvPlaying,
      onTogglePlay = { viewModel.toggleTvPlayback() },
      onNextSlide = { viewModel.nextTvSlide() },
      onPreviousSlide = { viewModel.previousTvSlide() },
      onClose = { viewModel.closeTvPlayer() }
    )
  }
}
