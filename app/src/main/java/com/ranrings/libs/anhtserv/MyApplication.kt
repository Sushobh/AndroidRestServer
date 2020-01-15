package com.ranrings.libs.anhtserv

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import com.ranrings.libs.androidapptorest.AndroidRestServer

import com.ranrings.libs.androidapptorest.Base.GetRequestHandler
import com.ranrings.libs.androidapptorest.Base.PostRequestHandler
import com.ranrings.libs.androidapptorest.Base.RequestHandler
import org.json.JSONObject



class MyApplication : Application() {

    val port = 8080

    companion object {
        lateinit var application: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        val androidRestServer = AndroidRestServer.Builder()
            .setApplication(this)

            .setPort(port)
            .addRequestHandler(object : GetRequestHandler<Any>(){

                override fun onGetRequest(uri: String): Any {
                    return packageName
                }

                override fun getMethodName(): String {
                    return "getpackagename"
                }

            }).startWebApp(true).build()

        androidRestServer.start()

    }
}
