package com.example

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class LaunchTest {
    init {
        ShadowLog.stream = System.out
    }

    @Test
    fun testLaunch() {
        val controller = Robolectric.buildActivity(MainActivity::class.java)
        controller.create().start().resume().visible()
    }
}
