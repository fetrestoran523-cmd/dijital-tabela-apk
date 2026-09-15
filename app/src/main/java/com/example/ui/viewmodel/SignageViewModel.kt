package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AdminUser
import com.example.data.model.Branch
import com.example.data.model.MediaItem
import com.example.data.model.MediaType
import com.example.data.model.Playlist
import com.example.data.model.PriceProduct
import com.example.data.model.PriceSlideTheme
import com.example.data.model.SignageScreen
import com.example.data.model.ViewportMode
import com.example.data.repository.SignageRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SignageViewModel(
  private val repository: SignageRepository = SignageRepository()
) : ViewModel() {

  // Current Navigation Screen
  private val _currentScreen = MutableStateFlow(SignageScreen.BRANCHES)
  val currentScreen: StateFlow<SignageScreen> = _currentScreen.asStateFlow()

  // Viewport mode simulation (Auto, Mobile 390px, Tablet 820px, Desktop 1440px)
  private val _viewportMode = MutableStateFlow(ViewportMode.AUTO)
  val viewportMode: StateFlow<ViewportMode> = _viewportMode.asStateFlow()

  // Selected Branch for Price Management / Details
  private val _selectedBranchId = MutableStateFlow<String>("b1")
  val selectedBranchId: StateFlow<String> = _selectedBranchId.asStateFlow()

  // Selected Playlist for Editing & Preview
  private val _selectedPlaylistId = MutableStateFlow<String>("pl1")
  val selectedPlaylistId: StateFlow<String> = _selectedPlaylistId.asStateFlow()

  // Fullscreen TV Live Player State
  private val _isTvPlayerOpen = MutableStateFlow(false)
  val isTvPlayerOpen: StateFlow<Boolean> = _isTvPlayerOpen.asStateFlow()

  private val _tvPlayerSlideIndex = MutableStateFlow(0)
  val tvPlayerSlideIndex: StateFlow<Int> = _tvPlayerSlideIndex.asStateFlow()

  private val _isTvPlaying = MutableStateFlow(true)
  val isTvPlaying: StateFlow<Boolean> = _isTvPlaying.asStateFlow()

  private var tvPlaybackJob: Job? = null

  // Search & Filters
  private val _branchSearchQuery = MutableStateFlow("")
  val branchSearchQuery: StateFlow<String> = _branchSearchQuery.asStateFlow()

  private val _branchFilterOnlineOnly = MutableStateFlow(false)
  val branchFilterOnlineOnly: StateFlow<Boolean> = _branchFilterOnlineOnly.asStateFlow()

  private val _mediaTypeFilter = MutableStateFlow<MediaType?>(null)
  val mediaTypeFilter: StateFlow<MediaType?> = _mediaTypeFilter.asStateFlow()

  // Repository Flows
  val currentUser: StateFlow<AdminUser?> = repository.currentUser
  val rawBranches: StateFlow<List<Branch>> = repository.branches
  val rawMediaItems: StateFlow<List<MediaItem>> = repository.mediaItems
  val playlists: StateFlow<List<Playlist>> = repository.playlists
  val rawPriceProducts: StateFlow<List<PriceProduct>> = repository.priceProducts
  val slideTheme: StateFlow<PriceSlideTheme> = repository.activeSlideTheme

  // Filtered Branches
  val filteredBranches: StateFlow<List<Branch>> = combine(
    rawBranches,
    _branchSearchQuery,
    _branchFilterOnlineOnly
  ) { list, query, onlineOnly ->
    list.filter { branch ->
      val matchesQuery = query.isBlank() ||
        branch.name.contains(query, ignoreCase = true) ||
        branch.location.contains(query, ignoreCase = true) ||
        branch.devicePairingCode.contains(query, ignoreCase = true)

      val matchesOnline = !onlineOnly || branch.isOnline
      matchesQuery && matchesOnline
    }
  }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

  // Filtered Media
  val filteredMediaItems: StateFlow<List<MediaItem>> = combine(
    rawMediaItems,
    _mediaTypeFilter
  ) { list, filterType ->
    if (filterType == null) list else list.filter { it.type == filterType }
  }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

  // Products for currently selected branch
  val currentBranchProducts: StateFlow<List<PriceProduct>> = combine(
    rawPriceProducts,
    _selectedBranchId
  ) { list, branchId ->
    list.filter { it.branchId == branchId }
  }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

  // Currently Selected Playlist
  val activePlaylist: StateFlow<Playlist?> = combine(
    playlists,
    _selectedPlaylistId
  ) { list, id ->
    list.firstOrNull { it.id == id } ?: list.firstOrNull()
  }.stateIn(viewModelScope, SharingStarted.Lazily, null)

  // Screen & Navigation
  fun navigateTo(screen: SignageScreen) {
    _currentScreen.value = screen
  }

  fun setViewportMode(mode: ViewportMode) {
    _viewportMode.value = mode
  }

  fun selectBranch(branchId: String) {
    _selectedBranchId.value = branchId
  }

  fun selectPlaylist(playlistId: String) {
    _selectedPlaylistId.value = playlistId
  }

  fun setBranchSearch(query: String) {
    _branchSearchQuery.value = query
  }

  fun setOnlineFilter(onlineOnly: Boolean) {
    _branchFilterOnlineOnly.value = onlineOnly
  }

  fun setMediaTypeFilter(type: MediaType?) {
    _mediaTypeFilter.value = type
  }

  fun setSlideTheme(theme: PriceSlideTheme) {
    repository.setSlideTheme(theme)
  }

  // Auth
  fun login(email: String, name: String) {
    repository.login(email, name)
  }

  fun logout() {
    repository.logout()
  }

  // Branch CRUD
  fun addBranch(name: String, location: String, pairingCode: String, screenCount: Int) {
    repository.addBranch(name, location, pairingCode, screenCount)
  }

  fun updateBranch(branch: Branch) {
    repository.updateBranch(branch)
  }

  fun toggleBranchStatus(branchId: String) {
    repository.toggleBranchStatus(branchId)
  }

  fun deleteBranch(branchId: String) {
    repository.deleteBranch(branchId)
    if (_selectedBranchId.value == branchId) {
      _selectedBranchId.value = rawBranches.value.firstOrNull { it.id != branchId }?.id ?: ""
    }
  }

  // Media Library CRUD
  fun uploadMedia(title: String, type: MediaType, duration: Int, url: String) {
    repository.addMediaItem(title, type, duration, url)
  }

  fun deleteMedia(mediaId: String) {
    repository.deleteMediaItem(mediaId)
  }

  // Playlist CRUD
  fun createPlaylist(name: String, assignedBranchIds: List<String>) {
    repository.createPlaylist(name, assignedBranchIds)
  }

  fun addMediaToActivePlaylist(mediaItem: MediaItem) {
    val plId = _selectedPlaylistId.value
    repository.addMediaToPlaylist(plId, mediaItem)
  }

  fun movePlaylistItem(itemIndex: Int, moveUp: Boolean) {
    val plId = _selectedPlaylistId.value
    repository.movePlaylistItem(plId, itemIndex, moveUp)
  }

  fun updateItemDuration(itemId: String, durationSeconds: Int) {
    val plId = _selectedPlaylistId.value
    repository.updatePlaylistItemDuration(plId, itemId, durationSeconds)
  }

  fun removePlaylistItem(itemId: String) {
    val plId = _selectedPlaylistId.value
    repository.removePlaylistItem(plId, itemId)
  }

  fun toggleBranchAssignment(playlistId: String, branchId: String) {
    val pl = playlists.value.firstOrNull { it.id == playlistId } ?: return
    val current = pl.assignedBranchIds.toMutableList()
    if (current.contains(branchId)) {
      current.remove(branchId)
    } else {
      current.add(branchId)
    }
    repository.updatePlaylistBranches(playlistId, current)
  }

  fun deletePlaylist(playlistId: String) {
    repository.deletePlaylist(playlistId)
    if (_selectedPlaylistId.value == playlistId) {
      _selectedPlaylistId.value = playlists.value.firstOrNull { it.id != playlistId }?.id ?: ""
    }
  }

  // Price Management CRUD
  fun addProduct(
    name: String,
    category: String,
    price: Double,
    oldPrice: Double?,
    description: String,
    isHighlighted: Boolean
  ) {
    val bId = _selectedBranchId.value
    repository.addPriceProduct(
      branchId = bId,
      name = name,
      category = category,
      price = price,
      oldPrice = oldPrice,
      description = description,
      isHighlighted = isHighlighted
    )
  }

  fun updateProduct(product: PriceProduct) {
    repository.updatePriceProduct(product)
  }

  fun deleteProduct(productId: String) {
    repository.deletePriceProduct(productId)
  }

  // Fullscreen TV Signage Player Simulation
  fun openTvPlayer() {
    _isTvPlayerOpen.value = true
    _tvPlayerSlideIndex.value = 0
    _isTvPlaying.value = true
    startTvLoop()
  }

  fun closeTvPlayer() {
    _isTvPlayerOpen.value = false
    tvPlaybackJob?.cancel()
  }

  fun toggleTvPlayback() {
    _isTvPlaying.value = !_isTvPlaying.value
    if (_isTvPlaying.value) {
      startTvLoop()
    } else {
      tvPlaybackJob?.cancel()
    }
  }

  fun nextTvSlide() {
    val items = activePlaylist.value?.items.orEmpty()
    if (items.isNotEmpty()) {
      _tvPlayerSlideIndex.value = (_tvPlayerSlideIndex.value + 1) % items.size
    }
  }

  fun previousTvSlide() {
    val items = activePlaylist.value?.items.orEmpty()
    if (items.isNotEmpty()) {
      val prev = _tvPlayerSlideIndex.value - 1
      _tvPlayerSlideIndex.value = if (prev < 0) items.size - 1 else prev
    }
  }

  private fun startTvLoop() {
    tvPlaybackJob?.cancel()
    tvPlaybackJob = viewModelScope.launch {
      while (_isTvPlaying.value) {
        val items = activePlaylist.value?.items.orEmpty()
        if (items.isEmpty()) {
          delay(2000)
          continue
        }
        val currentSlide = items.getOrNull(_tvPlayerSlideIndex.value)
        val durationMs = (currentSlide?.durationSeconds ?: 8) * 1000L
        delay(durationMs)
        if (_isTvPlaying.value) {
          nextTvSlide()
        }
      }
    }
  }
}
