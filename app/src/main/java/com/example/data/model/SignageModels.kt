package com.example.data.model

import java.util.UUID

enum class MediaType {
  IMAGE,
  VIDEO
}

data class Branch(
  val id: String = UUID.randomUUID().toString(),
  val name: String,
  val location: String,
  val devicePairingCode: String,
  val isOnline: Boolean = true,
  val screenCount: Int = 2,
  val lastPing: String = "Şimdi aktif",
  val activePlaylistId: String? = null
)

data class MediaItem(
  val id: String = UUID.randomUUID().toString(),
  val title: String,
  val type: MediaType,
  val url: String,
  val durationSeconds: Int = 10,
  val fileSize: String = "3.5 MB",
  val resolution: String = "1920x1080",
  val uploadedAt: String = "Bugün"
)

data class PlaylistItem(
  val id: String = UUID.randomUUID().toString(),
  val mediaId: String,
  val title: String,
  val type: MediaType,
  val durationSeconds: Int = 10,
  val previewUrl: String = ""
)

data class Playlist(
  val id: String = UUID.randomUUID().toString(),
  val name: String,
  val items: List<PlaylistItem> = emptyList(),
  val assignedBranchIds: List<String> = emptyList(),
  val isPublished: Boolean = true,
  val updatedAt: String = "Bugün, 14:30"
) {
  val totalDurationSeconds: Int
    get() = items.sumOf { it.durationSeconds }
}

data class PriceProduct(
  val id: String = UUID.randomUUID().toString(),
  val branchId: String,
  val name: String,
  val category: String = "Günün Menüsü",
  val price: Double,
  val oldPrice: Double? = null,
  val description: String = "",
  val isHighlighted: Boolean = false
) {
  val discountPercent: Int?
    get() {
      val old = oldPrice ?: return null
      if (old <= price || old <= 0) return null
      return (((old - price) / old) * 100).toInt()
    }
}

enum class PriceSlideTheme(val title: String, val subtitle: String) {
  DARK_LUXURY("Karanlık Lüks (Cinema)", "Premium restoran & bar görünümü"),
  BISTRO_WARM("Sıcak Kafe / Bistro", "Kahve ve fırın ürünleri için ideal"),
  NEON_PROMO("Market Fırsat / Neon", "Yüksek kontrastlı kampanya ve indirim tasarımı"),
  MINIMAL_CORP("Sade Kurumsal", "Hafif, ferah ve profesyonel tasarım")
}

data class AdminUser(
  val email: String,
  val name: String,
  val role: String = "Sistem Yöneticisi",
  val branchCount: Int = 4,
  val lastLogin: String = "Şimdi"
)

enum class ViewportMode(val title: String, val description: String) {
  AUTO("Otomatik", "Cihazın kendi ekran genişliği"),
  MOBILE("Mobil (390px)", "360-767px dikey stack & hamburger menü"),
  TABLET("Tablet (820px)", "768-1024px 2-3 kolonlu grid"),
  DESKTOP("Masaüstü (1440px)", "1440px+ tam genişlik kurumsal panel")
}

enum class SignageScreen(val title: String, val badge: String? = null) {
  BRANCHES("Şube Yönetimi"),
  MEDIA("Medya Kütüphanesi"),
  PLAYLISTS("Playlist Oluşturucu"),
  PRICES("Fiyat Yönetimi & TV Slayt"),
  SETTINGS("Sistem & Cihaz Ayarları")
}
