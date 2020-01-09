package com.ranrings.libs.androidapptorest

import fi.iki.elonen.NanoHTTPD
import java.net.Authenticator

abstract class GetRequestHandler : RequestHandler<String,Any>(String::class) {



    override fun isGetRequestHandler(): Boolean {
        return true
    }

    override fun onRequest(requestBody: String): Any {
        throw Exception("Get Request Handler does not support this method")
    }

    abstract fun onGetRequest(uri : String) : Any

}