package com.ranrings.libs.androidapptorest

import android.content.Context
import android.util.Log
import com.ranrings.libs.androidapptorest.Base.GetInputStreamRequestHandler
import java.io.InputStream

abstract class WebApp(val context: Context, val zipFileStream: InputStream) :
    GetInputStreamRequestHandler() {


    abstract fun getWebAppFoldeName(): String

    fun setUp() {
        try {
            val webAppExtractor = WebAppExtractor(context, getWebAppFoldeName())
            webAppExtractor.extractFromAssets(zipFileStream)
            Log.i("Eventyx", "File extracted succesfully")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    abstract fun getFileRootFolderPath(): String

}