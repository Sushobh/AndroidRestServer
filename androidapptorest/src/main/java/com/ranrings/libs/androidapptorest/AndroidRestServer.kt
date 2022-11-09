package com.ranrings.libs.androidapptorest

import android.app.Application
import com.ranrings.libs.androidapptorest.Base.RequestHandler
import fi.iki.elonen.NanoHTTPD


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
        private var requestHandlers = arrayListOf<RequestHandler<*, *>>()
        private var webApps = arrayListOf<WebApp>()
        private var application: Application? = null
        private var shouldStartWebApp = false

        constructor() {
            androidRestServer = AndroidRestServer()
        }


        fun addRequestHandler(requetHandler: RequestHandler<*, *>): Builder {
            requestHandlers.add(requetHandler)
            return this
        }

        fun addWebApp(webApp: WebApp): Builder {
            webApps.add(webApp)
            return this
        }

        fun setPort(portNumber: Int): Builder {
            androidRestServer.port = portNumber
            return this
        }

        fun setApplication(application: Application): Builder {
            this.application = application
            return this
        }

        fun startWebApp(shouldStart : Boolean): Builder {
            this.shouldStartWebApp = shouldStart
            return this
        }



        fun build() : AndroidRestServer {
              application?.let {
                  if (shouldStartWebApp) {
                      WebAppExtractor(it, WEB_APP_FOLDER_NAME).extractFromAssets(
                          it.assets.open(
                              WEB_APP_ZIP_NAME
                          )
                      )
                  }
                  androidRestServer.application = it
                  androidRestServer.requestCaller = RequestCaller(it)
                  androidRestServer.requestCaller.initialize(shouldStartWebApp)
                  requestHandlers.forEach({
                      androidRestServer.requestCaller.addRequestHandler(it)
                  })
                  webApps.forEach {
                      androidRestServer.requestCaller.addWebApp(it)
                  }
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