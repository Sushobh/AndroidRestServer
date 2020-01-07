package com.ranrings.libs.androidapptorest

import android.content.Context
import java.io.InputStream
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream


class WebAppRequestHandler : GetInputStreamRequestHandler {

    var context : Context

    constructor( context: Context) : super() {
        this.context = context
    }

    var numBytes: Long = 0L

    override fun getMimeType(requestUri: String): String {
        return "text/html"
    }

    override fun getMethodName(): String {
        return "webapp"
    }



    override fun onGetRequest(uri: String): Any {
        val indexHtmlFile = File(getWebFolderPath(context)+"/index.html")
        return BufferedInputStream(FileInputStream(indexHtmlFile))
    }


}