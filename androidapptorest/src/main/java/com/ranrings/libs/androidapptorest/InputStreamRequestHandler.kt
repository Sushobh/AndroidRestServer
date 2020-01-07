package com.ranrings.libs.androidapptorest

import android.util.Log
import java.io.InputStream
import java.lang.Exception

abstract class InputStreamRequestHandler<X>(val requestClass : Class<X>) : RequestHandler<X,InputStream>(requestClass) {

    abstract fun getMimeType(requestUri: String): String
    abstract fun onRequestWithUri(requestBody: Any,uri : String) : InputStream

     open fun onRequest(requestBody: String): InputStream {
        Log.i("TAG","")
        throw Exception("For InputStreamRequestHandler, please use the onRequest method which provides uri of the request")
    }



}