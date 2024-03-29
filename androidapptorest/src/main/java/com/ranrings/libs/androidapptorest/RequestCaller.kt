package com.ranrings.libs.androidapptorest

import android.app.Application
import com.google.gson.Gson
import com.ranrings.libs.androidapptorest.Base.GetInputStreamRequestHandler
import com.ranrings.libs.androidapptorest.Base.GetRequestHandler
import com.ranrings.libs.androidapptorest.Base.RequestHandler
import com.ranrings.libs.androidapptorest.HandlerRepo.GetMethodInfoRequestHandler
import com.ranrings.libs.androidapptorest.HandlerRepo.GetMethodsHandler
import com.ranrings.libs.androidapptorest.HandlerRepo.PublicFileRequestHandler
import com.ranrings.libs.androidapptorest.HandlerRepo.WebAppRequestHandler
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoHTTPD.newChunkedResponse
import fi.iki.elonen.NanoHTTPD.newFixedLengthResponse
import java.io.InputStream
import java.util.*
import kotlin.reflect.KClass


internal open class RequestCaller(var application: Application) {
    private val requestHandlers = arrayListOf<RequestHandler<*, *>>()
    private val reservedMethodNames = arrayListOf<String>()
    private val internalrequestNames = arrayListOf<String>()
    private val webApps = arrayListOf<WebApp>()


    fun initialize(shouldStartWebApp: Boolean) {
        if (shouldStartWebApp) {
            addRequestHandler(
                WebAppRequestHandler(
                    application
                )
            )
            addRequestHandler(
                PublicFileRequestHandler(
                    application
                )
            )
        }
        addRequestHandler(
            GetMethodsHandler(
                this
            )
        )
        addRequestHandler(
            GetMethodInfoRequestHandler(
                this
            )
        )
        reservedMethodNames.add("webapp")
        reservedMethodNames.add("public")
        reservedMethodNames.add("getmethods")
        reservedMethodNames.add("getmethodinfo")
        internalrequestNames.add("webapp")
        internalrequestNames.add("public")
    }

