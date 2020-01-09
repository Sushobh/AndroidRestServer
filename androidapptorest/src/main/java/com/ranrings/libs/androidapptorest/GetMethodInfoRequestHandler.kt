package com.ranrings.libs.androidapptorest

internal class GetMethodInfoRequestHandler(var requestCaller: RequestCaller) :
    RequestHandler<GetMethodInfoRequestHandler.MethodInfoReqBody,Any>(MethodInfoReqBody::class)
  {
      override fun onRequest(requestBody: MethodInfoReqBody): Any {
          requestCaller.requestHandlers.find {
              it.getMethodName().equals(requestBody.methodName,ignoreCase = true)
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
                  return RequestDescription(it.getDescription(),map,methodName = it.getMethodName())
              }
          }

          return "{message : 'No method found'}"
      }


    override fun getMethodName(): String {
        return "getmethodinfo"
    }


    data class MethodInfoReqBody(var methodName: String){

    }
}