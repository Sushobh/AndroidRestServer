package com.ranrings.libs.androidapptorest

import android.content.Context
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream


class PublicFileRequestHandler : InputStreamRequestHandler<String> {

    var context : Context

    constructor( context: Context) : super(String::class.java) {
        this.context = context
    }


    override fun getMimeType(requestUri: String): String = when (File(getFilePath(requestUri)).extension){
        "js" -> "application/javascript"
        "css" -> "application/css"
        else -> "text/plain"

    }

    override fun onRequestWithUri(requestBody: Any, uri: String): InputStream {

        val file = File(getFilePath(uri))
        return BufferedInputStream(FileInputStream(file))
    }

    override fun getMethodName(): String {
       return "public"
    }

    override fun onRequest(requestBody: String): InputStream {
       return super.onRequest(requestBody)
    }

     fun getFilePath(uri : String) : String {
       return getWebFolderPath(context)+"/${uri.split("/")[2]}"
    }
}