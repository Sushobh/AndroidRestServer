package com.ranrings.libs.androidapptorest

abstract class GetRequestHandler : RequestHandler<String,Any>(String::class.java) {

    override fun isGetRequestHandler(): Boolean {
        return true
    }

    override fun onRequest(requestBody: String): Any {
        throw Exception("Get Request Handler does not support this method")
    }

    abstract fun onGetRequest(uri : String) : Any

}