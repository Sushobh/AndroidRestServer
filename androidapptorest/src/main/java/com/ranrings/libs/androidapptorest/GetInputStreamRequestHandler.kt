package com.ranrings.libs.androidapptorest

import android.util.Log
import java.io.InputStream
import java.lang.Exception

abstract class GetInputStreamRequestHandler() : GetRequestHandler() {

    abstract fun getMimeType(requestUri: String): String

}