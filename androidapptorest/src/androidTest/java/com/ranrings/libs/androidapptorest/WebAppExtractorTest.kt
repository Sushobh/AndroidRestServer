package com.ranrings.libs.androidapptorest

import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertTrue
import org.junit.Test


import java.io.File



 class WebAppExtractorTest {


    @Test
    fun extract() {
        val context = InstrumentationRegistry.
            getInstrumentation().
            targetContext

        var webAppExtractor : WebAppExtractor = WebAppExtractor(context)
        webAppExtractor.extract()
        val file = File(context.filesDir.path+"/"+ WEB_APP_FOLDER_NAME)
        assertTrue(file.exists())

    }
}