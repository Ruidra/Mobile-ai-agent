package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.AuraRepository
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
        assertEquals("Aura Agent", appName)
    }

    @Test
    fun `test repository initial data and schedule optimization`() {
        val repo = AuraRepository()
        val events = repo.scheduleEvents.value
        assertTrue("Schedule should contain events", events.isNotEmpty())

        val urgentEvent = events.firstOrNull { it.isUrgent }
        assertNotNull("Urgent event should be present", urgentEvent)

        val optimizationResult = repo.optimizeSchedule()
        assertTrue("Optimization should succeed", optimizationResult.contains("Schedule optimized"))

        val emails = repo.emails.value
        assertTrue("Emails should be triaged", emails.isNotEmpty())
    }
}
