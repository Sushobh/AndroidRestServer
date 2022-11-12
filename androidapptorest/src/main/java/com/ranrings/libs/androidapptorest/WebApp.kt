package com.ranrings.libs.androidapptorest

import android.content.Context
import com.ranrings.libs.androidapptorest.Base.GetInputStreamRequestHandler
import java.io.InputStream

abstract class WebApp(val context: Context, val zipFileStream: InputStream) :
    GetInputStreamRequestHandler() {


    abstract fun getWebAppFoldeName(): String

   internal fun setUp() {
       try {
           val webAppExtractor = WebAppExtractor(context, getWebAppFoldeName())
           webAppExtractor.extractFromAssets(zipFileStream)
       } catch (e: Exception) {
           e.printStackTrace()
       }
   }

    abstract fun getFileRootFolderPath(): String

}