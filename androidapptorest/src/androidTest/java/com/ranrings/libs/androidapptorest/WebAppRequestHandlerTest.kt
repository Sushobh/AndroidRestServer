package com.ranrings.libs.androidapptorest

import androidx.test.platform.app.InstrumentationRegistry
import com.ranrings.libs.androidapptorest.HandlerRepo.WebAppRequestHandler
import org.junit.Test

class WebAppRequestHandlerTest {

    @Test
    fun onRequest() {
        val webAppRequestHandler =
            WebAppRequestHandler(
                InstrumentationRegistry.getInstrumentation()
                    .targetContext
            )

        webAppRequestHandler.onRequest("Hello World")
    }
}