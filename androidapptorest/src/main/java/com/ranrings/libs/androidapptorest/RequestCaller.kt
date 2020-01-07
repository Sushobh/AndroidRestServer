package com.ranrings.libs.androidapptorest

import android.app.Application
import com.google.gson.Gson
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoHTTPD.newChunkedResponse
import fi.iki.elonen.NanoHTTPD.newFixedLengthResponse
import java.io.InputStream
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KClass


open class RequestCaller(application: Application) {
    val requestHandlers  = arrayListOf<RequestHandler<*,*>>()
    val reservedMethodNames = arrayOf("methods","methodInfo")

    init {
        addRequestHandler(WebAppRequestHandler(application))
        addRequestHandler(PublicFileRequestHandler(application))
        addRequestHandler(GetMethodsHandler(this))
    }

    fun onRequestReceived(session : NanoHTTPD.IHTTPSession): NanoHTTPD.Response {

        val getInputStreamHandler : GetInputStreamRequestHandler? = requestHandlers.find {
            session.uri.substring(1,session.uri.length).startsWith(it.getMethodName())
                    && it is GetInputStreamRequestHandler
        } as GetInputStreamRequestHandler
        if(getInputStreamHandler != null){
            return handleInputStreamRequest(getInputStreamHandler,requestUri = session.uri)
        }
        else {
            val requestHandler : RequestHandler<*,*>? = requestHandlers.find {
                it.getMethodName().equals(parseUriToGetMethodName(session.uri))
            }
            requestHandler?.let {
                if(it.isGetRequestHandler() && (it is GetRequestHandler)){
                   return handleGetRequest(requestHandler,session)
                }
                else {
                    return handlePostRequest(requestHandler as RequestHandler<Any, Any>,session)
                }

            }
        }

        return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "application/json",
               convertObjectToJSONString(ErrorResponse("No method found"))
            );
    }

    private fun handlePostRequest(requestHandler: RequestHandler<Any,Any>,session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response {
        val postBodyInString = getPostBodyStringFromSession(session)
        postBodyInString?.let { postBody ->
            val requestObject = parsePostBodyFromJSONString(postBody,requestHandler.classOfReq)
            val response = convertObjectToJSONString(requestHandler.onRequest(requestObject))
            return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json",
                response
            );
        }

       throw return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "application/json",
           convertObjectToJSONString(ErrorResponse("Invalid Request Object"))
       );
    }

    private fun handleGetRequest(requestHandler: RequestHandler<*, *>,session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response {
        val response = convertObjectToJSONString((requestHandler as GetRequestHandler).
            onGetRequest(session.uri))
        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json",
            response
        );

    }



    private fun handleInputStreamRequest(
        requestHandlerGet: GetInputStreamRequestHandler,
        requestUri: String = ""
      ): NanoHTTPD.Response {
        val response : InputStream = requestHandlerGet.onGetRequest(requestUri) as InputStream
        return newChunkedResponse(NanoHTTPD.Response.Status.OK, requestHandlerGet.getMimeType(requestUri),
            response
        );
    }

    fun parseUriToGetMethodName(uri: String) : String?{
        uri.trim()
        val firstSlashIndex =  uri.indexOf("/")
        if(firstSlashIndex == -1 || firstSlashIndex != 0){
            return null
        }
        else {
            if(!isAllAlpha(uri.substring(1,uri.length))){
                return null
            }
            return uri.substring(1,uri.length)
        }

    }



    fun <T> convertObjectToJSONString( objectToConvert : T) : String?{
        return Gson().toJson(objectToConvert)
    }



    fun addRequestHandler(requestHandler: RequestHandler<*,*>) {
        if(!isAllAlpha(requestHandler.getMethodName())){
            throw WrongRequestHandlerException("Method names can contain only letters.")
        }
        else if(reservedMethodNames.find
            { requestHandler.getMethodName().toLowerCase(Locale.ENGLISH).equals(it.toLowerCase(
                Locale.ENGLISH))} != null) {
            throw WrongRequestHandlerException("${requestHandler.getMethodName()}  is a reserved method name. " +
                    "Cannot use it")
        }

        if(requestHandler.isGetRequestHandler()){
            requestHandlers.add(requestHandler)
        }
        else {
            if(!isValidRequestClass(requestClass = requestHandler.classOfReq::class)){
                throw Exception("Invalid Request Class")
            }
            else {
                requestHandlers.add(requestHandler)
            }
        }


    }

    fun isValidRequestClass(requestClass: KClass<*>) : Boolean{
        if(requestClass.isData) {
            requestClass.java.declaredFields
                .forEach {
                    if(!(it.type == String::class.java || it.type == Boolean::class.java
                                || it.type == Int::class.java ||
                                it.type == Double::class.java)){
                        throw WrongRequestHandlerException("Only String,Boolean,Int,Double fields are supported for request classes")
                    }
                }
        }
        else {
            throw WrongRequestHandlerException("Only Data classes are supported for request classes")
        }
        return true
    }

    private  fun isAllAlpha(str : String) : Boolean{
        return str.matches(Regex("[a-zA-Z]+"));
    }

    fun getPostBodyStringFromSession(session: NanoHTTPD.IHTTPSession) : String?{
        val map = HashMap<String,String>()
        session.parseBody(map)
        val toReturn =  map["postData"]
        return toReturn
    }

    fun <T> parsePostBodyFromJSONString(postBodyString : String,classType : Class<T>) : T  {
        return Gson().fromJson(postBodyString,classType)
    }

    data class ErrorResponse(var messge : String){

    }

    class WrongRequestHandlerException(message : String) : java.lang.Exception(message){

    }


}