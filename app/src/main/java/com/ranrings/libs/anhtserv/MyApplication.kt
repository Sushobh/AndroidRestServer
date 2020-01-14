package com.ranrings.libs.anhtserv

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import com.ranrings.libs.androidapptorest.AndroidRestServer
import com.ranrings.libs.androidapptorest.GetRequestHandler
import org.json.JSONObject



class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val androidRestServer = AndroidRestServer.Builder()
            .setApplication(this)
            .setPort(8080)
            .addRequestHandler(object : GetRequestHandler() {
                override fun onGetRequest(uri: String): Any {
                    return JSONObject().apply {
                        put("packagename",this@MyApplication.packageName)
                    }
                }
                override fun getMethodName(): String {
                    return "getpackagename"
                }
            })
            .
                startWebApp(true).
                build()
        androidRestServer.start()
    }
}
