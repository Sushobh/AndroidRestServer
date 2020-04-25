package com.ranrings.libs.androidapptorest.HandlerRepo

import android.content.Context
import com.ranrings.libs.androidapptorest.Base.GetInputStreamRequestHandler
import com.ranrings.libs.androidapptorest.getWebFolderPath
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream


open class PublicFileRequestHandler :
    GetInputStreamRequestHandler {

    var context : Context

    constructor( context: Context) : super() {
        this.context = context
    }


    override fun getMimeType(requestUri: String): String = when (File(getFilePath(requestUri)).extension){
        "js" -> "application/javascript"
        "css" -> "application/css"
        else -> "text/plain"
    }


    override fun getMethodName(): String {
       return "public"
    }


    override fun onGetRequest(uri: String): InputStream {
        val file = File(getFilePath(uri))
        return BufferedInputStream(FileInputStream(file))
    }

    open fun getFilePath(uri : String) : String {
       return getWebFolderPath(context) +"/${uri.split("/")[2]}"
    }
}