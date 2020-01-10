package com.ranrings.libs.androidapptorest.HandlerRepo

import com.ranrings.libs.androidapptorest.Base.GetRequestHandler
import com.ranrings.libs.androidapptorest.RequestCaller

internal class GetMethodsHandler(var requestCaller: RequestCaller) : GetRequestHandler<Any>() {


    override fun onGetRequest(uri: String) : Any{
        val methodNames  = arrayListOf<String>()
        requestCaller.getRequestHandlers().forEach {handler ->
            if(!requestCaller.isInternalRequest(handler.getMethodName())){
                methodNames.add(handler.getMethodName())
            }
        }
        return methodNames
    }

    override fun getMethodName(): String {
        return "getmethods"
    }




}