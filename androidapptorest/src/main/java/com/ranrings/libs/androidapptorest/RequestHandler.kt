package com.ranrings.libs.androidapptorest

import kotlin.reflect.KClass

abstract class RequestHandler<RequestBodyType,ResponseBodyType>(
    val classOfReq: KClass<*>,
    val classOfResp:KClass<*>? = null) {
    abstract fun getMethodName() : String
    abstract fun onRequest(requestBody : RequestBodyType ) : ResponseBodyType

    fun getDescription() : String {
        return "No Description available"
    }

    open fun isGetRequestHandler() : Boolean
    {
        return false
    }


 }