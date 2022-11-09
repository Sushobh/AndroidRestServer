package com.ranrings.libs.androidapptorest

import android.content.Context
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.net.URLConnection

const val REACTWEBAPP_FOLDER_NAME = "asjdaskjodg08qw4eq2873e4"

class ReactWebApp(context: Context, zipFileStream: InputStream, val rootUrlName: String) :
    WebApp(context, zipFileStream) {


    override fun getWebAppFoldeName(): String {
        return REACTWEBAPP_FOLDER_NAME
    }

    override fun onGetRequest(uri: String): InputStream {
        val filePath = getFilePath(requestUri = uri)
        return BufferedInputStream(FileInputStream(filePath))
    }

    private fun getFilePath(requestUri: String): String {
        val splitUris = requestUri.split("/")
        if (splitUris.size == 2) {
            return getIndexHtmlFilePath()
        } else {
            val stringBuilder = StringBuilder()
            stringBuilder.append("/")
            splitUris.forEachIndexed { index, string ->
                if (index > 1) {
                    stringBuilder.append(string).append("/")
                }
            }
            val filePath = getFileRootFolderPath() + "/${stringBuilder}"
            return filePath
        }
    }

    override fun getMethodName(): String {

        return rootUrlName
    }

    override fun getFileRootFolderPath(): String {
        return context.filesDir.path + "/${getWebAppFoldeName()}/build"
    }

    override fun getMimeType(requestUri: String): String {
//        "js" -> "application/javascript"
//        "css" -> "application/css"
//        "json" -> "application/json"
//        "html" -> "text/html"
//        else -> "text/plain"
        val mimeType =
            URLConnection.guessContentTypeFromName(File(getFilePath(requestUri)).getName())

        return mimeType ?: "text/plain"
    }


    fun getIndexHtmlFilePath(): String {
        return getFileRootFolderPath() + "/index.html"
    }


}