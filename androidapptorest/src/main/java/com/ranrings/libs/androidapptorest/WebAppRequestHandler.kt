package com.ranrings.libs.androidapptorest

import android.app.Application
import android.content.Context
import android.webkit.MimeTypeMap
import java.io.InputStream
import android.content.res.AssetFileDescriptor
import java.io.BufferedInputStream


class WebAppRequestHandler : InputStreamRequestHandler<String> {

    var context : Context

    constructor( context: Context) : super(String::class.java) {
        this.context = context
    }

    var numBytes: Long = 0L

    override fun getMimeType(): String {
        return "text/html"
    }

    override fun getMethodName(): String {
        return "webapp"
    }



    override fun onRequest(requestBody: String): InputStream {
        val fileDescriptor = context.assets.openFd("webapp/index.html")
        return BufferedInputStream(fileDescriptor.createInputStream())
    }



}