    fun onRequestReceived(session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response {

        val getInputStreamHandler: RequestHandler<*, *>? = requestHandlers.find {
            isCapableOfHandlingUri(it, session.uri)
                    && it is GetInputStreamRequestHandler
        }
        getInputStreamHandler?.let {
            return handleInputStreamRequest(
                getInputStreamHandler as GetInputStreamRequestHandler,
                requestUri = session.uri
            )
        }

        val requestHandler: RequestHandler<*, *>? = requestHandlers.find {
            isCapableOfHandlingUri(it, session.uri)
        }
        requestHandler?.let {
            if (it.getMethodType() == RequestHandler.ASMethodType.GET && (it is GetRequestHandler)) {
                return handleGetRequest(requestHandler, session)
            } else {
                return handlePostRequest(requestHandler as RequestHandler<Any, Any>, session)
            }
        }

        return newFixedLengthResponse(
            NanoHTTPD.Response.Status.NOT_FOUND, "application/json",
            convertObjectToJSONString(ErrorResponse("No method found"))
        );
    }

    private fun handlePostRequest(
        requestHandler: RequestHandler<Any, Any>,
        session: NanoHTTPD.IHTTPSession
    ): NanoHTTPD.Response {
        val postBodyInString = getPostBodyStringFromSession(session)
        postBodyInString?.let { postBody ->
            val requestObject = parsePostBodyFromJSONString(postBody, requestHandler.classOfReq)
            val response = convertObjectToJSONString(requestHandler.onRequest(requestObject))

            return allowOrigin(
                newFixedLengthResponse(
                    NanoHTTPD.Response.Status.OK, "application/json",
                    response
                )
            )
        }

        return allowOrigin(
            newFixedLengthResponse(
                NanoHTTPD.Response.Status.NOT_FOUND, "application/json",
                convertObjectToJSONString(ErrorResponse("Invalid Request Object"))
            )
        )
    }

    private fun handleGetRequest(
        requestHandler: RequestHandler<*, *>,
        session: NanoHTTPD.IHTTPSession
    ): NanoHTTPD.Response {
        val response = convertObjectToJSONString(
            (requestHandler as GetRequestHandler).onGetRequest(session.uri)
        )

        return allowOrigin(
            newFixedLengthResponse(
                NanoHTTPD.Response.Status.OK, "application/json",
                response
            )
        )
    }


    fun allowOrigin(resp: NanoHTTPD.Response): NanoHTTPD.Response {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Max-Age", "3628800");
        resp.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS");
        resp.addHeader("Access-Control-Allow-Headers", "X-Requested-With");
        resp.addHeader("Access-Control-Allow-Headers", "Authorization");
        return resp
    }


    private fun handleInputStreamRequest(
        requestHandlerGet: GetInputStreamRequestHandler,
        requestUri: String = ""
    ): NanoHTTPD.Response {
        val response: InputStream = requestHandlerGet.onGetRequest(requestUri) as InputStream
        return newChunkedResponse(
            NanoHTTPD.Response.Status.OK, requestHandlerGet.getMimeType(requestUri),
            response
        );
    }

    fun parseUriToGetMethodName(uri: String): String? {
        uri.trim()
        val firstSlashIndex = uri.indexOf("/")
        if (firstSlashIndex == -1 || firstSlashIndex != 0) {
            return null
        } else {
            if (!isAllAlpha(uri.substring(1, uri.length))) {
                return null
            }
            return uri.substring(1, uri.length)
        }

    }


    fun <T> convertObjectToJSONString(objectToConvert: T): String? {
        return Gson().toJson(objectToConvert)
    }


    fun addRequestHandler(requestHandler: RequestHandler<*, *>) {
        if (!isAllAlpha(requestHandler.getMethodName())) {
            throw WrongRequestHandlerException("Method names can contain only letters.")
        } else if (reservedMethodNames.find
            {
                requestHandler.getMethodName().toLowerCase(Locale.ENGLISH).equals(
                    it.toLowerCase(
                        Locale.ENGLISH
                    )
                )
            } != null
        ) {
            throw WrongRequestHandlerException(
                "${requestHandler.getMethodName()}  is a reserved method name. " +
                        "Cannot use it"
            )
        }

        if (requestHandler.getMethodType() == RequestHandler.ASMethodType.GET) {
            requestHandlers.add(requestHandler)
        } else {
            if (!isValidRequestClass(requestClass = requestHandler.classOfReq)) {
                throw Exception("Invalid Request Class")
            } else {
                requestHandlers.add(requestHandler)
            }
        }


    }

    fun isValidRequestClass(requestClass: KClass<*>): Boolean {
        if (requestClass.isData) {
            requestClass.java.declaredFields
                .forEach {
                    if (!(it.type == String::class.java || it.type == Boolean::class.java
                                || it.type == Int::class.java ||
                                it.type == Double::class.java)
                    ) {
                        throw WrongRequestHandlerException("Only String,Boolean,Int,Double fields are supported for request classes")
                    }
                }
        } else {
            throw WrongRequestHandlerException("Only Data classes are supported for request classes")
        }
        return true
    }

    fun isInternalRequest(requestName: String): Boolean {
        return reservedMethodNames.find {
            it.equals(requestName)
        } != null
    }

    private fun isAllAlpha(str: String): Boolean {
        return str.matches(Regex("[a-zA-Z]+"));
    }

    fun getPostBodyStringFromSession(session: NanoHTTPD.IHTTPSession): String? {
        val map = HashMap<String, String>()
        session.parseBody(map)
        val toReturn = map["postData"]
        return toReturn
    }

    fun <T : Any> parsePostBodyFromJSONString(postBodyString: String, classType: KClass<T>): T {
        return Gson().fromJson(postBodyString, classType.java)
    }

    data class ErrorResponse(var messge: String) {

    }

    class WrongRequestHandlerException(message: String) : java.lang.Exception(message) {

    }

    class WrongWebAppConfigException(messge: String) : java.lang.Exception(messge) {

    }

    fun getRequestHandlers(): ArrayList<RequestHandler<*, *>> {
        return ArrayList(requestHandlers)
    }

    fun addWebApp(webApp: WebApp) {
        if (webApps.find { it.getWebAppFoldeName().equals(webApp.getWebAppFoldeName()) } != null) {
            throw WrongWebAppConfigException("WebApp root path name or foler path already is being used")
        }
        webApp.setUp()
        addRequestHandler(webApp)
        webApps.add(webApp)
    }

    fun isCapableOfHandlingUri(requestHandler: RequestHandler<*, *>, uri: String): Boolean {
        return try {
            uri.split("/").get(1).lowercase().equals(requestHandler.getMethodName().lowercase())
        } catch (e: Exception) {
            false
        }
    }

}