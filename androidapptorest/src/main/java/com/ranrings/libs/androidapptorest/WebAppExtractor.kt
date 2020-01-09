package com.ranrings.libs.androidapptorest

import android.app.Application
import android.content.Context
import java.io.*
import java.lang.Exception
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


const val WEB_APP_REAL_FOLDER_NAME = "jarring-client"
const val WEB_APP_ZIP_NAME = "AndroidRestWebApp.zip"
const val WEB_APP_FOLDER_NAME = "dh3holdjhiewgrq2398ef"

fun getWebFolderPath(context: Context) : String{
    return context.filesDir.path+"/${WEB_APP_FOLDER_NAME}/${WEB_APP_REAL_FOLDER_NAME}"
}


class WebAppExtractor{

    var context: Context
    var folderInInternalStorage : String


    constructor(context: Context){
        this.context = context
        folderInInternalStorage = context.filesDir.path+"/${WEB_APP_FOLDER_NAME}"
        val file = File(folderInInternalStorage)
        file.mkdirs()
    }


    fun extract(): Boolean {
        val inputStream: InputStream
        val zis: ZipInputStream
        try {
            var filename: String
            inputStream = context.assets.open(WEB_APP_ZIP_NAME)
            zis = ZipInputStream(BufferedInputStream(inputStream))
            var ze: ZipEntry?
            val buffer = ByteArray(1024)
            var count: Int
            while (zis.nextEntry.also { ze = it } != null) {
                if(ze != null){
                    filename = ze!!.name
                    if (ze!!.isDirectory) {
                        val fmd = File(folderInInternalStorage, filename)
                        fmd.mkdirs()
                        continue
                    }
                    val fileToWrite = File(folderInInternalStorage,filename)
                    try{
                        fileToWrite.createNewFile()
                    }
                    catch (e : IOException){
                        continue
                    }
                    val fout = FileOutputStream(fileToWrite)
                    while (zis.read(buffer).also { count = it } != -1) {
                        fout.write(buffer, 0, count)
                    }
                    fout.close()
                    zis.closeEntry()
                }

            }
            zis.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
        return true
    }


}