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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PriceChange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Branch
import com.example.data.model.PriceProduct
import com.example.data.model.PriceSlideTheme
import com.example.ui.components.PriceSlide16x9Preview
import com.example.ui.components.TvPreviewContainer

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PriceManagementScreen(
  branches: List<Branch>,
  selectedBranchId: String,
  products: List<PriceProduct>,
  activeTheme: PriceSlideTheme,
  onSelectBranch: (String) -> Unit,
  onSelectTheme: (PriceSlideTheme) -> Unit,
  onAddProduct: (name: String, category: String, price: Double, oldPrice: Double?, description: String, isHighlighted: Boolean) -> Unit,
  onDeleteProduct: (String) -> Unit,
  onOpenFullScreenTv: () -> Unit,
  modifier: Modifier = Modifier
) {
  val currentBranch = branches.firstOrNull { it.id == selectedBranchId } ?: branches.firstOrNull()

  // Form State
  var productName by remember { mutableStateOf("") }
  var productCategory by remember { mutableStateOf("Burgerler") }
  var productPriceText by remember { mutableStateOf("") }
  var productOldPriceText by remember { mutableStateOf("") }
  var productDescription by remember { mutableStateOf("") }
  var isHighlighted by remember { mutableStateOf(false) }
  var formError by remember { mutableStateOf<String?>(null) }

  BoxWithConstraints(modifier = modifier.fillMaxSize()) {
    val screenWidth = maxWidth
    val isMobile = screenWidth < 900.dp

    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(if (isMobile) 12.dp else 20.dp)
    ) {
      // Header
      Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Text(
          text = "Fiyat Yönetimi & 16:9 TV Slayt Üretimi",
          style = if (isMobile) MaterialTheme.typography.titleLarge else MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
        Text(
          text = "Her şube için ürün ve fiyat girişi yapın, otomatik 1920x1080 (16:9 TV) slaytı anında oluşturulsun",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      // Branch Selector Chips (Stacks cleanly)
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(10.dp)
      ) {
        Column(modifier = Modifier.padding(10.dp)) {
          Text(
            text = "Aktif Şube Seçimi:",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF64748B)
          )
          Spacer(modifier = Modifier.height(6.dp))
          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            branches.forEach { branch ->
              val isSelected = branch.id == currentBranch?.id
              FilterChip(
                selected = isSelected,
                onClick = { onSelectBranch(branch.id) },
                label = { Text(branch.name, fontSize = 12.sp) },
                leadingIcon = {
                  Icon(
                    imageVector = Icons.Default.Business,
                    contentDescription = null,
                    tint = if (isSelected) Color(0xFF0284C7) else Color(0xFF94A3B8),
                    modifier = Modifier.size(14.dp)
                  )
                },
                modifier = Modifier.defaultMinSize(minHeight = 44.dp)
              )
            }
          }
        }
      }

      // Theme Selector Chips
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(10.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Palette,
              contentDescription = null,
              tint = Color(0xFF0284C7),
              modifier = Modifier.size(16.dp)
            )
            Text(
              text = "TV Slayt Görsel Teması:",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }

          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            PriceSlideTheme.values().forEach { theme ->
              FilterChip(
                selected = theme == activeTheme,
                onClick = { onSelectTheme(theme) },
                label = { Text(theme.title, fontSize = 11.sp) },
                modifier = Modifier.defaultMinSize(minHeight = 44.dp)
              )
            }
          }
        }
      }

      // Main Responsive Area
      if (isMobile) {
        // MOBILE VERTICAL STACK (No horizontal scroll)
        Column(
          modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          // 16:9 TV SLIDE PREVIEW (STRICT RATIO RULE)
          TvPreviewContainer(
            title = "${currentBranch?.name ?: "Şube"} 1920x1080 Menü TV Slaytı",
            subtitle = "Canlı ${products.size} Ürün Listesi",
            onOpenFullScreen = onOpenFullScreenTv
          ) {
            PriceSlide16x9Preview(
              branch = currentBranch,
              products = products,
              theme = activeTheme
            )
          }

          // Price entry form
          PriceEntryFormCard(
            productName = productName,
            productCategory = productCategory,
            productPriceText = productPriceText,
            productOldPriceText = productOldPriceText,
            productDescription = productDescription,
            isHighlighted = isHighlighted,
            formError = formError,
            onNameChange = { productName = it; formError = null },
            onCategoryChange = { productCategory = it },
            onPriceChange = { productPriceText = it; formError = null },
            onOldPriceChange = { productOldPriceText = it },
            onDescriptionChange = { productDescription = it },
            onHighlightChange = { isHighlighted = it },
            onSubmit = {
              val price = productPriceText.toDoubleOrNull()
              if (productName.isBlank() || price == null || price <= 0) {
                formError = "Lütfen geçerli bir ürün adı ve fiyatı giriniz."
              } else {
                val old = productOldPriceText.toDoubleOrNull()
                onAddProduct(productName, productCategory, price, old, productDescription, isHighlighted)
                // Clear
                productName = ""
                productPriceText = ""
                productOldPriceText = ""
                productDescription = ""
                isHighlighted = false
                formError = null
              }
            }
          )

          // Products List
          PriceProductsTableCard(
            products = products,
            onDeleteProduct = onDeleteProduct
          )
        }
      } else {
        // DESKTOP TWO-COLUMN LAYOUT
        Row(
          modifier = Modifier
            .fillMaxSize()
            .weight(1f),
          horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          // Left: Form & Products list
          Column(
            modifier = Modifier
              .weight(1f)
              .fillMaxHeight()
              .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            PriceEntryFormCard(
              productName = productName,
              productCategory = productCategory,
              productPriceText = productPriceText,
              productOldPriceText = productOldPriceText,
              productDescription = productDescription,
              isHighlighted = isHighlighted,
              formError = formError,
              onNameChange = { productName = it; formError = null },
              onCategoryChange = { productCategory = it },
              onPriceChange = { productPriceText = it; formError = null },
              onOldPriceChange = { productOldPriceText = it },
              onDescriptionChange = { productDescription = it },
              onHighlightChange = { isHighlighted = it },
              onSubmit = {
                val price = productPriceText.toDoubleOrNull()
                if (productName.isBlank() || price == null || price <= 0) {
                  formError = "Lütfen geçerli bir ürün adı ve fiyatı giriniz."
                } else {
                  val old = productOldPriceText.toDoubleOrNull()
                  onAddProduct(productName, productCategory, price, old, productDescription, isHighlighted)
                  productName = ""
                  productPriceText = ""
                  productOldPriceText = ""
                  productDescription = ""
                  isHighlighted = false
                  formError = null
                }
              }
            )

            PriceProductsTableCard(
              products = products,
              onDeleteProduct = onDeleteProduct
            )
          }

          // Right: 16:9 TV Preview Enclosure
          Column(
            modifier = Modifier
              .weight(1.1f)
              .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            // STRICT 16:9 TV PREVIEW
            TvPreviewContainer(
              title = "${currentBranch?.name ?: "Şube"} 1920x1080 Menü TV Slaytı",
              subtitle = "Gerçek Zamanlı FHD Slayt Üretimi",
              onOpenFullScreen = onOpenFullScreenTv
            ) {
              PriceSlide16x9Preview(
                branch = currentBranch,
                products = products,
                theme = activeTheme
              )
            }

            Card(
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Text(
                  text = "Otomatik 1920x1080 Fiyat Slaytı Hakkında",
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "Forma girilen ürün adı, güncel fiyat ve eski fiyat bilgileri otomatik olarak 16:9 TV oranında hesaplanır. İndirimli ürünlerde eski fiyatın üstü çizilir ve yüzde indirim rozeti eklenir.",
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
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PriceEntryFormCard(
  productName: String,
  productCategory: String,
  productPriceText: String,
  productOldPriceText: String,
  productDescription: String,
  isHighlighted: Boolean,
  formError: String?,
  onNameChange: (String) -> Unit,
  onCategoryChange: (String) -> Unit,
  onPriceChange: (String) -> Unit,
  onOldPriceChange: (String) -> Unit,
  onDescriptionChange: (String) -> Unit,
  onHighlightChange: (Boolean) -> Unit,
  onSubmit: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("price_entry_form"),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    shape = RoundedCornerShape(12.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Icon(
          imageVector = Icons.Default.PriceChange,
          contentDescription = null,
          tint = Color(0xFF0284C7),
          modifier = Modifier.size(18.dp)
        )
        Text(
          text = "Yeni Ürün & Fiyat Ekle",
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp
        )
      }

      OutlinedTextField(
        value = productName,
        onValueChange = onNameChange,
        label = { Text("Ürün Adı (Örn: Double Smash Burger)") },
        singleLine = true,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("product_name_input")
      )

      // Categories quick selector
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        listOf("Burgerler", "Kahveler", "Pizzalar", "Tatlılar", "İçecekler").forEach { cat ->
          Surface(
            color = if (cat == productCategory) Color(0xFF0284C7) else Color(0xFFF1F5F9),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
              .clickable { onCategoryChange(cat) }
              .defaultMinSize(minHeight = 36.dp)
          ) {
            Text(
              text = cat,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = if (cat == productCategory) Color.White else Color(0xFF475569),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
            )
          }
        }
      }

      // Price + Old Price Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedTextField(
          value = productPriceText,
          onValueChange = onPriceChange,
          label = { Text("Güncel Fiyat (₺)") },
          placeholder = { Text("280") },
          singleLine = true,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier
            .weight(1f)
            .testTag("product_price_input")
        )

        OutlinedTextField(
          value = productOldPriceText,
          onValueChange = onOldPriceChange,
          label = { Text("Eski Fiyat (İsteğe Bağlı)") },
          placeholder = { Text("340") },
          singleLine = true,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier
            .weight(1f)
            .testTag("product_old_price_input")
        )
      }

      OutlinedTextField(
        value = productDescription,
        onValueChange = onDescriptionChange,
        label = { Text("Açıklama / İçerik (Kısa)") },
        placeholder = { Text("Örn: %100 Dana eti, karamelize soğan...") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
      )

      // Highlight checkbox
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clickable { onHighlightChange(!isHighlighted) }
          .defaultMinSize(minHeight = 44.dp)
      ) {
        Checkbox(
          checked = isHighlighted,
          onCheckedChange = onHighlightChange,
          modifier = Modifier.size(44.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "Menü Slaytında Öne Çıkar (Günün Fırsatı Rozeti)",
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium
        )
      }

      if (formError != null) {
        Text(
          text = formError,
          color = Color(0xFFEF4444),
          fontSize = 12.sp
        )
      }

      Button(
        onClick = onSubmit,
        modifier = Modifier
          .fillMaxWidth()
          .defaultMinSize(minHeight = 46.dp)
          .testTag("submit_product_button"),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
        shape = RoundedCornerShape(8.dp)
      ) {
        Icon(Icons.Default.Add, contentDescription = null)
        Spacer(modifier = Modifier.width(6.dp))
        Text("Fiyat Slaytına Ekle & Önizlemeyi Güncelle", fontWeight = FontWeight.Bold)
      }
    }
  }
}

@Composable
fun PriceProductsTableCard(
  products: List<PriceProduct>,
  onDeleteProduct: (String) -> Unit,
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
        Text(
          text = "Şubeye Ait Ürün Listesi (${products.size})",
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp
        )

        Text(
          text = "Dikey Mobil Uyumlu",
          fontSize = 10.sp,
          color = Color(0xFF64748B)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      if (products.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "Bu şube için henüz ürün eklenmemiş.",
            fontSize = 12.sp,
            color = Color(0xFF94A3B8)
          )
        }
      } else {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          products.forEach { product ->
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color(0xFFF8FAFC),
              border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE2E8F0))
              ),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    if (product.isHighlighted) {
                      Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Öne Çıkarılan",
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(14.dp)
                      )
                      Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                      text = product.name,
                      fontWeight = FontWeight.Bold,
                      fontSize = 13.sp,
                      maxLines = 1,
                      overflow = TextOverflow.Ellipsis
                    )
                  }

                  Text(
                    text = "${product.category}${if (product.description.isNotBlank()) " • ${product.description}" else ""}",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                }

                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  Column(horizontalAlignment = Alignment.End) {
                    if (product.oldPrice != null && product.oldPrice > product.price) {
                      Text(
                        text = "₺${"%.0f".format(product.oldPrice)}",
                        color = Color(0xFFEF4444),
                        fontSize = 10.sp,
                        textDecoration = TextDecoration.LineThrough
                      )
                    }
                    Text(
                      text = "₺${"%.0f".format(product.price)}",
                      fontWeight = FontWeight.ExtraBold,
                      color = Color(0xFF0284C7),
                      fontSize = 14.sp
                    )
                  }

                  IconButton(
                    onClick = { onDeleteProduct(product.id) },
                    modifier = Modifier
                      .size(44.dp)
                      .testTag("delete_product_${product.id}")
                  ) {
                    Icon(
                      imageVector = Icons.Default.DeleteOutline,
                      contentDescription = "Ürünü Sil",
                      tint = Color(0xFFEF4444)
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
}
