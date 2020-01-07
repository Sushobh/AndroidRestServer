package com.ranrings.libs.androidapptorest

class GetMethodInfoRequestHandler(var requestCaller: RequestCaller) : GetRequestHandler()  {
    override fun onGetRequest(uri: String): Any {

        if(uri.split("/").size >= 3){
            val methodName = uri.split("/")[2].trim()
            requestCaller.requestHandlers.find {
                it.getMethodName().equals(methodName,ignoreCase = true)
            }?.let {
                val map = HashMap<String,String>()
                it.classOfReq.declaredFields.forEach {
                    map.put(it.name,it.type.simpleName)
                }
                return map
            }
        }


        return "No Method found"
    }

    override fun getMethodName(): String {
        return "getmethodinfo"
    }
}