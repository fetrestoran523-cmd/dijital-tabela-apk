package com.example.data.repository

import com.example.data.model.AdminUser
import com.example.data.model.Branch
import com.example.data.model.MediaItem
import com.example.data.model.MediaType
import com.example.data.model.Playlist
import com.example.data.model.PlaylistItem
import com.example.data.model.PriceProduct
import com.example.data.model.PriceSlideTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class SignageRepository {

  // Firebase Auth Simulation / Management
  private val _currentUser = MutableStateFlow<AdminUser?>(
    AdminUser(
      email = "admin@tabela.corp",
      name = "Ahmet Yılmaz",
      role = "Sistem Yöneticisi (Super Admin)",
      branchCount = 4
    )
  )
  val currentUser: StateFlow<AdminUser?> = _currentUser.asStateFlow()

  // Firestore "branches" Collection
  private val _branches = MutableStateFlow<List<Branch>>(
    listOf(
      Branch(
        id = "b1",
        name = "Kadıköy Rıhtım Şubesi",
        location = "İstanbul / Kadıköy - Rıhtım Cd. No:18",
        devicePairingCode = "TR-8924",
        isOnline = true,
        screenCount = 3,
        lastPing = "1 dk önce aktif",
        activePlaylistId = "pl1"
      ),
      Branch(
        id = "b2",
        name = "Beşiktaş Çarşı Şubesi",
        location = "İstanbul / Beşiktaş - Ihlamurdere Cd. No:42",
        devicePairingCode = "TR-4105",
        isOnline = true,
        screenCount = 2,
        lastPing = "3 dk önce aktif",
        activePlaylistId = "pl1"
      ),
      Branch(
        id = "b3",
        name = "Ankara Tunalı Hilmi Şubesi",
        location = "Ankara / Çankaya - Tunalı Hilmi Cd. No:88",
        devicePairingCode = "TR-9023",
        isOnline = false,
        screenCount = 2,
        lastPing = "45 dk önce çevrimdışı",
        activePlaylistId = "pl2"
      ),
      Branch(
        id = "b4",
        name = "İzmir Alsancak Kordon",
        location = "İzmir / Konak - Atatürk Cd. No:112",
        devicePairingCode = "TR-3378",
        isOnline = true,
        screenCount = 4,
        lastPing = "Şimdi aktif",
        activePlaylistId = "pl1"
      )
    )
  )
  val branches: StateFlow<List<Branch>> = _branches.asStateFlow()

  // Firebase Storage / Media Library
  private val _mediaItems = MutableStateFlow<List<MediaItem>>(
    listOf(
      MediaItem(
        id = "m1",
        title = "Günün Menüsü & Burger Kampanyası",
        type = MediaType.IMAGE,
        url = "https://images.unsplash.com/photo-1550547660-d9450f859349?w=800&q=80",
        durationSeconds = 12,
        fileSize = "2.8 MB",
        resolution = "1920x1080 FHD",
        uploadedAt = "Bugün, 09:15"
      ),
      MediaItem(
        id = "m2",
        title = "Artisan Kahve & Kruvasan Tanıtımı",
        type = MediaType.IMAGE,
        url = "https://images.unsplash.com/photo-1509042239860-f550ce710b93?w=800&q=80",
        durationSeconds = 10,
        fileSize = "3.1 MB",
        resolution = "1920x1080 FHD",
        uploadedAt = "Dün, 16:40"
      ),
      MediaItem(
        id = "m3",
        title = "Hafta Sonu Özel İndirim Animasyonu",
        type = MediaType.VIDEO,
        url = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800&q=80",
        durationSeconds = 15,
        fileSize = "18.4 MB",
        resolution = "1920x1080 60fps",
        uploadedAt = "12 Eylül 2026"
      ),
      MediaItem(
        id = "m4",
        title = "Taze Taş Fırın Pizza Lansmanı",
        type = MediaType.IMAGE,
        url = "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=800&q=80",
        durationSeconds = 12,
        fileSize = "4.2 MB",
        resolution = "1920x1080 FHD",
        uploadedAt = "10 Eylül 2026"
      ),
      MediaItem(
        id = "m5",
        title = "Organik Soğuk İçecekler ve Mocktail",
        type = MediaType.IMAGE,
        url = "https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?w=800&q=80",
        durationSeconds = 8,
        fileSize = "2.5 MB",
        resolution = "1920x1080 FHD",
        uploadedAt = "8 Eylül 2026"
      ),
      MediaItem(
        id = "m6",
        title = "Kurumsal Sosyal Sorumluluk Videosu",
        type = MediaType.VIDEO,
        url = "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=800&q=80",
        durationSeconds = 20,
        fileSize = "24.6 MB",
        resolution = "1920x1080 30fps",
        uploadedAt = "5 Eylül 2026"
      )
    )
  )
  val mediaItems: StateFlow<List<MediaItem>> = _mediaItems.asStateFlow()

  // Playlists
  private val _playlists = MutableStateFlow<List<Playlist>>(
    listOf(
      Playlist(
        id = "pl1",
        name = "Ana Ekran Kampanya Döngüsü (TV Canlı)",
        assignedBranchIds = listOf("b1", "b2", "b4"),
        isPublished = true,
        updatedAt = "Bugün, 11:20",
        items = listOf(
          PlaylistItem(
            id = "pi1",
            mediaId = "m1",
            title = "Günün Menüsü & Burger Kampanyası",
            type = MediaType.IMAGE,
            durationSeconds = 12,
            previewUrl = "https://images.unsplash.com/photo-1550547660-d9450f859349?w=800&q=80"
          ),
          PlaylistItem(
            id = "pi2",
            mediaId = "m2",
            title = "Artisan Kahve & Kruvasan Tanıtımı",
            type = MediaType.IMAGE,
            durationSeconds = 10,
            previewUrl = "https://images.unsplash.com/photo-1509042239860-f550ce710b93?w=800&q=80"
          ),
          PlaylistItem(
            id = "pi3",
            mediaId = "m4",
            title = "Taze Taş Fırın Pizza Lansmanı",
            type = MediaType.IMAGE,
            durationSeconds = 15,
            previewUrl = "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=800&q=80"
          )
        )
      ),
      Playlist(
        id = "pl2",
        name = "Kahve & Tatlı Öğleden Sonra Yayını",
        assignedBranchIds = listOf("b3"),
        isPublished = true,
        updatedAt = "Dün, 17:00",
        items = listOf(
          PlaylistItem(
            id = "pi4",
            mediaId = "m2",
            title = "Artisan Kahve & Kruvasan Tanıtımı",
            type = MediaType.IMAGE,
            durationSeconds = 12,
            previewUrl = "https://images.unsplash.com/photo-1509042239860-f550ce710b93?w=800&q=80"
          ),
          PlaylistItem(
            id = "pi5",
            mediaId = "m5",
            title = "Organik Soğuk İçecekler ve Mocktail",
            type = MediaType.IMAGE,
            durationSeconds = 10,
            previewUrl = "https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?w=800&q=80"
          )
        )
      )
    )
  )
  val playlists: StateFlow<List<Playlist>> = _playlists.asStateFlow()

  // Price Products (Grouped by Branch)
  private val _priceProducts = MutableStateFlow<List<PriceProduct>>(
    listOf(
      // Kadıköy Products
      PriceProduct(
        id = "p1",
        branchId = "b1",
        name = "Double Truffle Smash Burger",
        category = "Burgerler",
        price = 285.0,
        oldPrice = 340.0,
        description = "%100 Dana eti, trüf mayonez, cheddar, karamelize soğan",
        isHighlighted = true
      ),
      PriceProduct(
        id = "p2",
        branchId = "b1",
        name = "Artisan Flat White & Cookie",
        category = "Kahve Barı",
        price = 145.0,
        oldPrice = 175.0,
        description = "Özel kavrum çekirdek espresso, taze pişmiş çikolatalı kurabiye",
        isHighlighted = true
      ),
      PriceProduct(
        id = "p3",
        branchId = "b1",
        name = "Quattro Formaggi Taş Fırın Pizza",
        category = "Pizzalar",
        price = 320.0,
        oldPrice = null,
        description = "Mozzarella, gorgonzola, parmesan, gravyer peyniri",
        isHighlighted = false
      ),
      PriceProduct(
        id = "p4",
        branchId = "b1",
        name = "Organik Hibiscus & Nane Soğuk Çay",
        category = "İçecekler",
        price = 95.0,
        oldPrice = 115.0,
        description = "Ev yapımı demleme, taze nane ve limon eşliğinde",
        isHighlighted = false
      ),
      PriceProduct(
        id = "p5",
        branchId = "b1",
        name = "San Sebastian Cheesecake",
        category = "Tatlılar",
        price = 180.0,
        oldPrice = 220.0,
        description = "Sıcak Belçika çikolatası sosu ile servis edilir",
        isHighlighted = true
      ),

      // Beşiktaş Products
      PriceProduct(
        id = "p6",
        branchId = "b2",
        name = "Smash Cheesy Bacon Burger",
        category = "Burgerler",
        price = 295.0,
        oldPrice = 350.0,
        description = "Dana füme et, duble erimiş cheddar, patates kızartması",
        isHighlighted = true
      ),
      PriceProduct(
        id = "p7",
        branchId = "b2",
        name = "Iced Caramel Macchiato",
        category = "Kahve Barı",
        price = 135.0,
        oldPrice = 160.0,
        description = "Vanilya şurubu, soğuk süt, espresso ve karamel",
        isHighlighted = false
      ),

      // Ankara Products
      PriceProduct(
        id = "p8",
        branchId = "b3",
        name = "Tunalı Spesiyal Kasap Burger",
        category = "Burgerler",
        price = 270.0,
        oldPrice = 310.0,
        description = "Ev yapımı brioche ekmeği, özel barbekü sos",
        isHighlighted = true
      ),
      PriceProduct(
        id = "p9",
        branchId = "b3",
        name = "Filtre Kahve & Kruvasan Menü",
        category = "Kahve Barı",
        price = 120.0,
        oldPrice = 150.0,
        description = "Günün kahvesi ve tereyağlı sıcak kruvasan",
        isHighlighted = true
      ),

      // İzmir Products
      PriceProduct(
        id = "p10",
        branchId = "b4",
        name = "Kordon Gurme Burger Combo",
        category = "Burgerler",
        price = 310.0,
        oldPrice = 360.0,
        description = "Baharatlı elma dilim patates ve sınırsız içecek",
        isHighlighted = true
      ),
      PriceProduct(
        id = "p11",
        branchId = "b4",
        name = "Affogato Al Caffe",
        category = "Tatlılar",
        price = 140.0,
        oldPrice = null,
        description = "Vanilyalı dondurma üzerine sıcak duble espresso",
        isHighlighted = false
      )
    )
  )
  val priceProducts: StateFlow<List<PriceProduct>> = _priceProducts.asStateFlow()

  // Selected Price Slide Theme
  private val _activeSlideTheme = MutableStateFlow(PriceSlideTheme.DARK_LUXURY)
  val activeSlideTheme: StateFlow<PriceSlideTheme> = _activeSlideTheme.asStateFlow()

  fun setSlideTheme(theme: PriceSlideTheme) {
    _activeSlideTheme.value = theme
  }

  // Auth Operations
  fun login(email: String, name: String = "Admin") {
    _currentUser.value = AdminUser(
      email = email,
      name = name.ifBlank { "Yönetici" },
      role = "Sistem Yöneticisi",
      branchCount = _branches.value.size
    )
  }

  fun logout() {
    _currentUser.value = null
  }

  // Branch CRUD (Firestore Simulation)
  fun addBranch(name: String, location: String, devicePairingCode: String, screenCount: Int) {
    val newBranch = Branch(
      id = "b_${System.currentTimeMillis()}",
      name = name,
      location = location,
      devicePairingCode = devicePairingCode.ifBlank { "TR-${(1000..9999).random()}" },
      isOnline = true,
      screenCount = screenCount.coerceAtLeast(1),
      lastPing = "Yeni eklendi, çevrimiçi"
    )
    _branches.value = listOf(newBranch) + _branches.value
  }

  fun updateBranch(updated: Branch) {
    _branches.value = _branches.value.map { if (it.id == updated.id) updated else it }
  }

  fun toggleBranchStatus(branchId: String) {
    _branches.value = _branches.value.map {
      if (it.id == branchId) {
        val newStatus = !it.isOnline
        it.copy(
          isOnline = newStatus,
          lastPing = if (newStatus) "Şimdi aktif" else "Manuel kapatıldı"
        )
      } else it
    }
  }

  fun deleteBranch(branchId: String) {
    _branches.value = _branches.value.filter { it.id != branchId }
    // Clean products for this branch
    _priceProducts.value = _priceProducts.value.filter { it.branchId != branchId }
  }

  // Media Library Operations
  fun addMediaItem(title: String, type: MediaType, durationSeconds: Int, url: String) {
    val newItem = MediaItem(
      id = "m_${System.currentTimeMillis()}",
      title = title,
      type = type,
      url = url.ifBlank {
        if (type == MediaType.VIDEO)
          "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800&q=80"
        else
          "https://images.unsplash.com/photo-1550547660-d9450f859349?w=800&q=80"
      },
      durationSeconds = durationSeconds,
      fileSize = "${(2..18).random()}.${(1..9).random()} MB",
      resolution = "1920x1080 FHD",
      uploadedAt = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("tr")).format(Date())
    )
    _mediaItems.value = listOf(newItem) + _mediaItems.value
  }

  fun deleteMediaItem(mediaId: String) {
    _mediaItems.value = _mediaItems.value.filter { it.id != mediaId }
    // Also remove from all playlists
    _playlists.value = _playlists.value.map { pl ->
      pl.copy(items = pl.items.filter { it.mediaId != mediaId })
    }
  }

  // Playlist Operations
  fun createPlaylist(name: String, assignedBranchIds: List<String>) {
    val newPlaylist = Playlist(
      id = "pl_${System.currentTimeMillis()}",
      name = name,
      assignedBranchIds = assignedBranchIds,
      isPublished = true,
      updatedAt = "Şimdi",
      items = emptyList()
    )
    _playlists.value = listOf(newPlaylist) + _playlists.value
  }

  fun updatePlaylist(playlist: Playlist) {
    _playlists.value = _playlists.value.map { if (it.id == playlist.id) playlist else it }
  }

  fun deletePlaylist(playlistId: String) {
    _playlists.value = _playlists.value.filter { it.id != playlistId }
  }

  fun addMediaToPlaylist(playlistId: String, mediaItem: MediaItem) {
    _playlists.value = _playlists.value.map { pl ->
      if (pl.id == playlistId) {
        val newItem = PlaylistItem(
          id = UUID.randomUUID().toString(),
          mediaId = mediaItem.id,
          title = mediaItem.title,
          type = mediaItem.type,
          durationSeconds = mediaItem.durationSeconds,
          previewUrl = mediaItem.url
        )
        pl.copy(
          items = pl.items + newItem,
          updatedAt = "Şimdi"
        )
      } else pl
    }
  }

  fun movePlaylistItem(playlistId: String, itemIndex: Int, directionUp: Boolean) {
    _playlists.value = _playlists.value.map { pl ->
      if (pl.id == playlistId) {
        val targetIndex = if (directionUp) itemIndex - 1 else itemIndex + 1
        if (targetIndex in 0 until pl.items.size) {
          val mutable = pl.items.toMutableList()
          val item = mutable.removeAt(itemIndex)
          mutable.add(targetIndex, item)
          pl.copy(items = mutable, updatedAt = "Şimdi")
        } else pl
      } else pl
    }
  }

  fun updatePlaylistItemDuration(playlistId: String, itemId: String, newDurationSeconds: Int) {
    _playlists.value = _playlists.value.map { pl ->
      if (pl.id == playlistId) {
        pl.copy(
          items = pl.items.map {
            if (it.id == itemId) it.copy(durationSeconds = newDurationSeconds.coerceAtLeast(3)) else it
          },
          updatedAt = "Şimdi"
        )
      } else pl
    }
  }

  fun removePlaylistItem(playlistId: String, itemId: String) {
    _playlists.value = _playlists.value.map { pl ->
      if (pl.id == playlistId) {
        pl.copy(items = pl.items.filter { it.id != itemId }, updatedAt = "Şimdi")
      } else pl
    }
  }

  fun updatePlaylistBranches(playlistId: String, branchIds: List<String>) {
    _playlists.value = _playlists.value.map { pl ->
      if (pl.id == playlistId) {
        pl.copy(assignedBranchIds = branchIds, updatedAt = "Şimdi")
      } else pl
    }
  }

  // Price Product Operations
  fun addPriceProduct(
    branchId: String,
    name: String,
    category: String,
    price: Double,
    oldPrice: Double?,
    description: String = "",
    isHighlighted: Boolean = false
  ) {
    val newProduct = PriceProduct(
      id = "p_${System.currentTimeMillis()}",
      branchId = branchId,
      name = name,
      category = category.ifBlank { "Menü" },
      price = price,
      oldPrice = oldPrice,
      description = description,
      isHighlighted = isHighlighted
    )
    _priceProducts.value = listOf(newProduct) + _priceProducts.value
  }

  fun updatePriceProduct(product: PriceProduct) {
    _priceProducts.value = _priceProducts.value.map { if (it.id == product.id) product else it }
  }

  fun deletePriceProduct(productId: String) {
    _priceProducts.value = _priceProducts.value.filter { it.id != productId }
  }
}
