package com.ranrings.libs.androidapptorest.HandlerRepo

import com.ranrings.libs.androidapptorest.Base.PostRequestHandler
import com.ranrings.libs.androidapptorest.RequestCaller
import com.ranrings.libs.androidapptorest.RequestDescription

internal class GetMethodInfoRequestHandler(var requestCaller: RequestCaller) :
    PostRequestHandler<GetMethodInfoRequestHandler.MethodInfoReqBody, Any>(
        MethodInfoReqBody::class)
  {
      override fun onRequest(requestBody: MethodInfoReqBody): Any {
          requestCaller.getRequestHandlers().find {
              it.getMethodName().equals(requestBody.methodName,ignoreCase = true)
          }?.let {
              if(requestCaller.isInternalRequest(it.getMethodName())){
                  return "[Internal request. No information available.]"
              }
              else if(it.getMethodType() == ASMethodType.GET){
                  return RequestDescription(
                      it.getDescription(),
                      HashMap(),
                      it.getMethodName()
                  )
              }
              else {
                  val map = HashMap<String,String>()
                  it.classOfReq.java.declaredFields.forEach {
                      map.put(it.name,it.type.simpleName)
                  }
                  return RequestDescription(
                      it.getDescription(),
                      map,
                      methodName = it.getMethodName()
                  )
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