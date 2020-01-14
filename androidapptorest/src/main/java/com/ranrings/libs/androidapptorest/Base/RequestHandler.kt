package com.ranrings.libs.androidapptorest.Base

import kotlin.reflect.KClass

abstract  class RequestHandler<RequestBodyType,ResponseBodyType>(
    val classOfReq: KClass<*>,
    val classOfResp:KClass<*>? = null)

{
    abstract fun getMethodName() : String
    abstract fun onRequest(requestBody : RequestBodyType ) : ResponseBodyType
    abstract fun getMethodType() : ASMethodType

    fun getDescription() : String {
        return "No Description available"
    }



    enum class ASMethodType {
        GET,POST
    }


 }