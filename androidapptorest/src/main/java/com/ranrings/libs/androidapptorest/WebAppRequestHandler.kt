package com.ranrings.libs.androidapptorest

import android.content.Context
import java.io.InputStream
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream


class WebAppRequestHandler : InputStreamRequestHandler<String> {

    var context : Context

    constructor( context: Context) : super(String::class.java) {
        this.context = context
    }

    var numBytes: Long = 0L

    override fun getMimeType(requestUri: String): String {
        return "text/html"
    }

    override fun getMethodName(): String {
        return "webapp"
    }



    override fun onRequestWithUri(requestBody: Any, uri : String): InputStream {
        val indexHtmlFile = File(getWebFolderPath(context)+"/index.html")
        return BufferedInputStream(FileInputStream(indexHtmlFile))
    }

    override fun onRequest(requestBody: String): InputStream {
        return super.onRequest(requestBody)
    }


}