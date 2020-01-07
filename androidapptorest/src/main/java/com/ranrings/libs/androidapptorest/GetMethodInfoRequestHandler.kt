package com.ranrings.libs.androidapptorest

internal class GetMethodInfoRequestHandler(var requestCaller: RequestCaller) : GetRequestHandler()  {
    override fun onGetRequest(uri: String): Any {

        if(uri.split("/").size >= 3){
            val methodName = uri.split("/")[2].trim()
            requestCaller.requestHandlers.find {
                it.getMethodName().equals(methodName,ignoreCase = true)
            }?.let {
                if(requestCaller.isInternalRequest(it.getMethodName())){
                    return "[Internal request. No information available.]"
                }
                else if(it.isGetRequestHandler()){
                    return "[${it.getDescription()}]"
                }
                else {
                    val map = HashMap<String,String>()
                    it.classOfReq.java.declaredFields.forEach {
                        map.put(it.name,it.type.simpleName)
                    }
                    return map
                }
            }
        }


        return "No Method found"
    }

    override fun getMethodName(): String {
        return "getmethodinfo"
    }
}