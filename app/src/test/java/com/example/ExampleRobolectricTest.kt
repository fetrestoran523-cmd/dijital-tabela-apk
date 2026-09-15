package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.SignageRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Dijital Tabela", appName)
  }

  @Test
  fun `repository contains initial mock branches`() {
    val repo = SignageRepository()
    val branchList = repo.branches.value
    assertTrue(branchList.isNotEmpty())
    assertNotNull(branchList.firstOrNull { it.isOnline })
  }
}

