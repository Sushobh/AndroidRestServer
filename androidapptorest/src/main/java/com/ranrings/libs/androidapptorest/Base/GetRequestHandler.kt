package com.ranrings.libs.androidapptorest.Base

abstract class GetRequestHandler<X> : RequestHandler<String, X>(String::class) {


    override fun getMethodType(): ASMethodType {
        return ASMethodType.GET
    }


    override fun onRequest(requestBody: String): X {
        throw Exception("Get Request Handler does not support this method")
    }

    abstract fun onGetRequest(uri : String) : X

}