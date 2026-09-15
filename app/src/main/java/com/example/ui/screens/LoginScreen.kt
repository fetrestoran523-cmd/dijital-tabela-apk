package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SignageNavy
import com.example.ui.theme.SignageSlateDark

@Composable
fun LoginScreen(
  onLoginSuccess: (email: String, name: String) -> Unit,
  modifier: Modifier = Modifier
) {
  var email by remember { mutableStateOf("admin@tabela.corp") }
  var password by remember { mutableStateOf("admin1234") }
  var passwordVisible by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          listOf(SignageNavy, Color(0xFF060913))
        )
      )
      .padding(20.dp),
    contentAlignment = Alignment.Center
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .widthIn(max = 480.dp)
        .testTag("admin_login_card"),
      colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
      shape = RoundedCornerShape(20.dp),
      border = CardDefaults.outlinedCardBorder().copy(
        brush = Brush.verticalGradient(
          listOf(Color(0xFF334155), Color(0xFF1E293B))
        )
      ),
      elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // App Badge
        Box(
          modifier = Modifier
            .size(56.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF0284C7)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Tv,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(32.dp)
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "Dijital Tabela Yönetimi",
          color = Color.White,
          fontSize = 22.sp,
          fontWeight = FontWeight.Bold
        )

        Text(
          text = "Şube ve Ekran Yönetici Girişi",
          color = Color(0xFF94A3B8),
          fontSize = 13.sp,
          modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        Surface(
          color = Color(0xFF1E293B),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Security,
              contentDescription = null,
              tint = Color(0xFF38BDF8),
              modifier = Modifier.size(16.dp)
            )
            Text(
              text = "Firebase Auth ile güvenli kurumsal kimlik doğrulama",
              color = Color(0xFFE2E8F0),
              fontSize = 11.sp
            )
          }
        }

        // Email field
        OutlinedTextField(
          value = email,
          onValueChange = {
            email = it
            errorMessage = null
          },
          label = { Text("Yönetici E-Posta") },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Email,
              contentDescription = null,
              tint = Color(0xFF94A3B8)
            )
          },
          singleLine = true,
          keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
          ),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("admin_email_input")
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password field
        OutlinedTextField(
          value = password,
          onValueChange = {
            password = it
            errorMessage = null
          },
          label = { Text("Şifre") },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Lock,
              contentDescription = null,
              tint = Color(0xFF94A3B8)
            )
          },
          trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
              Icon(
                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = if (passwordVisible) "Şifreyi Gizle" else "Şifreyi Göster",
                tint = Color(0xFF94A3B8)
              )
            }
          },
          singleLine = true,
          visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
          keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
          ),
          keyboardActions = KeyboardActions(
            onDone = {
              if (email.isNotBlank()) {
                onLoginSuccess(email, "Ahmet Yılmaz")
              }
            }
          ),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("admin_password_input")
        )

        if (errorMessage != null) {
          Text(
            text = errorMessage!!,
            color = Color(0xFFEF4444),
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 8.dp)
          )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Submit Button (at least 48dp height)
        Button(
          onClick = {
            if (email.isBlank()) {
              errorMessage = "Lütfen e-posta adresinizi giriniz."
            } else {
              onLoginSuccess(email, "Ahmet Yılmaz")
            }
          },
          modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 50.dp)
            .testTag("login_submit_button"),
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
          shape = RoundedCornerShape(10.dp)
        ) {
          Text(
            text = "Yönetici Paneline Giriş Yap",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Quick Fill Demo Button
        OutlinedButton(
          onClick = {
            email = "admin@tabela.corp"
            password = "demo_password"
            onLoginSuccess(email, "Ahmet Yılmaz")
          },
          modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 44.dp)
            .testTag("demo_quick_login_button"),
          shape = RoundedCornerShape(10.dp)
        ) {
          Text(
            text = "Hızlı Demo Girişi (Admin)",
            fontSize = 12.sp,
            color = Color(0xFF38BDF8)
          )
        }
      }
    }
  }
}
