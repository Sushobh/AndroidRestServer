package com.ranrings.libs.androidapptorest

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test

import org.junit.Assert.*

class WebAppRequestHandlerTest {

    @Test
    fun onRequest() {
        val webAppRequestHandler = WebAppRequestHandler(InstrumentationRegistry.getInstrumentation()
            .targetContext)

        webAppRequestHandler.onRequest("Hello World")
    }
}