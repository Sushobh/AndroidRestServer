package com.ranrings.libs.androidapptorest

class GetMethodsHandler(var requestCaller: RequestCaller) : GetRequestHandler() {


    override fun onGetRequest(uri: String) : Any{
        val methodNames  = arrayListOf<String>()
        requestCaller.requestHandlers.forEach({
            methodNames.add(it.getMethodName())
        })
        return methodNames
    }

    override fun getMethodName(): String {
        return "getmethods"
    }




}