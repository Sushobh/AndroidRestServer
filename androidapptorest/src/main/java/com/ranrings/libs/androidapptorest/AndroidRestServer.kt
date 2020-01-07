package com.ranrings.libs.androidapptorest

import android.app.Application
import fi.iki.elonen.NanoHTTPD
import java.lang.Exception


class AndroidRestServer {

    private var port : Int = 8080
    private lateinit var nanoHttpServer: NanoHttpServer
    private lateinit  var requestCaller : RequestCaller
    private var application : Application? = null

    private constructor(){

    }

    fun start(){
       nanoHttpServer =  NanoHttpServer(port,{ session ->
           requestCaller.onRequestReceived(session)
       })
       try {
           nanoHttpServer.start()
       }
       catch (e : Exception) {
           e.printStackTrace()
       }
    }

    fun stop() {
        nanoHttpServer.stop()
    }



    class Builder {

        private var androidRestServer: AndroidRestServer;

        constructor(){
            androidRestServer =  AndroidRestServer()
        }


        fun addRequestHandler(requetHandler : RequestHandler<*,*>) : Builder{
            androidRestServer.requestCaller.addRequestHandler(requetHandler)
            return this
        }

        fun setPort(portNumber : Int)  : Builder {
            androidRestServer.port = portNumber
            return this
        }

        fun setApplication(application: Application): Builder {
            androidRestServer.application = application
            androidRestServer.requestCaller = RequestCaller(application)
            return this
        }

        internal fun buildForTest() : AndroidRestServer{
            return androidRestServer
        }

        fun build() : AndroidRestServer {
             androidRestServer.application?.let {
                 WebAppExtractor(it).extract()
                 return androidRestServer;
             }
             throw Exception("Please set application in the builder")
        }

    }

    private class NanoHttpServer(
        portNumber: Int, var handler:
            (session: IHTTPSession) -> Response
    )
                                 : NanoHTTPD(portNumber) {

        override fun serve(session: IHTTPSession?): Response {
            return if(session == null){
                newFixedLengthResponse(Response.Status.NOT_IMPLEMENTED,"text/plain", "No receiver found");
            }
            else {
                val respToReturn =  handler(session)
                return respToReturn
            }
        }
    }

}