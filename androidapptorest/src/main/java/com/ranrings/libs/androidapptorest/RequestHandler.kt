package com.ranrings.libs.androidapptorest

abstract class RequestHandler<RequestBodyType,ResponseBodyType>(val classOfReq : Class<RequestBodyType>,
                                                                val classOfResp : Class<ResponseBodyType>? = null
                                                                ) {
    abstract fun getMethodName() : String
    abstract fun onRequest(requestBody : RequestBodyType ) : ResponseBodyType


 }