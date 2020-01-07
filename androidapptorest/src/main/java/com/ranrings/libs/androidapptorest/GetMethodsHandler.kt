package com.ranrings.libs.androidapptorest

internal class GetMethodsHandler(var requestCaller: RequestCaller) : GetRequestHandler() {


    override fun onGetRequest(uri: String) : Any{
        val methodNames  = arrayListOf<String>()
        requestCaller.requestHandlers.forEach {handler ->
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